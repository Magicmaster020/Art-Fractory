package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class Buffalo extends ComplexIterator {

	public static String defaultName = "Buffalo Fractal";
	public static String description = "The Buffalo Fractal is a variation on the classic Mandelbrot Set. "
			+ "The idea is the same but it uses a different iterative equation.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/BuffaloFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/BuffaloSecondImage.tif");
	
	protected IntegerProperty power = new SimpleIntegerProperty(2);
	@FXML private NumberSlider powerSlider;

	public Buffalo() {
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
			z = ComplexNumber.add(ComplexNumber.add(ComplexNumber.pow(
					new ComplexNumber(Math.abs(z.getRe()), Math.abs(z.getIm())), power.get()), 
					new ComplexNumber(-Math.abs(z.getRe()), -Math.abs(z.getIm()))), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
}
