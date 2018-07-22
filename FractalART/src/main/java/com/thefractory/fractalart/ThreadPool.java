package com.thefractory.fractalart;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.thefractory.fractalart.utils.EnhancedCallable;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ThreadPool {

	private static ThreadPool threadPool = null;
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static Pane container;
	
	private ThreadPool() {}
	
	public static ThreadPool getInstance() {
		if(threadPool == null) {
			threadPool = new ThreadPool();
		}
		return threadPool;
	}
	
	public <T> Future<T> submit(EnhancedCallable<T> task, boolean showProgressBar){
		Future<T> future = executor.submit(task);
		if(container != null && showProgressBar) {
			new EnhancedProgressBar(task, future, container);
		}
		return future;
	}
	
	private class EnhancedProgressBar extends HBox {
		
		@FXML private Label description;
		@FXML private ProgressBar bar;
		private Future<?> future;

		private EnhancedProgressBar(EnhancedCallable<?> task, Future<?> future, Pane container) {
			this.future = future;
			
			//TODO Probably buggy.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
				        if(!future.isCancelled() && !future.isDone()) {
							loadBar(task, future, container);
				        }
					} catch(Exception e) {}
					
					try {
						future.get();
					} catch (InterruptedException | ExecutionException | CancellationException e) {
						//Do nothing.
					} finally {
						future.cancel(true);
						Platform.runLater(() -> {
							container.getChildren().remove(EnhancedProgressBar.this);
						});
					}
				}
			}).start();
		}
		
		private void loadBar(EnhancedCallable<?> task, Future<?> future, Pane container) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EnhancedProgressbar.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
	        fxmlLoader.setClassLoader(getClass().getClassLoader());
	        
	        try {
	            fxmlLoader.load();
	        } catch (IOException exception) {
	            throw new RuntimeException(exception);
	        }
	        
	        description.setText(task.getDescription());
	        bar.progressProperty().bind(task.progressProperty());
	        
			Platform.runLater(() -> {
				container.getChildren().add(EnhancedProgressBar.this);
			});
		}
	
		@FXML private void cancel() {
			future.cancel(true);
		}
	}

	public void setContainer(Pane container) {
		ThreadPool.container = container;
	}
}
