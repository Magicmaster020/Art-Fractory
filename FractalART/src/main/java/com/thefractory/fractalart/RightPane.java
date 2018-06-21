package com.thefractory.fractalart;

import com.thefractory.customcomponents.InfoIcon;
import com.thefractory.customcomponents.NumberField;
import com.thefractory.customcomponents.SpringSlider;

import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class RightPane extends GridPane {

	//FXML injected elements.
	@FXML private ImageView preview;
	@FXML private AnchorPane previewPane;
	@FXML private Region shadeTop;
	@FXML private Region shadeBottom;
	@FXML private Polygon proportionDraggerTop;
	@FXML private Polygon proportionDraggerBottom;
	@FXML private NumberField xField;
	@FXML private NumberField yField;
	@FXML private NumberField angleField;
	@FXML private NumberField zoomField;
	@FXML private InfoIcon xInfo;
	@FXML private InfoIcon yInfo;
	@FXML private InfoIcon angleInfo;
	@FXML private InfoIcon zoomInfo;
	@FXML private SpringSlider xSlider;
	@FXML private SpringSlider ySlider;
	@FXML private Slider angleSlider;
	@FXML private Slider zoomSlider;
	@FXML private Button recenter;
	@FXML private CheckBox showAxes;
	@FXML private CheckBox enableProportions;
	@FXML private NumberField proportionField;
	
	private DoubleProperty proportionProperty;
	Panner xPanner;
	Panner yPanner;
	
	//For Drag Events
	private double proportionDragY;
	private double[] lastCors = {0, 0};
	
	public RightPane() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RightPane.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.getStylesheets().add(getClass().getResource("rightpane.css").toString());
        
        proportionProperty = new SimpleDoubleProperty(proportionField.getValue());
        enableProportions.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					proportionProperty.bind(proportionField.valueProperty());
					proportionField.setDisable(false);
					proportionDraggerTop.setVisible(true);
					proportionDraggerBottom.setVisible(true);
				} else {
					proportionProperty.unbind();
					proportionProperty.setValue(1);
					proportionField.setDisable(true);
					proportionDraggerTop.setVisible(false);
					proportionDraggerBottom.setVisible(false);
				}
			}
        });

        previewPane.maxHeightProperty().bind(Bindings.min(this.heightProperty().subtract(224), this.widthProperty()));
        previewPane.maxWidthProperty().bind(previewPane.heightProperty());
        preview.fitWidthProperty().bind(previewPane.maxWidthProperty());
        preview.fitHeightProperty().bind(previewPane.maxHeightProperty());
        shadeTop.prefHeightProperty().bind(
        		previewPane.heightProperty().multiply(proportionProperty.subtract(1)).multiply(-0.5));
        shadeBottom.prefHeightProperty().bind(shadeTop.prefHeightProperty());
        
        angleSlider.valueProperty().bindBidirectional(angleField.valueProperty());
        zoomSlider.valueProperty().bindBidirectional(zoomField.valueProperty());
        
        xPanner = new Panner(xField.valueProperty());
        yPanner = new Panner(yField.valueProperty());        
        xSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				xPanner.setSpeed(newValue.doubleValue());
			}
        });
        ySlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				yPanner.setSpeed(newValue.doubleValue());
			}
        });
		new Thread(xPanner.task).start();
		new Thread(yPanner.task).start();
	}
	
	public void setImage(Image image) {
		preview.setImage(image);
	}
	
	private class Panner {
		
		private DoubleProperty property;
		private double speed = 0;
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				while(true) {
					try {
						Thread.sleep(10);
						Platform.runLater(() -> {
							property.setValue(property.getValue() + speed/zoomField.getValue());
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		private Panner(DoubleProperty property) {
			this.property = property;
		}
		
		private void setSpeed(double speed) {
			this.speed = speed/50;
		}
	}
	
	@FXML public void recenter() {
		xField.setValue(0);
		yField.setValue(0);
		angleField.setValue(0);
		zoomField.setValue(1);
	}

	@FXML public void proportionDragStart(MouseEvent event) {
		proportionDragY = event.getSceneY();
	}
	
	@FXML public void proportionDragTop(MouseEvent event) {
		if(enableProportions.isSelected()) {
			double y = event.getSceneY();
			proportionField.setValue(proportionField.getValue() - 2*((double) y - proportionDragY)/previewPane.getHeight());
	
			proportionDragY = y;
		}
	}
	
	@FXML public void proportionDragBottom(MouseEvent event) {
		if(enableProportions.isSelected()) {
			double y = event.getSceneY();
			proportionField.setValue(proportionField.getValue() - 2*((double) proportionDragY - y)/previewPane.getHeight());
	
			proportionDragY = y;
		}
	}

	@FXML public void panStart(MouseEvent event) {
		lastCors[0] = event.getX()/preview.getFitWidth();
		lastCors[1] = event.getY()/preview.getFitHeight();
	}
    
	@FXML public void pan(MouseEvent event) {
		double scale = 1.0/zoomField.getValue();
		xField.setValue(xField.getValue() + scale * (lastCors[0] - event.getX()/preview.getFitHeight()));
		yField.setValue(yField.getValue() + scale * (event.getY()/preview.getFitWidth() - lastCors[1]));
		lastCors[0] = event.getX()/preview.getFitHeight();
		lastCors[1] = event.getY()/preview.getFitWidth();
	}
	
	@FXML public void zoom(ScrollEvent event) {
    	double scale = 1.1;
    	if(event.getDeltaY() > 0) {
    		scale = 1/scale;
    	}
    	
    	xField.setValue(xField.getValue() + scale * (1 - scale) * (event.getX()/preview.getFitWidth() - 0.5));
    	yField.setValue(yField.getValue() + scale * (1 - scale) * (0.5 - event.getY()/preview.getFitHeight()));
		zoomField.setValue(zoomField.getValue() / scale);
		lastCors[0] = event.getX()/preview.getFitHeight();
		lastCors[1] = event.getY()/preview.getFitWidth();
	}

	@FXML public void fullScreen() {
		System.out.println("Open full screen.");
	}
}