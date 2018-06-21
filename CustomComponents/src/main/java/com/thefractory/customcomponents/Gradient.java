package com.thefractory.customcomponents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Gradient extends ImageView {
	//Properties
    private final StringProperty gradientLocation;
	
	@FXML public ImageView gradient;
	
	private WritableImage image;
	
	public Gradient(@NamedArg(value="gradientLocation" , defaultValue="pink") String gradientLocation){
		this.gradientLocation = new SimpleStringProperty(this, "gradientLocation", gradientLocation);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Gradient.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        Scanner sc = null;
		try {
			new FileReader("gradients/" + gradientLocation + ".txt");
			sc = new Scanner(new BufferedReader(new FileReader("gradients/" + gradientLocation + ".txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        ArrayList<double[]> colors = new ArrayList<double[]>();
        while(sc.hasNextLine()) {
        	colors.add(new double[3]);
            String[] line = sc.nextLine().trim().split(" ");
            for (int i=0; i < line.length; i++) {
                colors.get(colors.size())[i] = Double.parseDouble(line[i]);
            }
        }
                
        this.image = new WritableImage(500, 30);
        PixelWriter writer = image.getPixelWriter();
        for(int x = 0; x < image.getWidth(); x++) {
        	for(int y = 0; y < image.getHeight(); y++) {
        		double[] c = colors.get((int) Math.round(x/image.getWidth()));
        		Color color = Color.color(c[0], c[1], c[2]);
            	writer.setColor(x, y, color);
        	}
        }
        gradient.setImage(image);
	}
	
	public void function(){
		System.out.println("Hej");
	}
	
	public final String getIconLocation(){
		return gradientLocation.getValue();
	}
	public final void setIconLocation(String value){
		gradientLocation.setValue(value);
	}
	public final StringProperty iconLocationProperty(){
		return gradientLocation;
	}
}
