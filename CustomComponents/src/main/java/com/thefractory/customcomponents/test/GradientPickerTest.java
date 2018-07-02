package com.thefractory.customcomponents.test;

import java.io.IOException;
import com.thefractory.customcomponents.GradientPicker;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GradientPickerTest extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {

		GradientPicker gradientPicker = new GradientPicker();
		VBox box = new VBox();
		box.getChildren().addAll(gradientPicker, gradientPicker.getPalette());
		
		gradientPicker.paletteProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
				box.getChildren().clear();
				box.getChildren().addAll(gradientPicker, gradientPicker.getPalette());
			}
		});
		
		Scene scene = new Scene(box);
		
	    primaryStage.setTitle("GradientMaker Test");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

