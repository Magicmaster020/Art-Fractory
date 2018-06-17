package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.InfoIcon;
import com.thefractory.customcomponents.NumberField;
import com.thefractory.customcomponents.SpringSlider;

import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
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
	@FXML private NumberField proportionsField;
	
	private DoubleProperty proportionsProperty;
	Panner xPanner;
	Panner yPanner;
	
	
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
        
        proportionsProperty = new SimpleDoubleProperty(proportionsField.getValue());
        enableProportions.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					proportionsProperty.bind(proportionsField.valueProperty());
					proportionsField.setDisable(false);
					proportionDraggerTop.setVisible(true);
					proportionDraggerBottom.setVisible(true);
				} else {
					proportionsProperty.unbind();
					proportionsProperty.setValue(1);
					proportionsField.setDisable(true);
					proportionDraggerTop.setVisible(false);
					proportionDraggerBottom.setVisible(false);
				}
			}
        });

        preview.fitWidthProperty().bind(this.widthProperty());
        preview.fitHeightProperty().bind(this.widthProperty());
        previewPane.maxHeightProperty().bind(this.widthProperty());
        shadeTop.prefHeightProperty().bind(
        		previewPane.heightProperty().multiply(proportionsProperty.subtract(1)).multiply(-0.5));
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
}