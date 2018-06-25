package com.thefractory.customcomponents;

import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

/**
 * DEPRECATED
 * @author ivarc
 *
 */
public class InfoIcon extends StackPane {

    @FXML private Tooltip tip;

    public InfoIcon() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InfoIcon.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void showTip(){
        //Show a more detailed hint window.
        System.out.println("Not implemented!");
    }
    
    
    public final void setTipText(String value) {
        tip.textProperty().set(value);
    }

    public final String getTipText() {
        return tip.getText();
    }

    public final StringProperty tipTextProperty() {
        return tip.textProperty();
    }
}