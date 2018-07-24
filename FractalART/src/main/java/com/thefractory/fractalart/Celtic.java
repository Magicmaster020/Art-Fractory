package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class Celtic extends ComplexIterator {

	public static String defaultName = "Celtic Fractal";
	public static String description = "The Celtic Fractal is a variation on the classic Mandelbrot Set. "
			+ "The idea is the same but it uses a different iterative equation. It gets its name from the "
			+ "occurance of patterns that resemble traditional celtic shapes.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/CelticFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/CelticSecondImage.tif");
	
	protected IntegerProperty power = new SimpleIntegerProperty(2);
	@FXML private NumberSlider powerSlider;

	public Celtic() {
		super("ComplexIteratorPower.fxml");
		name = defaultName;
		this.setText(name);
		
        power.bind(powerSlider.valueProperty());
		power.addListener(updateListener);
	}
	
	@Override
	protected int iterate(ComplexNumber c) {
		ComplexNumber z = new ComplexNumber(realStart.get(), imaginaryStart.get());
		int i = 0;
		for(; i < hej; i++) {
			ComplexNumber temp = ComplexNumber.pow(z, power.get()); 
			z = ComplexNumber.add(new ComplexNumber(Math.abs(temp.getRe()), temp.getIm()), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
}
