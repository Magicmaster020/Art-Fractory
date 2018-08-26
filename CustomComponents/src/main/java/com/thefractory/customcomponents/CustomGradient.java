package com.thefractory.customcomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseDragEvent;
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

	private static int totalDepth = 1000;	
	private static int width = 518;
	double drag = 0;
	
	@FXML Gradient gradient;
	@FXML AnchorPane colorArrowContainer;
	
	@FXML NumberField positionField;
	@FXML ColorPicker firstColorPicker;
	@FXML IconButton removeSelectedArrow;
	
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
        
        colorArrowContainer.prefWidthProperty().bind(gradient.fitWidthProperty());
        colorArrowContainer.maxWidthProperty().bind(gradient.fitWidthProperty());
        
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
        
        for(ColorArrow arrow : colorArrows) {
        	newColorArrow(arrow);
        }
        
        for(ColorArrow arrow : colorArrowList) {
        	arrow.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					select(arrow);
					event.consume();
				}
        	});
        }
        
        drawArrows();
	}
	
	public CustomGradient() {
		this(new ArrayList<ColorArrow>(Arrays.asList(new ColorArrow(Color.BLACK, 0.0), new ColorArrow(Color.WHITE, 1.0))));
	}
	
	public void drawArrows() {
		for(ColorArrow arrow : colorArrowList) {
        	AnchorPane.setLeftAnchor(arrow, arrow.position.get() * width);
        }
	}
	
	private void calculateGradientMakers() {
		ArrayList<ColorArrow> arrows = new ArrayList<ColorArrow>(colorArrowList);
		arrows.sort(null);
		gradientMakerList.clear();
		
		gradientMakerList.add(new GradientMaker((Color) arrows.get(0).color.getFill(), 
				(Color) arrows.get(0).color.getFill(), 
				(int) Math.round(totalDepth* (arrows.get(0).position.doubleValue() 
						- 0)), "Linear"));
		
		for(int i = 0; i < arrows.size() - 1; i++) {
			gradientMakerList.add(new GradientMaker((Color) arrows.get(i).color.getFill(), 
					(Color) arrows.get(i + 1).color.getFill(), 
					(int) Math.round(totalDepth * (arrows.get(i + 1).position.doubleValue() 
							- arrows.get(i).position.doubleValue())), "Linear"));
		}
		
		gradientMakerList.add(new GradientMaker((Color) arrows.get(arrows.size() - 1).color.getFill(), 
				(Color) arrows.get(arrows.size() - 1).color.getFill(), 
				(int) Math.round(totalDepth * (1 
						- arrows.get(arrows.size() - 1).position.doubleValue())), "Linear"));
		updatePalette();
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
					arrow.position.unbindBidirectional(positionField.valueProperty());
					arrow.color.fillProperty().unbind();
				} catch(Exception e) {
					System.out.println("Unsuccessful unbinding.");
				}
				arrow.selected = false;
			}	
		}
		
		thisArrow.triangle.setFill(Color.BLACK);
		thisArrow.square.setFill(Color.BLACK);
		
		positionField.setValue(thisArrow.position.doubleValue());
		thisArrow.position.bindBidirectional(positionField.valueProperty());
		
		firstColorPicker.setValue((Color) thisArrow.color.getFill());
		thisArrow.color.fillProperty().bind(firstColorPicker.valueProperty());
		
		thisArrow.selected = true;
	}
	
	public void newColorArrow(ColorArrow arrow) {
		arrow.color.fillProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				calculateGradientMakers();
			}
		});
		arrow.position.addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				drawArrows();
				calculateGradientMakers();
			}
		});
		
		arrow.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				select(arrow);
				drag = event.getSceneX();
				event.consume();
			}
		});
		arrow.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double x = event.getSceneX();
				System.out.println("x: " + x + " drag: " + drag);
				arrow.position.set(arrow.position.doubleValue() + ((double) x - drag)/width);
			
				drag = x;
			}
		});
		
		colorArrowList.add(arrow);
    	colorArrowContainer.getChildren().add(arrow);
		select(arrow);
	}
	
	public void newColorArrow(double position) {
		ColorArrow arrow = new ColorArrow();
		arrow.position.set(position);
		newColorArrow(arrow);
	}
	
	@FXML public void newColorArrow() {
		newColorArrow(0.5);
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
	
}
