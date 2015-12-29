package by.abramov.motr.network1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.abramov.motr.network1.controller.TrainingTask;

public class Network {
	private final int neuronCount;
	private final int n; 
	
	private float[][] firstLayerMatrix;
	private float[][] secondLayerMatrix;
	 
	private TrainingTask task;
	private Thread taskThread; 
	
	

	
	public Network(int n, List<float []> inputVector) {
		super();
		this.n = n;
		this.neuronCount = 4;
		this.firstLayerMatrix = new float[n][this.neuronCount];
		this.secondLayerMatrix = new float[this.neuronCount][n];
		init();
	}
	
	public Network(int n, int neuronCount, List<float []> inputVector) {
		super();
		this.n = n;
		this.neuronCount = neuronCount;
		this.firstLayerMatrix = new float[n][this.neuronCount];
		this.secondLayerMatrix = new float[this.neuronCount][n];
		init();
	}
	
	private void init() {
		Random random = new Random(2701);
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.neuronCount; j++) {
				this.firstLayerMatrix[i][j] = this.secondLayerMatrix[j][i] = random.nextFloat();
			}
		}
		normalizeFirstLayer();
        normalizeSecondLayer();
	}
	
	public float[][] getFirstLayer() {
		return firstLayerMatrix;
	}
	
	public void setFirstLayer(float [][] firstLayer) {
		this.firstLayerMatrix = firstLayer;
	}
	
	public float[][] getSecondLayer() {
		return secondLayerMatrix;
	}
	public void setSecondLayer(float [][] secondLayer) {
		this.secondLayerMatrix = secondLayer;
	}
	
	public void firstLayerProcess(float[] x, float[] y) {
		for (int i = 0; i < neuronCount; i++) {
			y[i] = 0;
			for (int j = 0; j < n; j++) {
				y[i] += x[j] * this.firstLayerMatrix[j][i];
			}
		}
	}
	
	public void secondLayerProcess(float[] y, float[] newX) {
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
	
	public List<float []> getImageData(List<float []> inputData) {
		List<float []> list = new ArrayList<>();
		float y[] = new float[this.neuronCount];
		
		
		for (float[] x: inputData) {
			float newX[] = new float[n];
			firstLayerProcess(x, y);
			secondLayerProcess(y, newX);
			list.add(newX);
		}
		return list;
	}
	
	
	
	public void train() {
		
	}
	
	
	
	
	
	
}
