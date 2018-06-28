package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.Gradient;
import com.thefractory.customcomponents.NumberField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
	@FXML private VBox gradients;
	@FXML private NumberField latticePoints;
	protected Gradient gradient = new Gradient("pink");
	
	
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
        
        addMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		gradients.maxWidthProperty().bind(controlPanel.widthProperty());
		for(Node gradient : gradients.getChildren()) {
			((ImageView) gradient).fitWidthProperty().bind(gradients.maxWidthProperty());
		}

		controlPanel.getStylesheets().add(getClass().getResource("gradient.css").toString());
	}

	@Override
	public void updateImage(int resolution) {
		int q = (int)latticePoints.getValue();
		//int ResFactor = (int)(resolution / q);
		//resolution = resolution - resolution % q;
		double xLength = 1/rightPane.zoomField.getValue();
		double yLength = 8*xLength;
		double[] center = {rightPane.xField.getValue(), - rightPane.yField.getValue()*8};
		double angle = -Math.toRadians(rightPane.angleField.getValue());
		double[][] pixels = new double[resolution][resolution];
		double maxValue = 5;
		double minValue = 3;
		
		for(int i = 0; i < resolution; i++) {
			for(int j = 0; j < resolution; j++) {
				double alpha = center[0] + xLength * (-Math.cos(angle) * (0.5 - ((double) i)/resolution) 
						+ Math.sin(angle) * (0.5 - ((double) j)/resolution)); 
				double epsilon = center[1] + yLength * (-Math.sin(angle) * (0.5 - ((double) i)/resolution) 
						- Math.cos(angle) * (0.5 - ((double) j)/resolution));
				
				double[] xLatticePoint = new double[q]; //The physical lattice points of the electron. Not points in the picture
				
				xLatticePoint[0] = 2*Math.cos(2*Math.PI*alpha) - epsilon;
				double detXLatticePoints = xLatticePoint[0];
				//System.out.println(alpha);
				//System.out.println(epsilon);
				//System.out.println(xLatticePoints[0][0]);
				for(int k = 1; k < q; k++) {
					xLatticePoint[k] = 2*Math.cos(2*Math.PI*(k+1)*alpha) - epsilon - 1 / xLatticePoint[k-1];
					detXLatticePoints = detXLatticePoints*xLatticePoint[k];
					//System.out.println(xLatticePoints[k][k]);
				}
				//System.out.println();
				pixels[i][j] = Math.log(Math.abs(detXLatticePoints));
				if (maxValue < pixels[i][j])
					maxValue = pixels[i][j];
				else if(minValue > pixels[i][j])
					minValue = pixels[i][j];
			}
		}
		System.out.println(minValue);
		System.out.println(maxValue);
		
		//creating the picture
		image = new WritableImage(resolution, resolution);
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < resolution; i++) {
			for(int j = 0; j < resolution; j++) {
				Color color = gradient.getColor((pixels[i][j] - minValue) / (maxValue - minValue));
				writer.setColor(i, j, color);
			}
		}
		setImage();
	}
	
	@FXML public void update() {
		updateImage((int) rightPane.resolutionField.getValue());
	}
	
	@FXML public void changeGradient(MouseEvent event) {
		gradient = new Gradient(((Gradient) event.getSource()).getGradientLocation());
		update();
	}
	
	@FXML public void changeNumberOfLatP(KeyEvent event) {
		update();
	}
}
