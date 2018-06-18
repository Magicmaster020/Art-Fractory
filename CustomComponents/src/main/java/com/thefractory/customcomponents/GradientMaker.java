package com.thefractory.customcomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GradientMaker extends StackPane {

	private final ObjectProperty<ArrayList<Color>> gradient;
	
	@FXML private ColorPicker start = new ColorPicker();
	@FXML private ColorPicker stop = new ColorPicker();
	@FXML private NumberField depth;
	@FXML private ComboBox<String> function;
	Map<String, Function> functions = new HashMap<String, Function>();
	
	public GradientMaker(@NamedArg("start") Color start, @NamedArg("stop") Color stop, 
			@NamedArg("depth") int depth, @NamedArg("min") int min, 
			@NamedArg("max") int max, @NamedArg("function") String function) {
				
		this.gradient = new SimpleObjectProperty<ArrayList<Color>>(this, "gradient", null);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradientMaker.fxml"));
		fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        setStart(start);
        setStop(stop);
        setDepth(depth);
        setFunction(function);
		this.setup();
	}
	
	public GradientMaker() {
		this(Color.BLACK, Color.WHITE, 50, 1, 500, "Linear");
	}
	
	public GradientMaker(GradientMaker gradientMaker) {
		this(gradientMaker.stop.getValue(), gradientMaker.start.getValue(), 
				gradientMaker.getDepth(), gradientMaker.getMin(), gradientMaker.getMax(), 
				gradientMaker.function.getSelectionModel().getSelectedItem());
	}
	
	public int getMin() {
		return 5;
	}
	public int getMax() {
		return 5;
	}
	
	private void setup() {
		
		//Lambdas
		Function linear = (x) -> x;
		functions.put("Linear", linear);
		Function cosine = (x) -> (1-Math.cos(Math.PI*x))/2;
		functions.put("Sine", cosine);
		
		int[] constants = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		Function[] exponentials = new Function[constants.length];
		for(int i = 0; i < exponentials.length; i++) {
			int j = i;
			exponentials[i] = (x) -> (1-Math.exp(constants[j]*x))/(1-Math.exp(constants[j]));
			functions.put("Exponential " + Integer.toString(constants[i]), exponentials[i]);
		}
			
		ObjectProperty<Color> colorPropertyStart = this.start.valueProperty();
		colorPropertyStart.addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> arg0, Color oldValue, Color newValue) {
				updateGradient();	
			}
		});
		ObjectProperty<Color> colorPropertyStop = this.stop.valueProperty();
		colorPropertyStop.addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> arg0, Color oldValue, Color newValue) {
				updateGradient();	
			}
		});
		this.depth.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				updateGradient();	
			}
		});
		
	    ObservableList<String> options = FXCollections.observableArrayList(functions.keySet());
	    options.sort(null);
	    Collections.reverse(options);
	    this.function.setItems(options);
	    this.function.getSelectionModel().selectFirst();
		this.function.setPromptText("Function");
		ObjectProperty<String> functionProperty = this.function.valueProperty();
		functionProperty.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				updateGradient();	
			}
		});
	}
	
	public void updateGradient(){
		ArrayList<Color> palette = new ArrayList<Color>();
		
		double startRed = start.getValue().getRed();
		double startGreen = start.getValue().getGreen();
		double startBlue = start.getValue().getBlue();
		double stopRed = stop.getValue().getRed();
		double stopGreen = stop.getValue().getGreen();
		double stopBlue = stop.getValue().getBlue();
		
		Function f = getFunctionLambda();
		
		if(getDepth() == 1) {
			palette.add(Color.color(startRed, startGreen, startBlue));
		} else {
			for(int i = 0; i < getDepth(); i++) {
				double red = (startRed*(1-f.operation((double) i/(getDepth()-1))) + stopRed*f.operation((double) i/(getDepth()-1)));  
				double green = (startGreen*(1-f.operation((double) i/(getDepth()-1))) + stopGreen*f.operation((double) i/(getDepth()-1)));  
				double blue = (startBlue*(1-f.operation((double) i/(getDepth()-1))) + stopBlue*f.operation((double) i/(getDepth()-1)));
				
				palette.add(Color.color(red, green, blue));
			}
		}	
		setGradient(palette);
	}
	
	public int[] getStartRGB() {
		int[] color = new int[3];
		color[0] = (int) (255*start.getValue().getRed()); 
		color[1] = (int) (255*start.getValue().getGreen()); 
		color[2] = (int) (255*start.getValue().getBlue());
		
		return color;
	}
	
	public int[] getStopRGB() {
		int[] color = new int[3];
		color[0] = (int) (255*stop.getValue().getRed()); 
		color[1] = (int) (255*stop.getValue().getGreen()); 
		color[2] = (int) (255*stop.getValue().getBlue());
		
		return color;
	}
	
	public Function getFunctionLambda() {
		return functions.get(function.getSelectionModel().getSelectedItem());
	}
	
	public interface Function {
		public double operation(double x);
	}

	public final ArrayList<Color> getGradient(){
		return gradient.getValue();
	}
	
	public final void setGradient(ArrayList<Color> value) {
		gradient.setValue(value);
	}
	
	public final ObjectProperty<ArrayList<Color>> gradientProperty() {
		return gradient;
	}

	public final Color getStart() {
		return start.getValue();
	}
	
	public final void setStart(Color value) {
		start.setValue(value);
	}
	
	public final ObjectProperty<Color> startProperty() {
		return start.valueProperty();
	}
	
	public final Color getStop() {
		return stop.getValue();
	}
	
	public final void setStop(Color value) {
		stop.setValue(value);
	}
	
	public final ObjectProperty<Color> stopProperty() {
		return stop.valueProperty();
	}
	
	public final int getDepth() {
		return (int) Math.round(depth.getValue());
	}
	
	public final void setDepth(int value) {
		depth.setValue((double) value); 
	}
	
	public final IntegerProperty depthProperty() {
		IntegerProperty i = new SimpleIntegerProperty();
		i.bind(depth.valueProperty());
		return i;
	}
	
	public final String getFunction() {
		return function.getValue();
	}
	
	public final void setFunction(String value) {
		function.setValue(value);
	}
	
	public final ObjectProperty<String> functionProperty() {
		return function.valueProperty();
	}
}