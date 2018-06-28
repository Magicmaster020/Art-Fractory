package com.thefractory.fractalart;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thefractory.fractalart.test.ArtworkTest;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Model {
	
	private static Model model = new Model();
	
	private Model() { }
	
	public static Model getInstance() {
		return(model);
	}
	
	
	public void exportAs(Artwork artwork) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Artwork");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.tiff"));
		//File outputfile = new File("images/" + artwork.getName() + "." + fileType);
		File outputfile = fileChooser.showSaveDialog(ArtworkTest.primaryStage);
		
		String fileType = "tiff";
		
		try {
		switch (fileType) {
			case "tiff": 
				ImageIO.write(SwingFXUtils.fromFXImage(artwork.getImage(1920,1080), null), "png", outputfile);
				System.out.println("Saved");
		}
		}catch(IOException e) {
			System.out.println("Saving error");
		}
	}
}
