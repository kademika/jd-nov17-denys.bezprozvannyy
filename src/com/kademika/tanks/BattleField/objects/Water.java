package com.kademika.tanks.BattleField.objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Water extends AbstractObjects {

	BufferedImage img = null;
	
	public Water(int x, int y) {
	color = new Color(0, 0, 255);
	this.x = x;
	this.y = y;
	loadImage();
	}
	
	public void destroy() {
	}
	
	private void loadImage() {
		try {
	    	img = ImageIO.read(new File("water4.png"));
	    	
	    } catch (IOException e) {
		    System.err.println("Couldn't load image");
	    }
	}

	@Override
	public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Composite comp = g2d.getComposite();
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5);
        g2d.setComposite(alpha);
        g2d.drawImage(img, x, y, 64, 64, null);
        g2d.setComposite(comp);
	}
}
