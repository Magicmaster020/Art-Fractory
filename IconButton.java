package com.thefractory.customcomponents;

import java.io.IOException;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;

//https://stackoverflow.com/questions/43956201/javafx-how-to-remove-the-borders-from-button

public class IconButton extends Button{
	
    //Properties
    private final StringProperty iconLocation;
    
    //Structural Elements
    //@FXML private Button button;
	
	//private Tooltip tip; Implement this one also
	
	public IconButton(@NamedArg(value="iconLocation" , defaultValue="closebutton") String iconLocation){
		
		this.iconLocation = new SimpleStringProperty(this, "iconLocation", iconLocation);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IconButton.fxml"));
        fxmlLoader.setRoot(this);
        //fxmlLoader.setController(this);
        //fxmlLoader.setClassLoader(getClass().getClassLoader());

		System.out.println("blä");
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

		System.out.println("blä");
		
		Image image = new Image(getClass().getResourceAsStream("/icons/" + iconLocation + "/icon.png"));
		super.setGraphic(new ImageView(image));
	}
	
	public void function(){
		System.out.println("Hej");
	}
	
	public final String getIconLocation(){
		return iconLocation.getValue();
	}
	public final void setIconLocation(String value){
		iconLocation.setValue(value);
	}
	public final StringProperty iconLocationProperty(){
		return iconLocation;
	}
}
