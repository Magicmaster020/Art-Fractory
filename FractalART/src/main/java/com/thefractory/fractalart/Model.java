package com.thefractory.fractalart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import com.thefractory.fractalart.test.ArtworkTest;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Model {

	public final static ThreadPool THREAD_POOL= ThreadPool.getInstance();
	private static Model model = new Model();
	
	private Model() {Artwork.setThreadPool(THREAD_POOL);}
	
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
		fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(1));
		File outputFile = fileChooser.showSaveDialog(ArtworkTest.primaryStage);
		
		//Making resolution dialog.
		ResolutionDialog resolutionDialog = new ResolutionDialog(artwork.getRightPane().getProportions());
		resolutionDialog.setImage(artwork.getImage());
		Optional<int[]> resolution = resolutionDialog.showAndWait();
		
		//Saving the image.
		new Thread(new Runnable() {
			@Override
			public void run() {
				int width = (int)resolution.get()[0];
				int height = (int)resolution.get()[1];
				if(outputFile != null) {
					String name = outputFile.getName();
				    String extension = name.substring(1 + name.lastIndexOf(".")).toLowerCase();
					try {
						Future<WritableImage> future = THREAD_POOL.submit(artwork.getImageTask(width,height), true);
						WritableImage image = null;
						try {
							image = future.get();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					switch (extension) {
						case "tiff":
						case "tif":
							ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
							System.out.println("saved");
							break;
						case "png":
							ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
							System.out.println("saved");
							break;
						case "jpg":
						case "jpeg":
						case "jpe":
						case "jfif":
							BufferedImage bImage =SwingFXUtils.fromFXImage(image, null);
							BufferedImage bImage2 = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
							bImage2.getGraphics().drawImage(bImage, 0, 0, null);
							ImageIO.write(bImage2, "jpg", outputFile);
							System.out.println("saved");
							break;
						case "gif":
							ImageIO.write(SwingFXUtils.fromFXImage(image, null), "gif", outputFile);
							System.out.println("saved");
							break;
						default: 
							System.out.println("Unsupported extension");
							break;
						}
					} catch(IOException e) {
						System.out.println("Saving error");
					}
				}
			}
		}).start();
	}
}
