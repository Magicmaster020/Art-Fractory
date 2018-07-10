package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.NumberField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ResolutionDialog extends Dialog<int[]> {

	@FXML private DialogPane dialogPane;
	@FXML private NumberField widthField;
	@FXML private NumberField heightField;
	@FXML private CheckBox preRatio;
	@FXML private ImageView preview;
	
	public ResolutionDialog(double proportions) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResolutionDialog.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //Setting up the DialogPane.
		dialogPane.getButtonTypes().add(ButtonType.OK);
		preRatio.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					heightField.setDisable(true);
					heightField.valueProperty().bind(widthField.valueProperty().multiply(proportions));
				} else {
					heightField.setDisable(false);
					heightField.valueProperty().unbind();
				}
			}
        });
		
		//Setting up the dialog.
		this.setDialogPane(dialogPane);
		this.setResultConverter(new Callback<ButtonType,int[]>(){
			@Override
			public int[] call(ButtonType param) {
				int[] ret = {ResolutionDialog.this.getImageWidth(), ResolutionDialog.this.getImageHeight()};
				return ret;
			}
		});
        this.setOnCloseRequest(new EventHandler<DialogEvent>() {
			@Override
			public void handle(DialogEvent event) {
				ResolutionDialog.this.close();
			}
        });
	}
	
	public void setImage(Image image) {
		preview.setImage(image);
	}
	
	public int getImageWidth() {
		return (int) Math.round(widthField.getValue());
	}
	
	public int getImageHeight() {
		return (int) Math.round(heightField.getValue());
	}
}
