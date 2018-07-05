package com.thefractory.fractalart;
import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.fractalart.utils.ComplexNumber;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class AbstractMandelbrotSet extends Artwork {

	protected IntegerProperty iterations = new SimpleIntegerProperty(50);
	protected IntegerProperty power = new SimpleIntegerProperty(2);
	protected DoubleProperty realStart = new SimpleDoubleProperty(0.0);
	protected DoubleProperty imaginaryStart = new SimpleDoubleProperty(0.0);
	private GradientPicker gradientPicker;
	
	public AbstractMandelbrotSet(String name) {
		super(name);
		iterations.addListener(updateListener);
		power.addListener(updateListener);
		realStart.addListener(updateListener);
		imaginaryStart.addListener(updateListener);
	}
	
	private int iterate(ComplexNumber c) {
		ComplexNumber z = new ComplexNumber(realStart.get() , imaginaryStart.get());
		int i = 0;
		for(; i < iterations.get(); i++) {
			z = ComplexNumber.add(ComplexNumber.pow(z, power.get()), c);
			if(Math.pow(z.getRe(), 2) + Math.pow(z.getIm(), 2) > 4) {
				break;
			}
		}
		return i;
	}
	@Override
    public void init() {
		gradientPicker.paletteProperty().addListener(updateListener);
		updateImage((int) rightPane.resolutionField.getValue());
		setImage();
	}
	
	@Override
	public WritableImage getImage(int height, int width) {
		double xLength = 1/rightPane.zoomField.getValue();
		double[] center = {rightPane.xField.getValue(), rightPane.yField.getValue()};
		double angle = -Math.toRadians(rightPane.angleField.getValue());
		
		WritableImage image = new WritableImage(height, width);
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				
				ComplexNumber z = new ComplexNumber(
						center[0] + xLength * (-Math.cos(angle) * (0.5 - ((double) i)/width) 
								+ Math.sin(angle) * (0.5 - ((double) j)/width)), 
						- center[1] + xLength * (-Math.sin(angle) * (0.5 - ((double) i)/width) 
								- Math.cos(angle) * (0.5 - ((double) j)/width)));
				
				Color color = gradientPicker.getPalette().getColor(((double) iterate(z))/iterations.get());
				writer.setColor(i, j, color);
			}
		}
		return(image);
	}
	
	protected void setGradientPicker(GradientPicker gradientPicker) {
		this.gradientPicker = gradientPicker;
	}
}
