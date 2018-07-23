package com.thefractory.fractalart;

import javafx.scene.image.Image;

public class SimpleMandelbrotSet extends AbstractMandelbrotSet {

	public static String defaultName = "Simple mandelbrot Set";
	public static String description = "simple Mandelbrot Set Super description.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotSecondImage.tif");
	
	public SimpleMandelbrotSet() {
		super("SimpleMandelbrotSet.fxml");
		name = defaultName;
		this.setText(name);
	}
}
