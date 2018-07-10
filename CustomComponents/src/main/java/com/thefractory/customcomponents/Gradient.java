package com.thefractory.customcomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Shows a gradient from a list of {@code GradientMaker}s.
 * Alternatively the gradient can be set after initialisation.
 * @author Ivar Eriksson
 *
 */
public class Gradient extends ImageView {

	/**
	 * The gradient property.
	 */
	private final ObjectProperty<ArrayList<Color>> palette;
	/**
	 * The set of {@code GradientMaker}s that generated this {@code Gradient}.
	 */
	ObservableList<GradientMaker> gradientMakerList;   
	/**     
	 * The {@code Image} in which the gradient is drawn.
     */
	private WritableImage image;

	/**
	 * The {@code ImageView} displaying the gradient.
	 */
	@FXML private ImageView gradient;
	
	/**
	 * Makes a {@code Gradient} based on the given list of {@code GradientMaker}s.
	 * @param gradientMakers
	 */
	public Gradient(ArrayList<GradientMaker> gradientMakers){
		this.palette = new SimpleObjectProperty<ArrayList<Color>>();
		this.gradientMakerList = FXCollections.observableArrayList(gradientMakers); 
        setup();
	}
	
	/**
	 * Creates an empty {@code Gradient}.
	 */
	public Gradient() {
		this.palette = new SimpleObjectProperty<ArrayList<Color>>();
		this.gradientMakerList = FXCollections.observableArrayList(); 
        setup();
	}
	
	/**
	 * Fixes some general setup for the constructors.
	 */
	private void setup() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Gradient.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

		this.image = new WritableImage(1000, 30);
        palette.addListener(new ChangeListener<ArrayList<Color>>() {
			@Override
			public void changed(ObservableValue<? extends ArrayList<Color>> arg0, 
					ArrayList<Color> oldValue, ArrayList<Color> newValue) {
				updateGradient();
			}
		});
        gradientMakerList.addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change arg0) {
               updatePalette();
			}
        });
        if(gradientMakerList.size() != 0) {
            updatePalette();
        }
	}
	
	/**
	 * Draws the gradient from the current palatte.
	 */
	public void updateGradient() {
		if(palette.getValue().size() != 0) {
	        PixelWriter writer = image.getPixelWriter();
	        for(int x = 0; x < image.getWidth(); x++) {
	        	for(int y = 0; y < image.getHeight(); y++) {
	        		Color color = palette.getValue().get(
	        				(int) Math.round(x * (palette.getValue().size() - 1)/image.getWidth()));
	            	writer.setColor(x, y, color);
	        	}
	        }
	        gradient.setImage(image);
		}
	}
	
	/**
	 * Updates the {@code palette based on the current list of {@code GradientMakers}.
	 */
	public void updatePalette() {
		ArrayList<Color> palette = new ArrayList<Color>();
		for(GradientMaker gradientMaker : gradientMakerList) {
			palette.addAll(gradientMaker.getPalette());
		}
		this.palette.set(palette);
	}
	
	/**
	 * Returns the colour at the specified location in the gradient.
	 * @param index Takes values between 0 and 1.
	 * @return
	 */
	public Color getColor(double index) {
		try {
			return palette.getValue().get((int) Math.round(index * (palette.getValue().size() - 1)));
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(index);
			return Color.BLACK;
		}
	}

	/**
	 * Gets the list of {@code GradientMakers}.
	 */
	public ObservableList<GradientMaker> getGradientMakerList(){
		return gradientMakerList;
	}

	public void setGradientMakerList(ArrayList<GradientMaker> gradientMakerList) {
		this.gradientMakerList.clear();
		this.gradientMakerList.addAll(gradientMakerList);
	}
		
	/**
	 * Gets the list of {@code Color}s.
	 * @return
	 */
	public ArrayList<Color> getPalette(){
		return palette.get();
	}

	/**
	 * Returns a clone of this {@code Gradient}.
	 */
	public Gradient clone() {
		ArrayList<GradientMaker> gradientMakers = new ArrayList<GradientMaker>();
		for(GradientMaker gradientMaker : gradientMakerList) {
			gradientMakers.add(gradientMaker);
		}
		return new Gradient(gradientMakers);
	}
}
