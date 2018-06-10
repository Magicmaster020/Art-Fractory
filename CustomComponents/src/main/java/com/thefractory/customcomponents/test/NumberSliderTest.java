package com.thefractory.customcomponents.test;

import java.io.IOException;

import com.thefractory.customcomponents.NumberSlider;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NumberSliderTest extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        final NumberSlider slider = new NumberSlider(true, false, true, 1, 1, 1000);
        slider.setTip("This is a tip.");
        
        slider.field.valueProperty().addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
                System.out.println("Value: " + slider.field.getValue());
            }
        });
        
        Scene scene = new Scene(slider);

        primaryStage.setTitle("NumberSlider Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}