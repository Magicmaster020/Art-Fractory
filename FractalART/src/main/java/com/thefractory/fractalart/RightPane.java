package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.InfoIcon;
import com.thefractory.customcomponents.NumberField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class RightPane extends VBox {

	//FXML injected elements.
	@FXML private ImageView preview;
	@FXML private Rectangle shadeTop;
	@FXML private Rectangle shadeBottom;
	@FXML private NumberField xField;
	@FXML private NumberField yField;
	@FXML private NumberField angleField;
	@FXML private NumberField zoomField;
	@FXML private InfoIcon xInfo;
	@FXML private InfoIcon yInfo;
	@FXML private InfoIcon angleInfo;
	@FXML private InfoIcon zoomInfo;
	@FXML private Slider xSlider;
	@FXML private Slider ySlider;
	@FXML private Circle angleWheel;
	@FXML private Button recenter;
	@FXML private CheckBox showAxes;
	@FXML private CheckBox enableProportions;
	@FXML private NumberField proportionsField;
	
	
	public RightPane() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RightPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	@FXML public void recenter() {
		System.out.println("Herro!");
	}
	
	@Override
	public String toString() {
		String str = "";
		
		
		return str;
	}
}
