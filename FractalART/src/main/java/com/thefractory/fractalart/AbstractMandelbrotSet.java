package com.thefractory.fractalart;
import com.thefractory.customcomponents.Gradient;
import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.fractalart.utils.ComplexNumber;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class AbstractMandelbrotSet extends Artwork {

	protected int iterations = 50;
	protected int power = 2;
	protected ComplexNumber seed = new ComplexNumber(0,0);
	private GradientPicker gradientPicker; 
	
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
    public void init() {
		gradientPicker.paletteProperty().addListener(new ChangeListener<Gradient>() {
			@Override
			public void changed(ObservableValue<? extends Gradient> arg0, Gradient arg1, Gradient arg2) {
				updateImage((int) rightPane.resolutionField.getValue());
			}
			
		});
		updateImage((int) rightPane.resolutionField.getValue());
		setImage();
	}
	
	//this function is now concrete in artwork
	//@Override
	//public void updateImage(int resolution) {
	//	setImage(getImage(resolution, resolution));
	//	setImage();
	//}
	
	
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
				
				Color color = gradientPicker.getPalette().getColor(((double) iterate(z))/iterations);
				writer.setColor(i, j, color);
			}
		}
		return(image);
	}
	
	protected void setGradientPicker(GradientPicker gradientPicker) {
		this.gradientPicker = gradientPicker;
	}
}
