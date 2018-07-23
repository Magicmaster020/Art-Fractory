package com.thefractory.fractalart;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewArtworkDialog extends Stage {

	private Artwork theInatance = null;
	
	@FXML private BorderPane root;
	@FXML private TreeView<DescriptionPane> treeView;
	@FXML private StackPane descriptionPane;
	
	public NewArtworkDialog(Stage parentStage) {
		this.initOwner(parentStage);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setWidth(657);
		this.setResizable(false);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewArtworkDialog.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.setScene(new Scene(root));
        
        TreeItem<DescriptionPane> mandelbrotLeaf = new TreeItem<DescriptionPane>(new DescriptionPane(MandelbrotSet.class));
        TreeItem<DescriptionPane> complexIterator = new TreeItem<DescriptionPane>(new DescriptionPane(ComplexIterator.class));
        complexIterator.getChildren().addAll(
        		mandelbrotLeaf,
        		new TreeItem<DescriptionPane>(new DescriptionPane(SimpleMandelbrotSet.class)),
        		new TreeItem<DescriptionPane>(new DescriptionPane(Buffalo.class)),
        		new TreeItem<DescriptionPane>(new DescriptionPane(BurningShip.class)),
        		new TreeItem<DescriptionPane>(new DescriptionPane(Celtic.class)),
        		new TreeItem<DescriptionPane>(new DescriptionPane(Tricorn.class)));
        
        TreeItem<DescriptionPane> artworks = new TreeItem<DescriptionPane>(new DescriptionPane(Artwork.class));
        artworks.getChildren().addAll(
        		complexIterator,
        		new TreeItem<DescriptionPane>(new DescriptionPane(HofstadtersButterfly.class)),	
        		new TreeItem<DescriptionPane>(new DescriptionPane(Sandpiles.class))
        		);
        artworks.setExpanded(true);
        treeView.setRoot(artworks);
        
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
        	handleMouseClicked(event);
        };
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
        treeView.getSelectionModel().selectedItemProperty().addListener((c, oldValue, newValue) -> {
            if (newValue != null && !newValue.isLeaf()) {
            	newValue.getValue().newButton.setDisable(true);
            }
        });
        
        Platform.runLater(() -> treeView.getSelectionModel().select(mandelbrotLeaf));
		descriptionPane.getChildren().add(mandelbrotLeaf.getValue().descriptionPane);

		this.showAndWait();
	}
	
	private void handleMouseClicked(MouseEvent event) {
		Node node = event.getPickResult().getIntersectedNode();
		if(node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
			descriptionPane.getChildren().clear();
			descriptionPane.getChildren().add(((TreeItem<DescriptionPane>) treeView.getSelectionModel().getSelectedItem()).getValue().descriptionPane);
		}
	}
	
	private class DescriptionPane extends Label {

		private Class<? extends Artwork> artwork;
		
		@FXML private GridPane descriptionPane;
		@FXML private ImageView firstImage;
		@FXML private ImageView secondImage;
		@FXML private Label descriptionLabel;
		@FXML private Button newButton;
		
		private DescriptionPane(Class<? extends Artwork> artwork) {
			this.artwork = artwork;
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DescriptionPane.fxml"));
			fxmlLoader.setController(this);
	        fxmlLoader.setClassLoader(getClass().getClassLoader());
	        
	        try {
	            fxmlLoader.load();
	        } catch (IOException exception) {
	            throw new RuntimeException(exception);
	        }
	        	        
	        try {
				this.setText((String) artwork.getField("defaultName").get(null));
		        descriptionLabel.setText((String) artwork.getField("description").get(null));
		        firstImage.setImage((Image) artwork.getField("firstImage").get(null));
		        secondImage.setImage((Image) artwork.getField("secondImage").get(null));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
		@FXML public void newArtwork() {
        	try {
        		NewArtworkDialog.this.theInatance = artwork.newInstance();
				NewArtworkDialog.this.close();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
        }
	}
	
	public Artwork getInstance() {
		return theInatance;
	}
}
