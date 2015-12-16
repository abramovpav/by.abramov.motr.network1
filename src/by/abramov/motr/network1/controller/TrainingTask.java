package by.abramov.motr.network1.controller;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import by.abramov.motr.network1.model.Network;
import by.abramov.motr.network1.util.Utils;

public class TrainingTask implements Runnable {
		
		private boolean canceled = false;
		private double error = Double.MAX_VALUE;
		private double minError;
		private int n;
		private int neuronCount;
		private double trainRatio;
		private List<double[]> data;
		private List<double[]> newData;
		private ImageView outputImage;
		
		public TrainingTask(double minError, int n, int neuronCount, List<double []> data, 
				double trainRatio, ImageView outputImage) {
			this.n = n;
			this.neuronCount = neuronCount;
			this.data = data;
			this.minError = minError;
			this.trainRatio = trainRatio;
			this.outputImage = outputImage;
		}
		
		public void cancel() {
			this.canceled = true;
		}
		
		private boolean isCanceled() {
			return this.canceled;
		}
		
		
	    public void run() {
	    	
	    	Network network = new Network(n, neuronCount, data);
	    	int iteration = 0;

	        while(error > minError) {
	        	iteration++;
		        if (isCanceled()) {
		        	System.out.println("StopTask. Iteration = " + iteration + ", Error = " + error);
		        	newData = network.getImageData(data);
		        	System.out.println(newData.size());
		        	System.out.println(newData.get(0)[0]);
		        	System.out.println(newData.get(0).length);
		        	Image nimage = Utils.restoreImageData(newData, 8, 8, 256, 256);
		        	System.out.println();
		        	outputImage.setImage(nimage);
		        	break;
		        }
		        	
		        double y[] = new double[neuronCount];
		        double newX[] = new double[n];
		        double gamma[] = new double[neuronCount];
		        
		        double [][] firstLayer = network.getFirstLayer();
		        double [][] secondLayer = network.getSecondLayer();
		        	
		        for (double x[]: data) {
		        
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
	                    	firstLayer[i][j] -= trainRatio * gamma[j] * x[i];
	                    	secondLayer[j][i] -= trainRatio * (newX[i] - x[i]) * y[j];
	                    }
	                }
	                network.normalizeFirstLayer();
	                network.normalizeSecondLayer();

		        }
		        error = 0;
		        for (double [] x: data) {
	                network.firstLayerProcess(x, y);
	                network.secondLayerProcess(y, newX);
	                error += getError(x, newX);
	            }
		        
		        if (iteration % 10 == 0) {
		        	System.out.println("Iteration = " + iteration + ", Error = " + error);
		        }
		        	
	        }
	        System.out.println("Iteration = " + iteration + ", Error = " + error);
	        newData = network.getImageData(data);
	    }
	    
	    public List<double[]> getNewData() {
	    	return newData;
	    }
	    
	    private double getError(double[] x, double[] newX) {

	        float e = 0;
	        for (int i = 0; i < x.length; ++i) {
	            e += Math.pow(newX[i] - x[i], 2);
	        }
	        
	    	return e;
	    }
}