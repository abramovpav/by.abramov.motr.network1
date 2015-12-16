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
		return Math.min(Math.max((value + 1) / 2, 0), 1);
	}
	
	public static List<double []> getImageData(Image image, int w, int h) {
		List<double []> imageData = new ArrayList<double []>();
		int width = (int)image.getWidth();
		int height = (int)image.getHeight();
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				double v[] = new double [w * h * 3];
				int k = 0;
				for (int i = 0; i < h; i++) { 
					for (int j = 0; j < w; j++) {
						Color color = image.getPixelReader().getColor(startX + j, startY + i);
						v[k++] = encodeColorChannelValue(color.getRed());
						v[k++] = encodeColorChannelValue(color.getGreen());
						v[k++] = encodeColorChannelValue(color.getBlue());
					}
				}
				imageData.add(v);
			}
		}		
		return imageData;
	}

	public static Image restoreImageData(List<double []> imageData, int w, int h, int width, int height) {
		WritableImage image = new WritableImage(width, height);
		int k = 0;
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				double v[] = imageData.get(k++);
				int l = 0;
				for (int i = 0; i < h; i++) { 
					for (int j = 0; j < w; j++) {
						double red = decodeColorChannelValue(v[l++]);
						double green = decodeColorChannelValue(v[l++]);
						double blue = decodeColorChannelValue(v[l++]);
						image.getPixelWriter().setColor(startX + i, startY + j, new Color(red,  green,  blue, DEFAULT_OPACITY));
					}
				}
				
			}
		}		
		return image;
	}

}
