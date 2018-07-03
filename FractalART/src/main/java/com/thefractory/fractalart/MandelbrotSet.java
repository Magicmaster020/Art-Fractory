package com.thefractory.fractalart;

import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MandelbrotSet extends AbstractMandelbrotSet {
	
	private static String DEFAULT_NAME= "Mandelbrot Set";	
	
	@FXML private StackPane controlPanel;
	@FXML private GradientPicker gradientPicker;
	@FXML private NumberSlider iterationSlider;
	@FXML private NumberSlider powerSlider;
	@FXML private NumberSlider realStartSlider;
	@FXML private NumberSlider imaginaryStartSlider;

	public MandelbrotSet() {
		super(DEFAULT_NAME);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MandelbrotSet.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        iterations.bind(iterationSlider.valueProperty());
        power.bind(powerSlider.valueProperty());
        realStart.bind(realStartSlider.valueProperty());
        imaginaryStart.bind(imaginaryStartSlider.valueProperty());
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		setGradientPicker(gradientPicker);
		
		init();
		
	}
	
	@FXML public void update() {
		updateImage((int) rightPane.resolutionField.getValue());
	}
	
	@FXML public void export() {
		Model.getInstance().exportAs(this);
	}
}
