package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class MandelbrotSet extends AbstractMandelbrotSet {

	public static String defaultName = "Mandelbrot Set";
	public static String description = "The Mandelbrot set is perhaps the most famous fractal "
			+ "and certainly the most famous Complex Iterator Fractal. It has been studied by "
			+ "many mathematians who have found endless amounts of beautiful patterns in it.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotSecondImage.tif");
	
	@FXML private NumberSlider powerSlider;

	public MandelbrotSet() {
		super("ComplexIteratorPower.fxml");
		name = defaultName;
		this.setText(name);
		
        power.bind(powerSlider.valueProperty());
	}
}
