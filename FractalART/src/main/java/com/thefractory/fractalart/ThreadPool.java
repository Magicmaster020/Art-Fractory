package com.thefractory.fractalart;

import java.util.ArrayList;

import com.thefractory.fractalart.utils.EnhancedTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThreadPool {

	private static ThreadPool threadPool = null;
	private Thread[] threads = new Thread[4];
	
	ObservableList<EnhancedTask> taskList 
			= FXCollections.observableArrayList(new ArrayList<EnhancedTask>());
	
	private ThreadPool() {
		
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread();
		}
	}
	
	public static ThreadPool getInstance() {
		if(threadPool == null) {
			threadPool = new ThreadPool();
		}
		return threadPool;
	}
	
	public void addTask(EnhancedTask task) {
		taskList.add(task);
	}
	
	public boolean removeTask(EnhancedTask task) {
		boolean success = taskList.remove(task);
		return success;
	}
	
	public boolean cancelTask(EnhancedTask task) {
		boolean success = removeTask(task);
		
		if(!success) {
			for(Thread thread : threads) {
				thread.ca
			}
		}
		
		return success;
	}
}
