package com.thefractory.fractalart;

import java.io.IOException;
import java.util.concurrent.Future;
import com.thefractory.customcomponents.NumberField;
import com.thefractory.fractalart.utils.EnhancedCallable;
import com.thefractory.fractalart.utils.JavaFXUtils;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public abstract class Artwork extends Tab {

	protected String DEFAULT_NAME = "New Artwork";
	protected String name = DEFAULT_NAME;
	protected static ThreadPool threadPool;
	
	protected WritableImage image;
	private Image fullScreenImage;
	private int lowResolution = 50;
	private Future<WritableImage> highResFuture;
	
	@FXML protected RightPane rightPane;
    @FXML private SplitPane splitPane;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private AnchorPane rightAnchorPane;
    
    protected ChangeListener<Object> updateListener = new ChangeListener<Object>() {
        @Override
        public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
            updateLowResImage();
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

		updateHighResImage((int) rightPane.resolutionField.getValue());
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
	
	protected void setImage() {
		rightPane.setImage(this.image);
	}
	
	public void updateLowResImage() {
		Future<WritableImage> future = threadPool.submit(getImageTask(lowResolution, lowResolution), false);
		try {
			setImage(future.get());
			setImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int resolution = (int) rightPane.resolutionField.getValue();
		if(resolution > lowResolution) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					updateHighResImage(resolution);					
				}
			}).start();
		}
	}
	
	public void updateHighResImage(int resolution) {
		if(highResFuture != null) {
			//TODO Doesn't cancel.
			highResFuture.cancel(true);
		}
		
		EnhancedCallable<WritableImage> task = getImageTask(resolution, resolution);
		task.setDescription("Updating " + name);
		highResFuture = threadPool.submit(task, true);
		try {
			WritableImage image = highResFuture.get();
			if(image != null) {
				setImage(image);
				setImage();
			}
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
	public void setImage(WritableImage image) {
		this.image = image;
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
