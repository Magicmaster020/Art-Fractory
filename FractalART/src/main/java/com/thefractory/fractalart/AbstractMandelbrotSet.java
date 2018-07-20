package com.thefractory.fractalart;

import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class AbstractMandelbrotSet extends ComplexIterator {

	protected IntegerProperty power = new SimpleIntegerProperty(2);
	protected DoubleProperty realStart = new SimpleDoubleProperty(0.0);
	protected DoubleProperty imaginaryStart = new SimpleDoubleProperty(0.0);
	
	public AbstractMandelbrotSet() {
		super();
		iterations.addListener(updateListener);
		power.addListener(updateListener);
		realStart.addListener(updateListener);
		imaginaryStart.addListener(updateListener);
		divergeSize.addListener(updateListener);
	}
	
	@Override
	protected int iterate(ComplexNumber c) {
		ComplexNumber z = new ComplexNumber(realStart.get() , imaginaryStart.get());
		int i = 0;
		for(; i < hej; i++) {
			z = ComplexNumber.add(ComplexNumber.pow(z, power.get()), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
	
	@Override
    public void init() {
		rightPane.setDefaults(0, 0, 0, 0.25);
		super.init();
	}
}
