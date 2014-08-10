package org.scenarioo.rest.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FallbackImageMarker {
	
	public byte[] getMarkedImage(final File screenshot) throws IOException {
		BufferedImage image = ImageIO.read(screenshot);
		Graphics2D g = image.createGraphics();
		
		addTransparentWhiteBackground(image, g);
		writeFallbackMessageInCenter(image, g);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();
	}
	
	private void addTransparentWhiteBackground(final BufferedImage image, final Graphics2D g) {
		Composite originalComposite = g.getComposite();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
		
		g.setComposite(composite);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		g.setComposite(originalComposite);
	}
	
	private void writeFallbackMessageInCenter(final BufferedImage image, final Graphics2D g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.PLAIN, 50));
		FontMetrics fontMetrics = g.getFontMetrics();
		
		drawLine("This is a fallback image!", g, fontMetrics, 0, image);
		drawLine("The image you were looking for", g, fontMetrics, 1, image);
		drawLine("does not exist.", g, fontMetrics, 2, image);
		drawLine("Therefore we show you a different", g, fontMetrics, 3, image);
		drawLine("image of the same page.", g, fontMetrics, 4, image);
	}
	
	private void drawLine(final String message, final Graphics2D g, final FontMetrics fontMetrics, final int i,
			final BufferedImage image) {
		Rectangle2D stringBounds = fontMetrics.getStringBounds(message, g);
		
		int x = (image.getWidth() / 2) - (int) stringBounds.getCenterX();
		int y = ((image.getHeight() - (int) stringBounds.getHeight()) / 2) - ((3 - i) * (int) stringBounds.getHeight())
				- (int) stringBounds.getCenterY();
		
		g.drawString(message, x, y);
	}
	
}
