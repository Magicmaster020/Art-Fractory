package com.thefractory.customcomponents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * JavaFX component that lets the user easily create a custom gradient.
 * @author ivarc
 *
 */
public class GradientPicker extends StackPane {
	
	/**
	 * Keeps track of the open {@GradientMakers}.
	 */
	ArrayList<EnhancedGradientMaker> observableGradientMakerList = new ArrayList<EnhancedGradientMaker>();
	ObservableList<EnhancedGradientMaker> gradientMakerList 
			= FXCollections.observableArrayList(observableGradientMakerList);
	/**
	 * All saved gradients on display.
	 */
	private ArrayList<EnhancedGradient> savedGradientList = new ArrayList<EnhancedGradient>();
	/**
	 * The current combined gradient.
	 */
	private final ObjectProperty<ArrayList<Color>> palette = new SimpleObjectProperty<ArrayList<Color>>();
	/**
	 * The currently selectedSaveGradient.
	 */
	private EnhancedGradient selectedGradient;
	private boolean customVisible = true;
	
	/**
	 * The container for all gradientMakers.
	 */
	@FXML private VBox gradientMakerBox;
	@FXML private IconButton addNewGradientMaker; 
	/**
	 * The display for the custom {@code Gradient}.
	 */
	@FXML private Gradient customGradient;
	@FXML private HBox customPane;
	@FXML private VBox innerCustomPane;
	/**
	 * The container for all saved {@code Gradient}s.
	 */
	@FXML private VBox savedGradientBox;
	@FXML private TextField nameField;

	/**
	 * Creates a {@code GradientPicker}.
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

        //Reads in all saved gradients.
        for (File fileEntry : new File(System.getProperty("user.dir") 
				+ "/src/main/resources/com/thefractory/customcomponents/gradients/").listFiles()) {
        	addGradient(new Gradient(getGradientMakersFromFile(fileEntry)), fileEntry.getAbsolutePath());
        }
        try {
            selectedGradient = savedGradientList.get(0);
            selectedGradient.setSelected(true);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        savedGradientBox.getStylesheets().add(getClass().getResource("gradientPicker.css").toString());
              
        gradientMakerList.addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change arg0) {
               updatePalette();
			}
        });
        Platform.runLater(() -> {toggleCustomPane();});
	}
	
	/**
	 * Reads {@code GradientMaker}s from file.
	 * @param file
	 * @return
	 */
	private ArrayList<GradientMaker> getGradientMakersFromFile(File file) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
	        palette.set(new ArrayList<Color>());
	        ArrayList<String[]> lines = new ArrayList<String[]>();
	        while(true) {
	        	String line = bufferedReader.readLine();
	        	if(line == null) {
	        		break;
	        	}
	            lines.add(line.trim().split(" "));
	        }
	        
	        bufferedReader.close();
	        
	        ArrayList<GradientMaker> gradientMakerList = new ArrayList<GradientMaker>();
	        for(int i = 0; i < lines.size(); i+=4) {
	        	GradientMaker gradientMaker = new GradientMaker(
	        			Color.color(Double.parseDouble(lines.get(i)[0]),
			            		Double.parseDouble(lines.get(i)[1]),
			            		Double.parseDouble(lines.get(i)[2])),
	        			Color.color(Double.parseDouble(lines.get(i+1)[0]),
	    	            		Double.parseDouble(lines.get(i+1)[1]),
	    	            		Double.parseDouble(lines.get(i+1)[2])),
	        			Integer.parseInt(lines.get(i+2)[0]),
	        			lines.get(i+3)[0]);

	            gradientMakerList.add(gradientMaker);
	        }
	        
	        return gradientMakerList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * An enhanced {@code Gradient} class that adds functionality to {@code Gradient}.
	 * @author Ivar Eriksson
	 *
	 */
	private class EnhancedGradient {
		
		private Gradient gradient;
		private HBox box = new HBox();
		//private DragIcon drag;
		private IconButton remove = new IconButton("close");
		private String location;
		
		private EnhancedGradient(Gradient gradient, String location) {
			this.gradient = gradient;
			setup(location);
		}
		
		private EnhancedGradient(ArrayList<GradientMaker> gradientMakers, String location) {
			this.gradient = new Gradient(gradientMakers);
			setup(location);
		}
		
		private void setup(String location) {
			this.location = location;
			
			box.getChildren().addAll(gradient, remove);
			box.setId("gradient");
			gradient.setOnMousePressed(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					selectGradient(EnhancedGradient.this);
				}
			});
			remove.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					removeSavedGradient(EnhancedGradient.this);
				}
			});
		}
		
		private void setSelected(boolean selected) {
			if(selected) {
				box.setId("selected");
			} else {
				box.setId("gradient");
			}
		}
	}
	
	/**
	 * An enhanced {@code GradientMaker} class that adds functionality to {@code GradientMaker}.
	 * @author Ivar Eriksson
	 *
	 */
	private class EnhancedGradientMaker {
		
		private GradientMaker gradientMaker;
		private HBox box = new HBox();
		//private DragIcon drag;
		private IconButton remove = new IconButton("close");
		
		private EnhancedGradientMaker() {
			this.gradientMaker = new GradientMaker();
			setup();
		}
		
		private EnhancedGradientMaker(EnhancedGradientMaker enhancedGradientMaker) {
			this.gradientMaker = new GradientMaker(enhancedGradientMaker.gradientMaker);
			setup();
		}
		
		private EnhancedGradientMaker(GradientMaker gradientMaker) {
			this.gradientMaker = new GradientMaker(gradientMaker.getStart(),
					gradientMaker.getStop(), gradientMaker.getDepth(), gradientMaker.getFunction());
			setup();
		}
		
		private void setup() {
			remove.setOnAction(event -> {removeGradientMaker(this);});
			box.getChildren().addAll(gradientMaker, remove);
			gradientMaker.paletteProperty().addListener(new ChangeListener<Object>() {
	            public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
	                updatePalette();
	            }
	        });
		}
	}
	
	/**
	 * Adds a new {@code EnhancedGradientMaker} to the current set of {@code EnhancedGradientMaker}s. 
	 * @param enhancedGradientMaker
	 */
	private void addGradientMaker(EnhancedGradientMaker enhancedGradientMaker) {
		gradientMakerList.add(enhancedGradientMaker);
		gradientMakerBox.getChildren().add(enhancedGradientMaker.box);
		if(gradientMakerList.size() == 1) {
			gradientMakerList.get(0).remove.setDisable(true);
		} else if(gradientMakerList.size() == 2) {
			gradientMakerList.get(0).remove.setDisable(false);
		}
//		enhancedGradientMaker.gradientMaker.paletteProperty().addListener(new ChangeListener<ArrayList<Color>>() {
// 			@Override
// 			public void changed(ObservableValue<? extends ArrayList<Color>> arg0, 
// 					ArrayList<Color> oldValue, ArrayList<Color> newValue) {
// 				updatePalette();
// 			}
// 		});
	}
	
	/**
	 * Adds a new {@code EnhancedGradientMaker} on button press.
	 */
	@FXML public void addNewGradientMaker(){
		addGradientMaker(new EnhancedGradientMaker(gradientMakerList.get(gradientMakerList.size() - 1)));
	}
	
	/**
	 * Toggles the position of the pane for making custom {@code Gradient}s.
	 */
	@FXML public void toggleCustomPane() {
		if(customVisible) {
			customVisible = false;
			customPane.setTranslateX(-innerCustomPane.getWidth());
		} else {
			customVisible = true;
			customPane.setTranslateX(0);
			
			if(selectedGradient != null) {
				gradientMakerList.clear();
				gradientMakerBox.getChildren().clear();
				for(GradientMaker gradientMaker : selectedGradient.gradient.getGradientMakerList()) {
					EnhancedGradientMaker enhancedGradientMaker = new EnhancedGradientMaker(gradientMaker);
					addGradientMaker(enhancedGradientMaker);
				}
				selectGradient(null);
			}
        	customGradient.setFitWidth(innerCustomPane.getWidth());
        	palette.set(customGradient.getPalette());
		}
	}

	/**
	 * Saves the current custom {@code Gradient} to a file.
	 * Returns true if the operation succeeded false otherwise. 
	 * @return
	 */
	@FXML public boolean saveGradient() {
		try {
			String location = System.getProperty("user.dir") 
					+ "/src/main/resources/com/thefractory/customcomponents/gradients/"
					+ nameField.getText() + ".txt";
			PrintWriter writer = new PrintWriter(location, "UTF-8");
			
			for(EnhancedGradientMaker gradientMaker : gradientMakerList) {
				writer.println(gradientMaker.gradientMaker.getStart().getRed() + " "
						+ gradientMaker.gradientMaker.getStart().getGreen() + " "
						+ gradientMaker.gradientMaker.getStart().getBlue() + " ");
				writer.println(gradientMaker.gradientMaker.getStop().getRed() + " "
						+ gradientMaker.gradientMaker.getStop().getGreen() + " "
						+ gradientMaker.gradientMaker.getStop().getBlue() + " ");
				writer.println(gradientMaker.gradientMaker.getDepth());
				writer.println(gradientMaker.gradientMaker.getFunction());
			}
			writer.close();

			addGradient(customGradient, location);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Removes a {@code GradientMaker} from the current custom panel.
	 * @param EnhancedGradientMaker
	 */
	private void removeGradientMaker(EnhancedGradientMaker EnhancedGradientMaker) {
		gradientMakerList.remove(EnhancedGradientMaker);
		gradientMakerBox.getChildren().remove(EnhancedGradientMaker.box);
		if(gradientMakerList.size() == 1) {
			gradientMakerList.get(0).remove.setDisable(true);
		}
	}
	
	
	/**
	 * Makes a new {@code Color} list for the custom {@code Gradient}.
	 */
	public void updatePalette(){
		ArrayList<GradientMaker> gradientMakerList = new ArrayList<GradientMaker>();		
		ArrayList<Color> palette = new ArrayList<Color>();
		for(EnhancedGradientMaker enhancedGradientMaker : this.gradientMakerList) {
			palette.addAll(enhancedGradientMaker.gradientMaker.getPalette());
        	gradientMakerList.add(enhancedGradientMaker.gradientMaker);
		}
		this.palette.set(palette);
        customGradient.setGradientMakerList(gradientMakerList);
	}
	
	/**
	 * Adds a new {@code Gradient} to the set of saved {@code Gradient}s.
	 * @param gradient
	 */
	
	public void addGradient(Gradient gradient, String location) {
		ArrayList<GradientMaker> gradientMakers = new ArrayList<GradientMaker>();
		for(GradientMaker gradientMaker : gradient.getGradientMakerList()) {
			gradientMakers.add(gradientMaker);
		}
		EnhancedGradient enhancedGradient = new EnhancedGradient(gradientMakers, location);
		savedGradientList.add(enhancedGradient);
    	savedGradientBox.getChildren().add(enhancedGradient.box);
	}
	
	/**
	 * Deletes a saved {@code Gradient}.
	 * @param enhancedGradient
	 * @return
	 */
	
	public boolean removeSavedGradient(EnhancedGradient enhancedGradient) {
		try {
			System.out.println(enhancedGradient.location);
			Files.delete(Paths.get(enhancedGradient.location));
			savedGradientBox.getChildren().remove(enhancedGradient.box);
			savedGradientList.remove(enhancedGradient);
			if(enhancedGradient.equals(selectedGradient)) {
				selectedGradient = savedGradientList.get(0);
			}
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Marks the givesn saved {@code EnhancedGradient} as selected.
	 * @param enhancedGradient
	 */
	
	public void selectGradient(EnhancedGradient enhancedGradient) {
		if(enhancedGradient == null) {
			selectedGradient.setSelected(false);
			selectedGradient = enhancedGradient;
		} else {
			if(selectedGradient != null) {
				selectedGradient.setSelected(false);
			}
			selectedGradient = enhancedGradient;
			palette.set(selectedGradient.gradient.getPalette());
			selectedGradient.setSelected(true);
		}
	}
}
