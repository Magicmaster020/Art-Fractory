package com.thefractory.fractalart;

import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberSlider;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MandelbrotSet extends AbstractMandelbrotSet {
	
	
	
	@FXML private StackPane controlPanel;
	@FXML private GradientPicker gradientPicker;
	@FXML private NumberSlider iterationSlider;
	@FXML private NumberSlider powerSlider;
	@FXML private NumberSlider realStartSlider;
	@FXML private NumberSlider imaginaryStartSlider;
	@FXML private NumberSlider divergeSizeSlider;

	public MandelbrotSet() {
		super();
		DEFAULT_NAME = "Mandelbrot Set";
		name = DEFAULT_NAME;
		this.setText(name);
		
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
        divergeSize.bind(divergeSizeSlider.valueProperty());
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		setGradientPicker(gradientPicker);
		
		init();
	}
	
	@FXML public void update() {
		updateLowResImage();
	}
}
