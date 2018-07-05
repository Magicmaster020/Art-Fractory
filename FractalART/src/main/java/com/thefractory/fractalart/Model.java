package com.thefractory.fractalart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.thefractory.customcomponents.NumberField;
import com.thefractory.fractalart.test.ArtworkTest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Callback;

public class Model {
	
	private static Model model = new Model();
	
	private Model() { }
	
	public static Model getInstance() {
		return(model);
	}
	
	
	public void exportAs(Artwork artwork){
		
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
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ResolutionDialog.fxml"));
		
		try {
			DialogPane root = loader.load();
			root.getButtonTypes().add(ButtonType.OK);
			Dialog<double[]> dialog = new Dialog<double[]>();
			
			Map<String, Object> fxmlNamespace = loader.getNamespace();
			NumberField dialogWidth = (NumberField) fxmlNamespace.get("dialogWidth");
			NumberField dialogHeight = (NumberField) fxmlNamespace.get("dialogHeight");
			CheckBox preRatio = (CheckBox) fxmlNamespace.get("preRatio");
			
			
			
			
			preRatio.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(newValue) {
						dialogHeight.setDisable(true);
						/*proportionProperty.bind(proportionField.valueProperty());
						proportionField.setDisable(false);
						proportionDraggerTop.setVisible(true);
						proportionDraggerBottom.setVisible(true);*/
					} else {
						dialogHeight.setDisable(false);
						
						/*proportionProperty.unbind();
						proportionProperty.setValue(1);
						proportionField.setDisable(true);
						proportionDraggerTop.setVisible(false);
						proportionDraggerBottom.setVisible(false);*/
					}
				}
	        });
			
			dialog.setDialogPane(root);
			dialog.setResultConverter(new Callback<ButtonType,double[]>(){
				
				@Override
				public double[] call(ButtonType param) {
					System.out.println(param);
					// TODO Auto-generated method stub
					return null;
				}
			});
			
	        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {

				@Override
				public void handle(DialogEvent event) {
					dialog.close();
				}
	        	
	        });

			Optional<double[]> resolution = dialog.showAndWait();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
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
