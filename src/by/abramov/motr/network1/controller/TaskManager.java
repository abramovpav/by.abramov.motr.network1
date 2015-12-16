package by.abramov.motr.network1.controller;

import java.util.List;

import javafx.scene.image.ImageView;
import by.abramov.motr.network1.model.Network;

public class TaskManager {
	private TrainingTask  task;
	private Thread taskThread;
	
	public void runTask(int m, int n, int neuronCount, List<double []> imageData, double minError,
			ImageView outputImage) {
		
		task = new TrainingTask(minError, m * n * 3, neuronCount, imageData, 1.0E-4, outputImage);
		taskThread = new Thread(task);
		taskThread.start();
	}
	
	public List<double []> stopTask() {
		task.cancel();
		taskThread.interrupt();
		return task.getNewData();
		
	}
}
