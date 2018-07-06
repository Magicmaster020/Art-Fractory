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
	protected double hej = 50;
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
		for(; i < hej; i++) {
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
		rightPane.setDefaults(0, 0, 0, 0.25);
		rightPane.reset();
		updateImage((int) rightPane.resolutionField.getValue());
		setImage();
	}
	
	@Override
	public WritableImage getImage(int width, int height) {
		double xLength = 1/rightPane.zoomProperty.getValue();
		double[] center = {rightPane.xField.getValue(), rightPane.yField.getValue()};
		double angle = -Math.toRadians(rightPane.angleField.getValue());
		
		double minIterations = hej;
		double[][] iterationsAt = new double[width][height];
		WritableImage image = new WritableImage(width, height);
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				
				ComplexNumber z = new ComplexNumber(
						center[0] + xLength * (-Math.cos(angle) * (((0.5 * width) - ((double) i))/width) 
								+ Math.sin(angle) * (((0.5 * height) - ((double) j))/width)), 
						- center[1] + xLength * (-Math.sin(angle) * (((0.5 * width) - ((double) i))/width) 
								- Math.cos(angle) * (((0.5 * height) - ((double) j))/width)));
				double tempIt = (double) iterate(z);
				if (tempIt < minIterations){
					minIterations = tempIt;
				}
				iterationsAt[i][j] = tempIt;
			}
		}
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				Color color = gradientPicker.getPalette().getColor((iterationsAt[i][j] - minIterations)/(hej - minIterations));
				writer.setColor(i, j, color);
			}
		}
		
		hej = minIterations + iterations.get();
		return(image);
	}
	
	protected void setGradientPicker(GradientPicker gradientPicker) {
		this.gradientPicker = gradientPicker;
	}
}
