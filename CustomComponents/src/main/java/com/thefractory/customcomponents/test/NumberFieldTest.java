package com.thefractory.customcomponents.test;


import java.io.IOException;

import com.thefractory.customcomponents.NumberField;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NumberFieldTest extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
        NumberField field = new NumberField(false, 5, -51, 1000);
        field.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
                System.out.println(field.getValue());
            }
        });
        
        Scene scene = new Scene(field);

	    primaryStage.setTitle("NumberField Test");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}