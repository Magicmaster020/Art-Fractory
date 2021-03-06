package com.thefractory.fractalart;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.thefractory.customcomponents.NumberField;
import com.thefractory.fractalart.utils.EnhancedCallable;
import com.thefractory.fractalart.utils.JavaFXUtils;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public abstract class Artwork extends Tab {

	public static String defaultName = "New Artwork";
	public String name = defaultName;
	public static String description = "No description set.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotSecondImage.tif");

	protected static ThreadPool threadPool;
	
	protected WritableImage image;
	private Image fullScreenImage;
	private int lowResolution = 50;
	
	@FXML protected RightPane rightPane;
    @FXML private SplitPane splitPane;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private AnchorPane rightAnchorPane;
    
    private IntegerProperty numberOfUpdaters = new SimpleIntegerProperty(0);

    protected ChangeListener<Object> updateListener = new ChangeListener<Object>() {
        @Override
        public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
            new UpdateHandler();
        }
    };
    public EnhancedCallable<WritableImage> getImageTask(int width, int height) {
		return new EnhancedCallable<WritableImage>("Generating " + name) {
			@Override
			public WritableImage call() {
				return getImage(width, height, this);
			}
		};	
	}
	
	public Artwork() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Artwork.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
		this.setText(name);
		this.setContent(splitPane);
		for(Node child : JavaFXUtils.getNodesFromType(rightPane, NumberField.class)) {
			((NumberField) child).valueProperty().addListener(updateListener);
		}
	}
	
	protected void setMainPane(StackPane mainPane) {
        this.mainAnchorPane.getChildren().add(mainPane);
        AnchorPane.setTopAnchor(mainPane, 0.0);
        AnchorPane.setRightAnchor(mainPane, 0.0);
        AnchorPane.setBottomAnchor(mainPane, 0.0);
        AnchorPane.setLeftAnchor(mainPane, 0.0);
	}
	
	protected void setControlPanelPrefWidth(double value) {
		mainAnchorPane.setPrefWidth(value);
	}
	
	public void getFile() {
		
	}
	
	public Artwork openFromFile() {
		return null;
	}
	
	private class UpdateHandler {
		
		private boolean cancelled = false;
		private int number;
		private Future<WritableImage> future;
		private ChangeListener<Number> listener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.intValue() > number) {
					cancel();						
				}
			}				
		}; 
		
		private UpdateHandler() {
			number = numberOfUpdaters.get() + 1;
			numberOfUpdaters.set(number);
			numberOfUpdaters.addListener(listener);	
			if(number < numberOfUpdaters.get()) {//If the number was increased before the listener was applied.
				cancel();
			}
			
			updateLowResImage();

			int resolution = (int) rightPane.resolutionField.getValue();
			if(resolution > lowResolution && !isCancelled()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						EnhancedCallable<WritableImage> task = getImageTask(resolution, resolution);
						task.setDescription("Generating " + name);
						future = threadPool.submit(task, true);
						if(isCancelled()) {//If cancelled before construction but after check cancel again.
							cancel();
						}
						
						try {
							WritableImage image = future.get();
							if(image != null) {
								setImage(image);
							}
						} catch (ExecutionException | InterruptedException | CancellationException e) {
							//Do nothing.
						} finally {//When we are done cancel to make sure the listener is removed.
							cancel();
						}
					}
				}).start();
			}
		}
		
		/**
		 * Cancels the future or prevents it from starting. Can be called several times without problems.
		 */
		private void cancel() {
			cancelled = true;
			try {
				if(!future.isDone() && !future.isCancelled()) {//If Future is still active.
					future.cancel(true);
				}
				
				if(future.isDone() || future.isCancelled()) {//If Future is not active anymore.
					numberOfUpdaters.removeListener(listener);
				}
			} catch(NullPointerException e) {//If the Future is not yet initialised.
				numberOfUpdaters.removeListener(listener);
			}
		}
		private boolean isCancelled() {
			return cancelled;
		}	
	}
	
	public void updateLowResImage() {
		try {
			setImage(threadPool.submit(getImageTask(lowResolution, lowResolution), false).get());
			//System.out.println("Low res Future completed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public abstract void init();
	
	public WritableImage getImage() {
		return image;
	}
	
	public abstract WritableImage getImage(int height, int length, EnhancedCallable<WritableImage> task);
	
	public RightPane getRightPane() {
		return rightPane;
	}
	public synchronized void setImage(WritableImage image) {
		this.image = image;
		rightPane.setImage(this.image);
	}
	public Image getFullScreenImage() {
		return fullScreenImage;
	}
	public void setFullScreenImage(Image fullScreenImage) {
		this.fullScreenImage = fullScreenImage;
	}
	public int getLowResolution() {
		return lowResolution;
	}
	public void setLowResolution(int lowResolution) {
		this.lowResolution = lowResolution;
	}
	public static void setThreadPool(ThreadPool threadPool) {
		Artwork.threadPool = threadPool;
	}
}
