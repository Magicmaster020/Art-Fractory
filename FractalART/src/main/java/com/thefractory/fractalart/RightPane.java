package com.thefractory.fractalart;

import com.thefractory.customcomponents.InfoIcon;
import com.thefractory.customcomponents.NumberField;
import com.thefractory.customcomponents.SpringSlider;
import com.thefractory.customcomponents.CircularSlider;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
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
	@FXML protected NumberField xField;
	@FXML protected NumberField yField;
	@FXML protected NumberField angleField;
	@FXML protected NumberField zoomField;
	@FXML private InfoIcon xInfo;
	@FXML private InfoIcon yInfo;
	@FXML private InfoIcon angleInfo;
	@FXML private InfoIcon zoomInfo;
	@FXML private SpringSlider xSlider;
	@FXML private SpringSlider ySlider;
	@FXML private CircularSlider angleSlider;
	@FXML private CircularSlider zoomSlider;
	@FXML private Button recenter;
	@FXML private CheckBox showAxes;
	@FXML private CheckBox enableProportions;
	@FXML NumberField proportionField;
	@FXML NumberField resolutionField;
	
	protected DoubleProperty proportionProperty;
	Panner panner = new Panner();
	
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
              
        xSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				panner.setXSpeed(newValue.doubleValue());
			}
        });
        ySlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				panner.setYSpeed(newValue.doubleValue());
			}
        });
		new Thread(panner.task).start();
	}
	
	public void setImage(Image image) {
		preview.setImage(image);
	}
	

	private class Panner {
		
		private double xSpeed = 0;
		private double ySpeed = 0;
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				while(true) {
					try {
						Thread.sleep(10);
						if(xSpeed != 0 || ySpeed != 0) {
							Platform.runLater(() -> {
								System.out.println();
								xField.setValue(xField.getValue()
				                		+ (Math.cos(Math.toRadians(angleField.getValue())) 
				                				* xSpeed - Math.sin(Math.toRadians(angleField.getValue())) 
				                				* ySpeed) / zoomField.getValue());
								yField.setValue(yField.getValue()
										+ (Math.sin(Math.toRadians(angleField.getValue())) 
				                				* xSpeed + Math.cos(Math.toRadians(angleField.getValue())) 
				                				* ySpeed) / zoomField.getValue());
							});
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		private void setXSpeed(double speed) {
			this.xSpeed = speed/50;
		}
		
		private void setYSpeed(double speed) {
			this.ySpeed = speed/50;
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
		double angle = Math.toRadians(angleField.getValue());
		xField.setValue(xField.getValue() 
				+ Math.cos(angle) * scale * (lastCors[0] - event.getX()/preview.getFitHeight())
				- Math.sin(angle) * scale * (event.getY()/preview.getFitHeight() - lastCors[1]));
		yField.setValue(yField.getValue() 
				+ Math.sin(angle) * scale * (lastCors[0] - event.getX()/preview.getFitHeight())
				+ Math.cos(angle) * scale * (event.getY()/preview.getFitHeight() - lastCors[1]));
		lastCors[0] = event.getX()/preview.getFitHeight();
		lastCors[1] = event.getY()/preview.getFitWidth();
	}
	
	@FXML public void zoom(ScrollEvent event) {
    	double scale = 1.1;
    	double angle = Math.toRadians(angleField.getValue());
    	if(event.getDeltaY() > 0) {
    		scale = 1/scale;
    	}
    	
    	xField.setValue(xField.getValue() 
    			+ Math.cos(angle) * scale * (1 - scale) * (event.getX()/preview.getFitWidth() - 0.5) / zoomField.getValue()
    			- Math.sin(angle) * scale * (1 - scale) * (0.5 - event.getY()/preview.getFitHeight()) / zoomField.getValue());
    	yField.setValue(yField.getValue() 
    			+ Math.cos(angle) * scale * (1 - scale) * (0.5 - event.getY()/preview.getFitHeight()) / zoomField.getValue()
    			+ Math.sin(angle) * scale * (1 - scale) * (event.getX()/preview.getFitWidth() - 0.5) / zoomField.getValue());
		zoomField.setValue(zoomField.getValue() / scale);
	}

	@FXML public void fullScreen() {
		System.out.println("Open full screen.");
	}
}