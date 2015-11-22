package by.abramov.motr.network1.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Utils {
	private final static double DEFAULT_OPACITY = 1.0;
	
	private static double encodeColorChannelValue(double value) {
		return value * 2 - 1;
	}
	
	private static double decodeColorChannelValue(double value) {
		return (value + 1) / 2;
	}
	
	public static List<Double> getImageData(Image image, int w, int h) {
		List<Double> imageData = new ArrayList<Double>();
		int width = (int)image.getWidth();
		int height = (int)image.getHeight();
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				for (int i = 0; i < h; i++) { 
					for (int j = 0; j < w; j++) {
						Color color = image.getPixelReader().getColor(startX + j, startY + i);
						imageData.add(encodeColorChannelValue(color.getRed()));
						imageData.add(encodeColorChannelValue(color.getGreen()));
						imageData.add(encodeColorChannelValue(color.getBlue()));
					}
				}
				
			}
		}		
		return imageData;
	}

	public static Image restoreImageData(List<Double> imageData, int w, int h, int width, int height) {
		WritableImage image = new WritableImage(width, height);
		int k = 0;
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				for (int i = 0; i < h; i++) { 
					for (int j = 0; j < w; j++) {
						double red = decodeColorChannelValue(imageData.get(k++));
						double green = decodeColorChannelValue(imageData.get(k++));
						double blue = decodeColorChannelValue(imageData.get(k++));
						image.getPixelWriter().setColor(startX + i, startY + j, new Color(red,  green,  blue, DEFAULT_OPACITY));
					}
				}
				
			}
		}		
		return image;
	}

}
