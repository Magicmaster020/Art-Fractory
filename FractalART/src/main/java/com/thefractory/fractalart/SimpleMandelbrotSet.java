package com.thefractory.fractalart;

import com.thefractory.customcomponents.GradientPicker;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class SimpleMandelbrotSet extends AbstractMandelbrotSet {
	
	@FXML private StackPane controlPanel;
	@FXML private GradientPicker gradientPicker;
	
	public SimpleMandelbrotSet() {
		super();
		DEFAULT_NAME = "Simple Mandelbrot Set";
		name = DEFAULT_NAME;
		this.setText(name);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SimpleMandelbrotSet.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
		
		setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		setGradientPicker(gradientPicker);
		
		init();
	}
	
	@FXML public void update() {
		updateLowResImage();
	}

}
