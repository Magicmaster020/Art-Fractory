package com.thefractory.customcomponents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.batik.css.parser.DefaultLangCondition;

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
	private final ObjectProperty<Gradient> palette = new SimpleObjectProperty<Gradient>();
	/**
	 * The currently selectedSaveGradient.
	 */
	private EnhancedGradient selectedGradient;
	private boolean customVisible = true;
	private static String pathToGradients = "C:/Users/Jonas/Documents/savedGradients.txt";
	
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
        

		
        

        //Reads in all default gradients.
        try {
        	InputStream stream = getClass().getClassLoader().getResourceAsStream(
				"com/thefractory/customcomponents/defaultGradients.txt");
	        addGradientsFromFile(stream, true);		
	        //Reads in all by the user saved gradients.
	        stream = new FileInputStream(pathToGradients);
	        addGradientsFromFile(stream, false);
	        stream.close();
        } catch(Exception e){
        	e.printStackTrace();
        }
		
        
		//Selects the first Gradient as default.
		try {
            selectGradient(savedGradientList.get(0));
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
	private void addGradientsFromFile(InputStream stream, boolean defaultGradient) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			ArrayList<String> lines = new ArrayList<String>();
	        while(true) {
	        	String line = bufferedReader.readLine();
	        	if(line == null) {
	        		break;
	        	}
	        	lines.add(line);
	        }
	        bufferedReader.close();
			
			for(String line : lines) {
				String[] args = line.split(", ");
				
				ArrayList<GradientMaker> gradientMakerList = new ArrayList<GradientMaker>();
		        for(int i = 0; i < args.length; i+=4) {
		        	GradientMaker gradientMaker = new GradientMaker(
		        			Color.color(Double.parseDouble(args[i].split(" ")[0]),
				            		Double.parseDouble(args[i].split(" ")[1]),
				            		Double.parseDouble(args[i].split(" ")[2])),
		        			Color.color(Double.parseDouble(args[i+1].split(" ")[0]),
		    	            		Double.parseDouble(args[i+1].split(" ")[1]),
		    	            		Double.parseDouble(args[i+1].split(" ")[2])),
		        			Integer.parseInt(args[i+2]),
		        			args[i+3]);

		            gradientMakerList.add(gradientMaker);
		        }
		        addGradient(new Gradient(gradientMakerList), defaultGradient);
			}
		} catch(Exception e) {
			e.printStackTrace();
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
		private boolean defaultGradient;
		
		private EnhancedGradient(Gradient gradient, boolean defaultGradient) {
			this.gradient = gradient;
			setup(defaultGradient);
		}
		
		private EnhancedGradient(ArrayList<GradientMaker> gradientMakers, boolean defaultGradient) {
			this.gradient = new Gradient(gradientMakers);
			setup(defaultGradient);
		}
		
		private void setup(boolean defaultGradient) {
			this.defaultGradient = defaultGradient;
			
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
			if(defaultGradient) {
				remove.setDisable(true);
			}
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
				ArrayList<GradientMaker> gradientMakers = new ArrayList<GradientMaker>();
				for(GradientMaker gradientMaker : selectedGradient.gradient.getGradientMakerList()) {
					EnhancedGradientMaker enhancedGradientMaker = new EnhancedGradientMaker(gradientMaker);
					addGradientMaker(enhancedGradientMaker);
					gradientMakers.add(gradientMaker);
				}
				selectGradient(null);
	        	palette.set(customGradient.clone());
			}
        	customGradient.setFitWidth(innerCustomPane.getWidth());
		}
	}

	/**
	 * Saves the current custom {@code Gradient} to a file.
	 * Returns true if the operation succeeded false otherwise. 
	 * @return
	 */
	@FXML public boolean saveGradient() {
				
		boolean successful = false;
		String lineToAdd = "";
		for(GradientMaker gradientMaker: customGradient.gradientMakerList) {
			lineToAdd += gradientMaker.getStart().getRed() + " "
					+ gradientMaker.getStart().getGreen() + " "
					+ gradientMaker.getStart().getBlue() + ", "
					+ gradientMaker.getStop().getRed() + " "
					+ gradientMaker.getStop().getGreen() + " "
					+ gradientMaker.getStop().getBlue() + ", "
					+ gradientMaker.getDepth() + ", "
					+ gradientMaker.getFunction() + ", ";
		}
		lineToAdd += "\n";
		
		try {
			Writer writer = new BufferedWriter(new FileWriter(pathToGradients, true));
			writer.append(lineToAdd);
			writer.close();
		    addGradient(customGradient.clone(), false);
		    successful = true;
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return successful;
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
		for(EnhancedGradientMaker enhancedGradientMaker : this.gradientMakerList) {
        	gradientMakerList.add(enhancedGradientMaker.gradientMaker);
		}
		this.palette.set(new Gradient(gradientMakerList));
        customGradient.setGradientMakerList(gradientMakerList);
	}
	
	/**
	 * Adds a new {@code Gradient} to the set of saved {@code Gradient}s.
	 * @param gradient
	 */
	
	public void addGradient(Gradient gradient, Boolean defaultGradient) {
		ArrayList<GradientMaker> gradientMakers = new ArrayList<GradientMaker>();
		for(GradientMaker gradientMaker : gradient.getGradientMakerList()) {
			gradientMakers.add(gradientMaker);
		}
		EnhancedGradient enhancedGradient = new EnhancedGradient(gradientMakers, defaultGradient);
		savedGradientList.add(enhancedGradient);
    	savedGradientBox.getChildren().add(enhancedGradient.box);
	}
	
	/**
	 * Deletes a saved {@code Gradient}.
	 * @param enhancedGradient
	 * @return
	 */
	public boolean removeSavedGradient(EnhancedGradient enhancedGradient) {
		boolean successful = false;
		if(!enhancedGradient.defaultGradient) {
			try {
				File file = new File(pathToGradients);
				BufferedReader reader = new BufferedReader(new FileReader(file));

				String lineToRemove = "";
				for(GradientMaker gradientMaker: enhancedGradient.gradient.gradientMakerList) {
					lineToRemove += gradientMaker.getStart().getRed() + " "
							+ gradientMaker.getStart().getGreen() + " "
							+ gradientMaker.getStart().getBlue() + ", "
							+ gradientMaker.getStop().getRed() + " "
							+ gradientMaker.getStop().getGreen() + " "
							+ gradientMaker.getStop().getBlue() + ", "
							+ gradientMaker.getDepth() + ", "
							+ gradientMaker.getFunction() + ", ";
				}
				
				String currentLine;
				String output = "";
				boolean found = false;
				
				while((currentLine = reader.readLine()) != null) {
				    if(currentLine.equals(lineToRemove) && !found) {
				    	found = true;
				    	continue;
				    }
				    output += currentLine + "\n";
				}
				reader.close(); 
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(output);
				writer.close();
			
				savedGradientBox.getChildren().remove(enhancedGradient.box);
				savedGradientList.remove(enhancedGradient);
				if(enhancedGradient.equals(selectedGradient)) {
					selectGradient(savedGradientList.get(0));
				}
				
				if(savedGradientList.size() == 1) {
					savedGradientList.get(0).remove.setDisable(true);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return successful;
	}
	
	/**
	 * Marks the given saved {@code EnhancedGradient} as selected.
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
			palette.set(enhancedGradient.gradient.clone());
			selectedGradient.setSelected(true);
		}
	}

	public Gradient getPalette() {
		return palette.get();
	}
	public ObjectProperty<Gradient> paletteProperty() {
		return palette;
	}
}
