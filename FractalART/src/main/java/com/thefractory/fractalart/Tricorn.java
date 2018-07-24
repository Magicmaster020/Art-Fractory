package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class Tricorn extends ComplexIterator {

	public static String defaultName = "Tricorn Fractal";
	public static String description = "The Tricorn Fractal is a variation of the "
			+ "Mandelbrot Set that makes use of conjugation in each step.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/TricornFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/TricornSecondImage.tif");
	
	protected IntegerProperty power = new SimpleIntegerProperty(2);
	@FXML private NumberSlider powerSlider;

	public Tricorn() {
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
			z = ComplexNumber.add(ComplexNumber.pow(z.conjugate(), power.get()), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
}
