package com.thefractory.fractalart;

import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MandelbrotSet extends AbstractMandelbrotSet {
	
	private static String DEFAULT_NAME= "Mandelbrot Set";	
	
	@FXML private StackPane controlPanel;
	@FXML private NumberSlider iterationSlider;
	@FXML private NumberSlider powerSlider;

	public MandelbrotSet() {
		super(DEFAULT_NAME);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MandelbrotSet.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
		
		addMainPane(controlPanel);
	}
	
	public void updateImage() {
//		image = makeMandelbrot();
	}
//	
//	public BufferedImage makeMandelbrot(){
//		int iter = iterations.getValue(); 
//		
//		double[] scale = {size[0]/dimensions[0], size[1]/dimensions[1]};
//		double[] topLeft = {center[0] - size[0]/2, center[1] + size[1]/2};
//		
//		BufferedImage img = new BufferedImage(dimensions[0], dimensions[1], BufferedImage.TYPE_INT_RGB);
//				
//		double m;
//		for(int x = 0; x < dimensions[0]; x++) {
//			for(int y = 0; y < dimensions[1]; y++){
//				ComplexNumber c = new ComplexNumber(topLeft[0] + x * scale[0], topLeft[1] - y * scale[1]);
//				int n = iterate(c, new ComplexNumber(), iterateMax, power);
//				
//				if(n == -1) {
//					m = 1;
//				} else {
//					m = (float) n/iterateMax;
//				}
//				
//				int col = (palette[Math.max(0, (int) (m * (palette.length-1)))][0] << 16)
//						| (palette[Math.max(0, (int) (m * (palette.length-1)))][1] << 8)
//						| palette[Math.max(0, (int) (m * (palette.length-1)))][2];
//				img.setRGB(x, y, col);
//			}
//		}		
//		return img;
//	}

	//TODO SHOULD BE MOVED TO A "COMPLEX ITERATOR" CLASS
	public static int iterate(ComplexNumber c, ComplexNumber z, int iterateMax, int power) {
		for(int i = 0; i < iterateMax; i++) {
			z = ComplexNumber.add(ComplexNumber.pow(z, power), c);
			if(z.mod() > 2) {
				return i;
			}
		}
		return -1; //Previously None
	}
	
}
