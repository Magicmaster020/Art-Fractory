package com.thefractory.fractalart;

public class SimpleMandelbrotSet extends AbstractMandelbrotSet {
	
	public SimpleMandelbrotSet() {
		super("SimpleMandelbrotSet.fxml");
		DEFAULT_NAME = "Simple Mandelbrot Set";
		name = DEFAULT_NAME;
		this.setText(name);
	}
}
