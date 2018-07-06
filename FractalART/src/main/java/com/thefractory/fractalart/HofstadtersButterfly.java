package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.Gradient;
import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HofstadtersButterfly extends Artwork{
	
	private static String DEFAULT_NAME = "Simple Hofstadters Butterfly";	
	
	@FXML private StackPane controlPanel;
	@FXML private NumberField latticePoints;
	@FXML protected GradientPicker gradientPicker;
	
	
	public HofstadtersButterfly() {
		super(DEFAULT_NAME);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HofstadtersButterfly.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		controlPanel.getStylesheets().add(getClass().getResource("gradient.css").toString());

		init();
	}
	
	@Override
	public WritableImage getImage(int height, int length) {
		int q = (int)latticePoints.getValue();
		//int ResFactor = (int)(resolution / q);
		//resolution = resolution - resolution % q;
		double xLength = 1/rightPane.zoomProperty.getValue();
		double yLength = 8*xLength;
		double[] center = {rightPane.xField.getValue(), - rightPane.yField.getValue()*8};
		double angle = -Math.toRadians(rightPane.angleField.getValue());
		double[][] pixels = new double[height][length];
		double maxValue = 5;
		double minValue = 3;
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < length; j++) {
				double alpha = center[0] + xLength * (-Math.cos(angle) * (0.5 - ((double) i)/length) 
						+ Math.sin(angle) * (0.5 - ((double) j)/length)); 
				double epsilon = center[1] + yLength * (-Math.sin(angle) * (0.5 - ((double) i)/length) 
						- Math.cos(angle) * (0.5 - ((double) j)/length));
				
				double[] xLatticePoint = new double[q]; //The physical lattice points of the electron. Not points in the picture
				
				xLatticePoint[0] = 2*Math.cos(2*Math.PI*alpha) - epsilon;
				double detXLatticePoints = xLatticePoint[0];
				for(int k = 1; k < q; k++) {
					xLatticePoint[k] = 2*Math.cos(2*Math.PI*(k+1)*alpha) - epsilon - 1 / xLatticePoint[k-1];
					detXLatticePoints = detXLatticePoints*xLatticePoint[k];
				}
				//System.out.println();
				pixels[i][j] = Math.log(Math.abs(detXLatticePoints));
				if(pixels[i][j] == Double.POSITIVE_INFINITY) {
					pixels[i][j] = 710;
				}else if(pixels[i][j] == Double.NEGATIVE_INFINITY) {
					pixels[i][j] = -710;
				}
				if (maxValue < pixels[i][j])
					maxValue = pixels[i][j];
				else if(minValue > pixels[i][j])
					minValue = pixels[i][j];
			}
		}
		System.out.println(minValue);
		System.out.println(maxValue);
		//creating the picture
		WritableImage image = new WritableImage(height, length);
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < length; j++) {
				Color color = gradientPicker.getPalette().getColor((pixels[i][j] - minValue) / (maxValue - minValue));
				writer.setColor(i, j, color);
			}
		}
		
		
		// TODO Auto-generated method stub
		return image;
	}
	
	@Override
    public void init() {
		updateImage((int) rightPane.resolutionField.getValue());
		rightPane.setDefaults(0.5, 0, 0, 1);
		rightPane.reset();
		setImage();
	}
	
	
	@FXML public void update() {
		updateImage((int) rightPane.resolutionField.getValue());
	}
	
	@FXML public void changeNumberOfLatP(KeyEvent event) {
		update();
	}

	
}
