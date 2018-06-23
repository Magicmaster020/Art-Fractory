package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.Gradient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SimpleMandelbrotSet extends AbstractMandelbrotSet {

	private static String DEFAULT_NAME = "Simple Mandelbrot Set";	

	@FXML private StackPane controlPanel;
	@FXML private VBox gradients;
	
	public SimpleMandelbrotSet() {
		super(DEFAULT_NAME);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SimpleMandelbrotSet.fxml"));
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
	
	@FXML public void update() {
		updateImage((int) rightPane.resolutionField.getValue());
	}

	@FXML public void changeGradient(MouseEvent event) {
		gradient = new Gradient(((Gradient) event.getSource()).getGradientLocation());
		update();
	}
}
