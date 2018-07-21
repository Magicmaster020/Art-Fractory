package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;

public class Tricorn extends ComplexIterator {

	protected IntegerProperty power = new SimpleIntegerProperty(2);
	@FXML private NumberSlider powerSlider;

	public Tricorn() {
		super("ComplexIteratorPower.fxml");
		DEFAULT_NAME = "Tricorn Fractal";
		name = DEFAULT_NAME;
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
