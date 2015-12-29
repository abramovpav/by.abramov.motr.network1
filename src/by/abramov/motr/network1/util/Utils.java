package by.abramov.motr.network1.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Utils {
	private final static float DEFAULT_OPACITY = (float) 1.0;
	
	private static float encodeColorChannelValue(double value) {
		return (float)(value * 2 - 1);
	}
	
	private static float decodeColorChannelValue(double value) {
		return Math.min(Math.max((float)((value + 1) / 2), 0), 1);
	}
	
	public static List<float []> getImageData(Image image, int w, int h) {
		List<float []> imageData = new ArrayList<float []>();
		int width = (int)image.getWidth();
		int height = (int)image.getHeight();
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				float v[] = new float [w * h * 3];
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

	public static Image restoreImageData(List<float []> imageData, int w, int h, int width, int height) {
		WritableImage image = new WritableImage(width, height);
		int k = 0;
		for (int startY = 0; startY < height; startY += h) {
			if (startY + h > height) startY = height - h;
			for (int startX = 0; startX < width; startX += w) {
				if (startX + w > width) startX = width - w;
				
				float v[] = imageData.get(k++);
				int l = 0;
				for (int i = 0; i < h; i++) { 
					for (int j = 0; j < w; j++) {
						float red = decodeColorChannelValue(v[l++]);
						float green = decodeColorChannelValue(v[l++]);
						float blue = decodeColorChannelValue(v[l++]);
						image.getPixelWriter().setColor(startX + j, startY + i, new Color(red,  green,  blue, DEFAULT_OPACITY));
					}
				}
				
			}
		}		
		return image;
	}

}
