package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import javafx.fxml.FXML;

public class MandelbrotSet extends AbstractMandelbrotSet {
	
	@FXML private NumberSlider powerSlider;

	public MandelbrotSet() {
		super("ComplexIteratorPower.fxml");
		DEFAULT_NAME = "Mandelbrot Set";
		name = DEFAULT_NAME;
		this.setText(name);
		
        power.bind(powerSlider.valueProperty());
	}
}
