package com.thefractory.customcomponents.test;

import java.io.IOException;
import java.util.ArrayList;

import com.thefractory.customcomponents.Gradient;
import com.thefractory.customcomponents.GradientMaker;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GradientMakerTest extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		GradientMaker gradientMaker = new GradientMaker();
		Gradient gradient = new Gradient();
		VBox box = new VBox();
		box.getChildren().addAll(gradientMaker, gradient);
		
		gradientMaker.paletteProperty().addListener(new ChangeListener<ArrayList<Color>>() {
			@Override
			public void changed(ObservableValue<? extends ArrayList<Color>> arg0, ArrayList<Color> arg1,
					ArrayList<Color> arg2) {
				ArrayList<GradientMaker> gradientMakerList = new ArrayList<GradientMaker>();
				gradientMakerList.add(gradientMaker);
				gradient.setGradientMakerList(gradientMakerList);
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