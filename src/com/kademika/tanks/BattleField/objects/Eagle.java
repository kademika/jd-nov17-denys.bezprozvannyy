package com.kademika.tanks.BattleField.objects;

import com.kademika.tanks.interfaces.Destroyable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Eagle extends AbstractObjects implements Destroyable {

	BufferedImage img = null;
	
	public Eagle(int x, int y) {
		color = new Color(255, 255, 0);
		this.x = x;
		this.y = y;
		loadImage();
	}

	private void loadImage() {
		try {
	    	img = ImageIO.read(new File("eagle.png"));
	    	
	    } catch (IOException e) {
		    System.err.println("Couldn't load image");
	    }
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, x, y, 64, 64, null);
	}
}
