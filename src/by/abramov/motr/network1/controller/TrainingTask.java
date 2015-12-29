package by.abramov.motr.network1.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import by.abramov.motr.network1.model.Network;
import by.abramov.motr.network1.model.Storage;
import by.abramov.motr.network1.util.Utils;

public class TrainingTask implements Runnable {
		
		private boolean canceled = false;
		public float errorToShow = Float.MAX_VALUE;
		public boolean stopped = false;
		private float error = Float.MAX_VALUE;
		private float minError;
		private int n;
		private int neuronCount;
		private float trainStep;
		private List<float[]> data;
		private List<float[]> newData;
		private ImageView outputImage;
		public Network network;
		public int iteration = 0;
		
		public TrainingTask(float minError, int n, int neuronCount, List<float []> data, 
				float trainRatio, ImageView outputImage) {
			this.n = n;
			this.neuronCount = neuronCount;
			this.data = data;
			this.minError = minError;
			this.trainStep = trainRatio;
			this.outputImage = outputImage;
		}
		
		public void cancel() {
			this.canceled = true;
		}
		
		private boolean isCanceled() {
			return this.canceled;
		}
		
		
	    public void run() {
	    	
	    	network = new Network(n, neuronCount, data);
	    	outputImage.setImage(null);

	        while(error > minError) {
	        	
	        	float y[] = new float[neuronCount];
		        float newX[] = new float[n];
		        float gamma[] = new float[neuronCount];
		        
		        float [][] firstLayer = network.getFirstLayer();
		        float [][] secondLayer = network.getSecondLayer();
	        	
		        if (isCanceled()) {
		        	this.log(iteration, error, true);
		        	newData = network.getImageData(data);
		        	Image nimage = Utils.restoreImageData(newData, Storage.fragmentWidth, Storage.fragmentHeight, Storage.originWidth, Storage.originHeight);
		        	System.out.println();
		        	outputImage.setImage(nimage);
		        	stopped = true;
		        	break;
		        }
		        	
		        
		        	
		        for (float x[]: data) {
		        
			        network.firstLayerProcess(x, y);
			        network.secondLayerProcess(y, newX);
			        
			        for (int j = 0; j < neuronCount; ++j) {
	                    gamma[j] = 0;
	                    for (int i = 0; i < n; ++i) {
	                        gamma[j] += (newX[i] - x[i]) * secondLayer[j][i];
	                    }
	                }

	                for (int i = 0; i < n; ++i) {
	                    for (int j = 0; j < neuronCount; ++j) {
	                    	firstLayer[i][j] -= trainStep * gamma[j] * x[i];
	                    	secondLayer[j][i] -= trainStep * (newX[i] - x[i]) * y[j];
	                    }
	                }
	                network.normalizeFirstLayer();
	                network.normalizeSecondLayer();

		        }
		        error = 0;
		        for (float [] x: data) {
	                network.firstLayerProcess(x, y);
	                network.secondLayerProcess(y, newX);
	                error += getError(x, newX);
	            }
		        error /= 2;
		        iteration++;
		        errorToShow = error;        
		        	
	        }
	        this.log(iteration, error, true);
	        newData = network.getImageData(data);
	        stopped = true;
	    }
	    
	    private void log(int iteration, float error, boolean stop) {
	    	String message = "Iteration = " + iteration + ", Error = " + error;
	    	if (stop) {
	    		message = "Stop task. " + message;
	    	}
	    	System.out.println(message);
	    }
	    
	    public List<float[]> getNewData() {
	    	return newData;
	    }
	    
	    private float getError(float[] x, float[] newX) {

	        float e = 0;
	        for (int i = 0; i < x.length; ++i) {
	            e += Math.pow(newX[i] - x[i], 2);
	        }
	        
	    	return e;
	    }
}