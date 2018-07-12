package com.thefractory.fractalart.test;

import java.io.File;

import javax.imageio.ImageIO;

import com.thefractory.fractalart.Grid;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
 
public class GridTest extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	long start = System.nanoTime();
    	Grid grid = new Grid(6, 6, false, 75, 75, true);
    	grid.setValueAt(10000, 37, 37);
    	while(grid.toppleGrid());
    	
    	ImageView imageView = new ImageView();
    	WritableImage image = grid.getImage(530, 605);
    	imageView.setImage(image);
    	
    	// Display image on screen
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 530, 605);
        primaryStage.setTitle("Grid Test");
        primaryStage.setScene(scene);
        primaryStage.show();  	
    	
        
        
        
		File file = new File("C:\\Users\\Jonas\\Pictures\\CanvasImage.png");
		try {
		    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (Exception s) {
			System.out.println("Could not save to file");
		}
  
        
        long elapsedTime = System.nanoTime() - start;
    	System.out.println("Finished in time: " + elapsedTime/1000000000 + " seconds");
    }
}