package by.abramov.motr.network1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.abramov.motr.network1.controller.TrainingTask;

public class Network {
	private final int neuronCount;
	private final int n; 
	
	private double[][] firstLayerMatrix;
	private double[][] secondLayerMatrix;
	 
	private TrainingTask task;
	private Thread taskThread; 
	
	

	
	public Network(int n, List<double []> inputVector) {
		super();
		this.n = n;
		this.neuronCount = 4;
		this.firstLayerMatrix = new double[n][this.neuronCount];
		this.secondLayerMatrix = new double[this.neuronCount][n];
		init();
	}
	
	public Network(int n, int neuronCount, List<double []> inputVector) {
		super();
		this.n = n;
		this.neuronCount = neuronCount;
		this.firstLayerMatrix = new double[n][this.neuronCount];
		this.secondLayerMatrix = new double[this.neuronCount][n];
		init();
	}
	
	private void init() {
		Random random = new Random(2701);
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.neuronCount; j++) {
				this.firstLayerMatrix[i][j] = this.secondLayerMatrix[j][i] = random.nextDouble();
			}
		}
		normalizeFirstLayer();
        normalizeSecondLayer();
	}
	
	public double[][] getFirstLayer() {
		return firstLayerMatrix;
	}
	
	public void setFirstLayer(double [][] firstLayer) {
		this.firstLayerMatrix = firstLayer;
	}
	
	public double[][] getSecondLayer() {
		return secondLayerMatrix;
	}
	public void setSecondLayer(double [][] secondLayer) {
		this.secondLayerMatrix = secondLayer;
	}
	
	public void firstLayerProcess(double[] x, double[] y) {
		for (int i = 0; i < neuronCount; i++) {
			y[i] = 0;
			for (int j = 0; j < n; j++) {
				y[i] += x[j] * this.firstLayerMatrix[j][i];
			}
		}
	}
	
	public void secondLayerProcess(double[] y, double[] newX) {
		for (int i = 0; i < n; i++) {
			newX[i] = 0;
			for (int j = 0; j < neuronCount; j++) {
				newX[i] += y[j] * this.secondLayerMatrix[j][i];
			}
		}
	}
	
	public void normalizeSecondLayer() {
        for (int j = 0; j < neuronCount; ++j) {
            float sum = 0;
            for (int i = 0; i < n; ++i) {
                sum += Math.pow(secondLayerMatrix[j][i], 2);
            }
            sum = (float) Math.sqrt(sum);

            for (int i = 0; i < n; ++i) {
                secondLayerMatrix[j][i] /= sum;
            }
        }
    }

    public void normalizeFirstLayer() {
        for (int i = 0; i < n; ++i) {
            float sum = 0;
            for (int j = 0; j < neuronCount; ++j) {
                sum += Math.pow(firstLayerMatrix[i][j], 2);
            }
            sum = (float) Math.sqrt(sum);

            for (int j = 0; j < neuronCount; ++j) {
                firstLayerMatrix[i][j] /= sum;
            }
        }
    }
	
	public List<double []> getImageData(List<double []> inputData) {
		List<double []> list = new ArrayList<>();
		double y[] = new double[this.neuronCount];
		
		
		for (double[] x: inputData) {
			double newX[] = new double[n];
			firstLayerProcess(x, y);
			secondLayerProcess(y, newX);
			list.add(newX);
		}
		return list;
	}
	
	
	
	public void train() {
		
	}
	
	
	
	
	
	
}
