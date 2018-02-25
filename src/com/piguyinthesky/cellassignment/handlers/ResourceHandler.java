package com.piguyinthesky.cellassignment.handlers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceHandler {

	public Image[] loadImages(String[] imgPaths) {
		Image[] images = new Image[imgPaths.length];
		for (int i = 0; i < imgPaths.length; i++) {
			try {
				BufferedImage image = ImageIO
						.read(new File("/Users/alexandercai/Documents/Programming/eclipse-workspace/CellAssignment/res/"
								+ imgPaths[i]));
				images[i] = image;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return images;
	}

}
