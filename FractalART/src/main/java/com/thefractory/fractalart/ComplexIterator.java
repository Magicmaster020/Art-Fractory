package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.Gradient;
import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import com.thefractory.fractalart.utils.EnhancedCallable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class ComplexIterator extends Artwork {

	public static String defaultName = "Complex Iterator";
	public static String description = "The Complex Iterator Fractals make a collection of very famous fractals. "
			+ "Among these, the most famous is the Mandelbrot Set. However, many other fractals based on the "
			+ "same ideas have also been explored.";
	
	protected IntegerProperty iterations = new SimpleIntegerProperty(50);
	protected DoubleProperty realStart = new SimpleDoubleProperty(0.0);
	protected DoubleProperty imaginaryStart = new SimpleDoubleProperty(0.0);
	protected DoubleProperty divergeSize = new SimpleDoubleProperty(2.0);
	protected double hej = 50;

	@FXML protected StackPane controlPanel;
	@FXML protected GradientPicker gradientPicker;
	@FXML protected NumberSlider iterationSlider;
	@FXML protected NumberSlider realStartSlider;
	@FXML protected NumberSlider imaginaryStartSlider;
	@FXML protected NumberSlider divergeSizeSlider;
	
	protected ComplexIterator(String fxml) {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        try {
            iterations.bind(iterationSlider.valueProperty());
            realStart.bind(realStartSlider.valueProperty());
            imaginaryStart.bind(imaginaryStartSlider.valueProperty());
            divergeSize.bind(divergeSizeSlider.valueProperty());
        } catch(NullPointerException e) {
        	System.out.println("No sliders.");
        }
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		setGradientPicker(gradientPicker);

		iterations.addListener(updateListener);
		realStart.addListener(updateListener);
		imaginaryStart.addListener(updateListener);
		divergeSize.addListener(updateListener);
		
		init();
	}
	
	protected ComplexIterator() {
		this("ComplexIterator.fxml");
	}
	
	protected abstract int iterate(ComplexNumber c);
		
	@Override
	public WritableImage getImage(int width, int height, EnhancedCallable<WritableImage> task) {
		
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
				

				if(task != null) {
					if(Thread.currentThread().isInterrupted()) {
						return null;
					}
					task.setProgress((double) (i * height + j + 1) / (height * width));
				}
			}
		}
		
		Gradient gradient = gradientPicker.getPalette();
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				Color color = gradient.getColor((iterationsAt[i][j] - minIterations)/(hej - minIterations));
				writer.setColor(i, j, color);
			}
		}
		
		hej = minIterations + iterations.get();
		
		return image;
	}
	
	@Override
	public void init() {
		rightPane.setDefaults(0, 0, 0, 0.25);
		gradientPicker.paletteProperty().addListener(updateListener);
		rightPane.reset();
	}
	
	protected void setGradientPicker(GradientPicker gradientPicker) {
		this.gradientPicker = gradientPicker;
	}
	
	@FXML public void update() {
		updateLowResImage();
	}
}
