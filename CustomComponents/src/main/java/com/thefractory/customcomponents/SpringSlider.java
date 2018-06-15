package com.thefractory.customcomponents;

import java.io.IOException;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SpringSlider extends Slider{
	
	BooleanProperty isHorizontal; 
	
	public SpringSlider(@NamedArg("isHorizontal") boolean isHorizontal){
		//super(-1,1,0);
		this.isHorizontal = new SimpleBooleanProperty(this, "isHorizontal", isHorizontal);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SpringSlider.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
		//button.getStylesheets().add(getClass().getResource("icons/" + iconLocation + "/style.css").toString());
	
	
	
	
	
	this.valueProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
            SpringSlider.this.setValueChanging(true);
        }
    });
    this.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            System.out.println("Mouse released!");
            SpringSlider.this.setValueChanging(false);
            SpringSlider.this.setValue(0);
            //event.consume();
        }	
    });/*
    this.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) {
            System.out.println(newVal);
        }
    });*/
    System.out.println(this.getOnKeyPressed());
    this.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			System.out.println("Jag åt upp eventet");
			SpringSlider.this.setValue(0);
			//event.consume();
		}
    });
    }
}