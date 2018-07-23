package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class BurningShip extends ComplexIterator {

	public static String defaultName = "Burning Ship Fractal";
	public static String description = "Burning Ship Super description.";
	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/BurningShipFirstImage.tif");
	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/BurningShipSecondImage.tif");
	
	protected IntegerProperty power = new SimpleIntegerProperty(2);
	@FXML private NumberSlider powerSlider;

	public BurningShip() {
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
			z = ComplexNumber.add(ComplexNumber.pow(new ComplexNumber(Math.abs(z.getRe()), Math.abs(z.getIm())), power.get()), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
}
