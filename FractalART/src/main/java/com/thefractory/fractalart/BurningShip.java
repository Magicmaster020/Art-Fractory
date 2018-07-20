package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.ComplexNumber;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class BurningShip extends ComplexIterator {

	protected IntegerProperty power = new SimpleIntegerProperty(2);
	protected DoubleProperty realStart = new SimpleDoubleProperty(0.0);
	protected DoubleProperty imaginaryStart = new SimpleDoubleProperty(0.0);
	
	@FXML private StackPane controlPanel;
	@FXML private GradientPicker gradientPicker;
	@FXML private NumberSlider iterationSlider;
	@FXML private NumberSlider powerSlider;
	@FXML private NumberSlider realStartSlider;
	@FXML private NumberSlider imaginaryStartSlider;
	@FXML private NumberSlider divergeSizeSlider;

	public BurningShip() {
		super();
		DEFAULT_NAME = "Burning Ship Fractal";
		name = DEFAULT_NAME;
		this.setText(name);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BurningShip.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        iterations.bind(iterationSlider.valueProperty());
        power.bind(powerSlider.valueProperty());
        realStart.bind(realStartSlider.valueProperty());
        imaginaryStart.bind(imaginaryStartSlider.valueProperty());
        divergeSize.bind(divergeSizeSlider.valueProperty());
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);
		setGradientPicker(gradientPicker);

		iterations.addListener(updateListener);
		power.addListener(updateListener);
		realStart.addListener(updateListener);
		imaginaryStart.addListener(updateListener);
		divergeSize.addListener(updateListener);
		
		init();
	}
	
	@Override
	protected int iterate(ComplexNumber c) {
		ComplexNumber z = new ComplexNumber(realStart.get(), imaginaryStart.get());
		int i = 0;
		for(; i < hej; i++) {
			z = ComplexNumber.add(ComplexNumber.pow(new ComplexNumber(Math.abs(z.getRe()), Math.abs(z.getIm())), power.get()), c);
			if(z.getAbs() > divergeSize.get()) {
				break;
			}
		}
		return i;
	}
}
