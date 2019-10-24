package com.thefractory.customcomponents;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A GUI interface for making simple gradients.
 * Best used in larger groups.
 * 
 * @author Ivar Eriksson
 *
 */
public class GradientMaker extends StackPane {

	/**
	 * The gradient property.
	 */
	protected final ObjectProperty<ArrayList<Color>> palette;
	/**
	 * Start color.
	 */
	protected ObjectProperty<Color> start;
	/**
	 * End color.
	 */
	protected ObjectProperty<Color> stop;
	/**
	 * Color depth.
	 */
	protected IntegerProperty depth;
	
	/**
	 * Makes a {@code GradientMaker} with the given parameters.
	 * @param start
	 * @param stop
	 * @param depth
	 * @param min
	 * @param max
	 * @param function
	 */
	public GradientMaker(Color start, Color stop, int depth) {
				
		this.palette = new SimpleObjectProperty<ArrayList<Color>>();
		setup(start, stop, depth);
	}
	
	/**
	 * Makes a reversed {@code GradientMaker} from the given {@code GradientMaker}. 
	 * @param gradientMaker
	 */
	public GradientMaker(GradientMaker gradientMaker) {
		this(gradientMaker.stop.getValue(), gradientMaker.start.getValue(), 
				gradientMaker.getDepth());
	}
	
	/**
	 * Makes a default {@code GradientMaker}.
	 */
	public GradientMaker() {
		this(Color.BLACK, Color.WHITE, 50);
	}
	
	/**
	 * Fixes some general setup for the constructors.
	 */
	private void setup(Color start, Color stop, int depth) {

        setStart(start);
        setStop(stop);
        setDepth(depth);
					
        this.start.addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> arg0, Color oldValue, Color newValue) {
				updatePalette();	
			}
		});
        this.stop.addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> arg0, Color oldValue, Color newValue) {
				updatePalette();	
			}
		});
		this.depth.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				updatePalette();	
			}
		});
			
		updatePalette();
	}
	
	/**
	 * Updates the {@code palette based on the current list of {@code GradientMakers}.
	 */
	public void updatePalette(){
		ArrayList<Color> palette = new ArrayList<Color>();
		
		double startRed = start.getValue().getRed();
		double startGreen = start.getValue().getGreen();
		double startBlue = start.getValue().getBlue();
		double stopRed = stop.getValue().getRed();
		double stopGreen = stop.getValue().getGreen();
		double stopBlue = stop.getValue().getBlue();
		
		if(getDepth() == 1) {
			palette.add(Color.color(startRed, startGreen, startBlue));
		} else {
			for(int i = 0; i < getDepth(); i++) {
				double red = (startRed*((double) i/getDepth()) + stopRed*(1 - (double) i/getDepth()));  
				double green = (startGreen*((double) i/getDepth()) + stopGreen*(1 - (double) i/getDepth()));  
				double blue = (startBlue*((double) i/getDepth()) + stopBlue*(1 - (double) i/getDepth()));  
				
				palette.add(Color.color(red, green, blue));
			}
		}	
		this.palette.set(palette);
	}

	
	/**
	 * The getters and setters for various properties.
	 */
	public final ArrayList<Color> getPalette(){
		return palette.getValue();
	}
	public final ObjectProperty<ArrayList<Color>> paletteProperty() {
		return palette;
	}
	public final Color getStart() {
		return start.getValue();
	}
	public final void setStart(Color value) {
		start.setValue(value);
	}
	public final ObjectProperty<Color> startProperty() {
		return start;
	}
	public final Color getStop() {
		return stop.getValue();
	}
	public final void setStop(Color value) {
		stop.setValue(value);
	}
	public final ObjectProperty<Color> stopProperty() {
		return stop;
	}
	public final int getDepth() {
		return (int) Math.round(depth.getValue());
	}
	public final void setDepth(int value) {
		depth.setValue((double) value); 
	}
	public final IntegerProperty depthProperty() {
		IntegerProperty i = new SimpleIntegerProperty();
		i.bind(depth);
		return i;
	}
}