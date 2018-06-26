package com.thefractory.customcomponents;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * JavaFX component that lets the user easily create a custom gradient.
 * @author ivarc
 *
 */
public class GradientPicker extends VBox {
	
	/**
	 * The VBox containing all gradientMakers.
	 */
	@FXML private VBox gradientBox;
	@FXML private IconButton addGradient; 
	@FXML private Gradient gradient; 
	
	/**
	 * Keeps track of the open {@GradientMakers}.
	 */
	private ArrayList<ExtendedGradientMaker> gradientMakers = new ArrayList<ExtendedGradientMaker>();
	
	/**
	 * The current combined gradient.
	 */
	private ObjectProperty<ArrayList<Color>> colors = new SimpleObjectProperty<ArrayList<Color>>();
	
	/**
	 * Creates a default {@code GradientPicker}
	 */
	public GradientPicker() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradientPicker.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        addGradient(new ExtendedGradientMaker());
        gradient.colors.bind(colors);
	}
	
	private class ExtendedGradientMaker {
		
		private GradientMaker gradientMaker;
		private HBox box = new HBox();
		//private DragIcon drag;
		private IconButton remove = new IconButton("close");
		
		private ExtendedGradientMaker() {
			this.gradientMaker = new GradientMaker();
			remove.setOnAction(event -> {removeGradient(this);});
			box.getChildren().addAll(gradientMaker, remove);
			addListener();
		}
		
		private ExtendedGradientMaker(ExtendedGradientMaker extendedGradientMaker) {
			this.gradientMaker = new GradientMaker(extendedGradientMaker.gradientMaker);
			remove.setOnAction(event -> {removeGradient(this);});
			box.getChildren().addAll(gradientMaker, remove);
			addListener();
		}
		
		private void addListener() {
			gradientMaker.gradientProperty().addListener(new ChangeListener<Object>() {
	            public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
	                makeColors();
	            }
	        });
		}
	}
	
	private void addGradient(ExtendedGradientMaker extendedGradientMaker) {
		gradientMakers.add(extendedGradientMaker);
		gradientBox.getChildren().add(extendedGradientMaker.box);
		makeColors();
		if(gradientMakers.size() == 1) {
			gradientMakers.get(0).remove.setDisable(true);
		} else if(gradientMakers.size() == 2) {
			gradientMakers.get(0).remove.setDisable(false);
		}
	}
	
	/**
	 * Adds a new {@code ExtendedGradientMaker} on button press.
	 */
	@FXML void addNewGradient(){
		addGradient(new ExtendedGradientMaker(gradientMakers.get(gradientMakers.size() - 1)));
		
	}
	
	private void removeGradient(ExtendedGradientMaker extendedGradientMaker) {
		gradientMakers.remove(extendedGradientMaker);
		gradientBox.getChildren().remove(extendedGradientMaker.box);
		makeColors();
		if(gradientMakers.size() == 1) {
			gradientMakers.get(0).remove.setDisable(true);
		}
	}
	
	/**
	 * Makes a new {@code Color} list for the gradient.
	 */
	public void makeColors(){
		ArrayList<Color> palette = new ArrayList<Color>();
		for(ExtendedGradientMaker extendedGradientMaker : gradientMakers) {
			palette.addAll(extendedGradientMaker.gradientMaker.getGradient());
		}
		colors.set(palette);
	}
}
