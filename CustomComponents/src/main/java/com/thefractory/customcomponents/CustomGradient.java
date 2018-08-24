package com.thefractory.customcomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

public class CustomGradient extends VBox {
	
	ObservableList<ColorArrow> colorArrowList 
			= FXCollections.observableArrayList(new ArrayList<ColorArrow>());
	ObservableList<GradientMaker> gradientMakerList 
	= FXCollections.observableArrayList(new ArrayList<GradientMaker>());
	private final ObjectProperty<Gradient> palette = new SimpleObjectProperty<Gradient>();

	
	@FXML Gradient gradient;
	@FXML AnchorPane opacityArrowContainer;
	@FXML AnchorPane colorArrowContainer;
	
	@FXML NumberField locationField;
	@FXML ColorPicker firstColorPicker;
	@FXML ColorPicker secondColorPicker;
	@FXML IconButton removeSelectedArrow;
	@FXML CheckBox splitColorArrow;
	
	public CustomGradient(ArrayList<ColorArrow> colorArrows) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomGradient.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        colorArrowContainer.maxWidthProperty().bind(gradient.fitWidthProperty());
        opacityArrowContainer.maxWidthProperty().bind(gradient.fitWidthProperty());
        
        colorArrowList.addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change arg0) {
				calculateGradientMakers();
				drawArrows();
			}
        });
        gradientMakerList.addListener(new ListChangeListener() {
	    	@Override
			public void onChanged(Change arg0) {
    			updatePalette();
			}
    	});
        
        colorArrowList.addAll(colorArrows);
        colorArrowList.add(new SplitColorArrow(Color.BLUE, Color.GREEN, 0.5));
        
        for(ColorArrow arrow : colorArrowList) {
        	arrow.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					select(arrow);
				}
        	});
        }
        
        drawArrows();
	}
	
	public CustomGradient() {
		this(new ArrayList<ColorArrow>(Arrays.asList(new ColorArrow(Color.BLACK, 0.0), new ColorArrow(Color.WHITE, 1.0))));
	}
	
	public void drawArrows() {
		colorArrowContainer.getChildren().clear();
		for(ColorArrow arrow : colorArrowList) {
        	colorArrowContainer.getChildren().add(arrow);
        	AnchorPane.setLeftAnchor(arrow, arrow.position.get() * colorArrowContainer.getWidth());
        }
	}
	
	private void calculateGradientMakers() {
		ArrayList<ColorArrow> arrows = new ArrayList<ColorArrow>(colorArrowList);
		arrows.sort(null);
		gradientMakerList.clear();
		for(int i = 0; i < arrows.size() - 1; i++) {
			if(arrows.get(i) instanceof SplitColorArrow) {
				gradientMakerList.add(new GradientMaker((Color) ((SplitColorArrow) arrows.get(i)).secondColor.getFill(), 
						(Color) arrows.get(i + 1).color.getFill(), 50, "Linear"));
			} else {
				gradientMakerList.add(new GradientMaker((Color) arrows.get(i).color.getFill(), 
						(Color) arrows.get(i + 1).color.getFill(), 50, "Linear"));
			}
		}
	}
	
	public void updatePalette(){
		ArrayList<GradientMaker> gradientMakerList = new ArrayList<GradientMaker>();		
		for(GradientMaker gradientMaker : this.gradientMakerList) {
        	gradientMakerList.add(gradientMaker);
		}
		this.palette.set(new Gradient(gradientMakerList));
        gradient.setGradientMakerList(gradientMakerList);
	}
	
	public void select(ColorArrow thisArrow) {
		for(ColorArrow arrow : colorArrowList) {
			if(arrow.selected) {
				arrow.triangle.setFill(Color.web("#c7c7c7"));
				arrow.square.setFill(Color.web("#c7c7c7"));
				
				try {
					arrow.position.unbind();
					arrow.color.fillProperty().unbind();
					if(thisArrow instanceof SplitColorArrow) {
						((SplitColorArrow) thisArrow).secondColor.fillProperty().unbind();
					}
				} catch(Exception e) {
					System.out.println("Unsuccessful unbinding.");
				}
				
				arrow.selected = false;
			}	
		}
		
		thisArrow.triangle.setFill(Color.BLACK);
		thisArrow.square.setFill(Color.BLACK);
		thisArrow.position.bindBidirectional(locationField.valueProperty());
		thisArrow.color.fillProperty().bind(firstColorPicker.valueProperty());
		if(thisArrow instanceof SplitColorArrow) {
			((SplitColorArrow) thisArrow).secondColor.fillProperty().bind(secondColorPicker.valueProperty());
			splitColorArrow.setSelected(true);
		} else {
			splitColorArrow.setSelected(false);
		}
		
		thisArrow.selected = true;
	}
	
	@FXML public void newOpacityArrow() {
		
	}
	
	@FXML public void newColorArrow() {
		
	}
	
	@FXML public void reverse() {
		Collections.reverse(colorArrowList);
	}
	
	@FXML public void removeSelectedArrow() {
	}
	
	public static class ColorArrow extends VBox implements Comparable<ColorArrow> {
		private DoubleProperty position = new SimpleDoubleProperty(0);
		private boolean selected = false;
		
		@FXML Rectangle color;
		@FXML StackPane colorContainer;
		@FXML SVGPath triangle;
		@FXML Rectangle square;
		
		public ColorArrow(Color c, Double pos) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ColorArrow.fxml"));
	        fxmlLoader.setController(this);
	        fxmlLoader.setRoot(this);
	        fxmlLoader.setClassLoader(getClass().getClassLoader());
	        
	        try {
	            fxmlLoader.load();
	        } catch (IOException exception) {
	            throw new RuntimeException(exception);
	        }
	        
	        color.setFill(c);
	        position.set(pos);
		}
		
		public ColorArrow() {
			this(Color.BLACK, 0.0);
		}

		@Override
		public int compareTo(ColorArrow arrow) {
			if(this.position.get() > arrow.position.get()) {
				return 1;
			} else if (this.position.get() < arrow.position.get()) {
				return -1;
			}
			return 0;
		}
	}
	
	public static class SplitColorArrow extends ColorArrow {
		@FXML SVGPath secondColor = new SVGPath();
		
		public SplitColorArrow(Color c, Color c2, double pos) {
			super(c, pos);
	        secondColor.setContent("M 13 3 L 13 13 L 3 13 Z");
	        secondColor.setFill(c2);
	        
	        colorContainer.getChildren().add(secondColor);
		}
		
		public SplitColorArrow() {
			this(Color.BLACK, Color.WHITE, 0);
		}
	}

}
