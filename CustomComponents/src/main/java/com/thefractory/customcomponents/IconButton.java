package com.thefractory.customcomponents;

import java.io.IOException;
import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

//https://stackoverflow.com/questions/43956201/javafx-how-to-remove-the-borders-from-button

public class IconButton extends Button {
	
    //Properties
    private final StringProperty iconLocation;
	
	@FXML public Button button;
	@FXML private Tooltip tip;
	
	public IconButton(@NamedArg(value="iconLocation" , defaultValue="new") String iconLocation){
		this.iconLocation = new SimpleStringProperty(this, "iconLocation", iconLocation);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IconButton.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
		button.getStylesheets().add(getClass().getResource("icons/" + iconLocation + "/style.css").toString());
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
	public final String getTip() {
        return tip.getText();
}
	public final void setTip(String value) {
	        tip.setText(value);
	}
	public final StringProperty tipProperty() {
        return tip.textProperty();
}
}