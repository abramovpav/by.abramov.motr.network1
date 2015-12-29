package by.abramov.motr.network1.controller;

import java.util.List;

import javafx.scene.image.ImageView;
import by.abramov.motr.network1.model.Network;
import by.abramov.motr.network1.model.Storage;

public class TaskManager {
	private TrainingTask  task;
	private Thread taskThread;
	
	public void runTask(int m, int n, int neuronCount, List<float []> imageData, float minError,
			float alpha, ImageView outputImage) {
		
		task = new TrainingTask(minError,  m * n * 3, neuronCount, imageData, alpha, outputImage);
		taskThread = new Thread(task);
		taskThread.start();
		Storage.task = task;
	}
	
	public List<float []> stopTask() {
		task.cancel();
		taskThread.interrupt();
		return task.getNewData();
		
	}
}
