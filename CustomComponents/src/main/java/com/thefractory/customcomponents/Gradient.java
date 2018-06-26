package com.thefractory.customcomponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Shows a gradient read from a text-file in 
 * resources>com>thefractory>customcomponents>gradients. The text file must
 * specify the colours line by line and with rgb-values between 0 and 1. 
 * Each value should be separated by spaces. 
 * 
 * Alternatively the gradient can be set after initialisation.
 * @author Ivar Eriksson
 *
 */
public class Gradient extends ImageView {

	/**
	 * The gradient property.
	 */
	public final ObjectProperty<ArrayList<Color>> colors;
	
	/**
	 * The location of the text file containing the gradient.
	 */
    private final StringProperty gradientLocation;
    /**
     * The {@code Image} on which the gradient is drawn.
     */
	private WritableImage image;
	
	/**
	 * The {@code ImageView} displaying the gradient.
	 */
	@FXML public ImageView gradient;
	
	/**
	 * Returns the gradient based on the location given.
	 * @param gradientLocation
	 */
	public Gradient(@NamedArg(value="gradientLocation", defaultValue="pink") String gradientLocation){
		this.gradientLocation = new SimpleStringProperty(this, "gradientLocation", gradientLocation);
		this.colors = new SimpleObjectProperty<ArrayList<Color>>();
		
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Gradient.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(
		        this.getClass().getResourceAsStream("gradients/" + gradientLocation + ".txt"))));
        colors.set(new ArrayList<Color>());
        while(sc.hasNextLine()) {
        	colors.getValue().add(Color.BLACK);
            String[] line = sc.nextLine().trim().split(" ");
            colors.getValue().set(colors.getValue().size() - 1, Color.color(Double.parseDouble(line[0]),
            		Double.parseDouble(line[1]),
            		Double.parseDouble(line[2])));
        }
        sc.close();
                
        this.image = new WritableImage(500, 30);
        PixelWriter writer = image.getPixelWriter();
        for(int x = 0; x < image.getWidth(); x++) {
        	for(int y = 0; y < image.getHeight(); y++) {
        		Color color = colors.getValue().get(
        				(int) Math.round(x * (colors.getValue().size() - 1)/image.getWidth()));
            	writer.setColor(x, y, color);
        	}
        }
        gradient.setImage(image);
	}
	
	/**
	 * Creates an empty {@code Gradient}.
	 */
	public Gradient() {
		this.gradientLocation = new SimpleStringProperty(this, null, null);
		this.colors = new SimpleObjectProperty<ArrayList<Color>>();
		
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Gradient.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        colors.addListener(new ChangeListener<ArrayList<Color>>() {
			@Override
			public void changed(ObservableValue<? extends ArrayList<Color>> arg0, 
					ArrayList<Color> oldValue, ArrayList<Color> newValue) {
				update();
			}
		});
	}
	
	/**
	 * Returns the colour at the specified location in the gradient.
	 * @param index Takes values between 0 and 1.
	 * @return
	 */
	public Color getColor(double index) {
		return colors.getValue().get((int) Math.round(index * (colors.getValue().size() - 1))); 
	}
	
	/**
	 * Updates the shown gradient.
	 * Is called when {@code colours} changes.
	 */
	public void update() {
		System.out.println(colors.getValue().size());
		if(colors.getValue().size() != 0) {
			this.image = new WritableImage(500, 30);
	        PixelWriter writer = image.getPixelWriter();
	        for(int x = 0; x < image.getWidth(); x++) {
	        	for(int y = 0; y < image.getHeight(); y++) {
	        		Color color = colors.getValue().get(
	        				(int) Math.round(x * (colors.getValue().size() - 1)/image.getWidth()));
	            	writer.setColor(x, y, color);
	        	}
	        }
	        gradient.setImage(image);
		}
	}
	
	public final String getGradientLocation(){
		return gradientLocation.getValue();
	}
	public final void setGradientLocation(String value){
		gradientLocation.setValue(value);
	}
	public final StringProperty gradientLocationProperty(){
		return gradientLocation;
	}
}
