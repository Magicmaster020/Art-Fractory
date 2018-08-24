package com.thefractory.fractalart.test;

import java.io.File;

import javax.imageio.ImageIO;

import com.thefractory.fractalart.Grid;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class GridTest extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	long start = System.nanoTime();
    	Grid grid = new Grid(6, 6, false, 81, 81, true);
    	grid.setValueAt(10000, 40, 40);
    	while(grid.toppleGrid());
    	
    	ImageView imageView = new ImageView();
    	WritableImage image = grid.getImage(250, 250);
    	imageView.setImage(image);
    	imageView.setFitWidth(500);
    	imageView.setFitHeight(500);
    	
    	// Display image on screen
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Grid Test");
        primaryStage.setScene(scene);
        primaryStage.show();  	
        
        
		File file = new File("C:\\Users\\ivarc\\Pictures\\CanvasImage.png");
		try {
		    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (Exception s) {
			System.out.println("Could not save to file");
		}
  
        
        long elapsedTime = System.nanoTime() - start;
    	System.out.println("Finished in time: " + elapsedTime/1000000000 + " seconds");
    }
}