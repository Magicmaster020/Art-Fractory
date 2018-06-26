package com.thefractory.customcomponents.test;

import java.io.IOException;
import com.thefractory.customcomponents.GradientPicker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GradientPickerTest extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		GradientPicker gradientPicker = new GradientPicker();
		
		Scene scene = new Scene(gradientPicker);
		
	    primaryStage.setTitle("GradientMaker Test");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	
}
