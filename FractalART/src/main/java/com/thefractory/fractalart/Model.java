package com.thefractory.fractalart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thefractory.fractalart.test.ArtworkTest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Model {
	
	private static Model model = new Model();
	
	private Model() { }
	
	public static Model getInstance() {
		return(model);
	}
	
	
	public void exportAs(Artwork artwork) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Artwork");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("JPEG", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"), 
		         new ExtensionFilter("TIFF", "*.tif", "*.tiff"), 
		         new ExtensionFilter("PNG", "*.png"), 
		         new ExtensionFilter("GIF", "*.gif"));
		//File outputfile = new File("images/" + artwork.getName() + "." + fileType);
		/*
		new ChangeListener<ObjectProperty>() {
			@Override
			public void changed(ObservableValue<? extends ObjectProperty> observable, ObjectProperty oldValue,
					ObjectProperty newValue) {
				String name = fileChooser.getInitialFileName();
				String extension = name.substring(1 + name.lastIndexOf(".")).toLowerCase();
				System.out.println("Nu blev det " + newValue.getDescription());
			}
		});*/
		
		
		File outputFile = fileChooser.showSaveDialog(ArtworkTest.primaryStage);
		
		if(outputFile != null) {
			String name = outputFile.getName();
		    String extension = name.substring(1 + name.lastIndexOf(".")).toLowerCase();
		    System.out.println(extension);
			try {
			switch (extension) {
				case "tiff":
				case "tif":
					System.out.println(ImageIO.write(SwingFXUtils.fromFXImage(artwork.getImage(1920,1080), null), "png", outputFile));
					System.out.println("saved");
					break;
				case "png":
					ImageIO.write(SwingFXUtils.fromFXImage(artwork.getImage(1920,1080), null), "png", outputFile);
					System.out.println("saved");
					break;
				case "jpg":
				case "jpeg":
				case "jpe":
				case "jfif":
					BufferedImage bImage =SwingFXUtils.fromFXImage(artwork.getImage(1920,1080), null);
					BufferedImage bImage2 = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
					bImage2.getGraphics().drawImage(bImage, 0, 0, null);
					System.out.println(ImageIO.write(bImage2, "jpg", outputFile));
					System.out.println("saved");
					break;
				case "gif":
					ImageIO.write(SwingFXUtils.fromFXImage(artwork.getImage(1920,1080), null), "gif", outputFile);
					System.out.println("saved");
					break;
				default: 
					System.out.println("Unsupported extension");
					break;
			}
			}catch(IOException e) {
				System.out.println("Saving error");
			}
		}
	}
}
