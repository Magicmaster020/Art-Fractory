package com.thefractory.fractalart;

import com.thefractory.customcomponents.Gradient;
import com.thefractory.fractalart.utils.ComplexNumber;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class AbstractMandelbrotSet extends Artwork {

	protected int iterations = 50;
	protected int power = 2;
	protected ComplexNumber seed = new ComplexNumber(0,0);
	
	
	public AbstractMandelbrotSet(String name) {
		super(name);
	}
	
	private int iterate(ComplexNumber c) {
		ComplexNumber z = seed;
		int i = 0;
		for(; i < iterations; i++) {
			z = ComplexNumber.add(ComplexNumber.pow(z, power), c);
			if(Math.pow(z.getRe(), 2) + Math.pow(z.getIm(), 2) > 4) {
				break;
			}
		}
		return i;
	}
	
	@Override
	public void updateImage(int resolution) {
		double xLength = 1/rightPane.zoomField.getValue();
		double[] center = {rightPane.xField.getValue(), rightPane.yField.getValue()};
		double angle = -Math.toRadians(rightPane.angleField.getValue());
		
		image = new WritableImage(resolution, resolution);
		PixelWriter writer = image.getPixelWriter();
		
		for(int i = 0; i < resolution; i++) {
			for(int j = 0; j < resolution; j++) {
				
				ComplexNumber z = new ComplexNumber(
						center[0] + xLength * (-Math.cos(angle) * (0.5 - ((double) i)/resolution) 
								+ Math.sin(angle) * (0.5 - ((double) j)/resolution)), 
						- center[1] + xLength * (-Math.sin(angle) * (0.5 - ((double) i)/resolution) 
								- Math.cos(angle) * (0.5 - ((double) j)/resolution)));
				
				Color color = gradient.getColor(((double) iterate(z))/iterations);
				
				writer.setColor(i, j, color);
			}
		}
		setImage();
	}
}
