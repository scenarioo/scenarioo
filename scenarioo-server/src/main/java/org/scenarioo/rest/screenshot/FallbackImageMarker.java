package org.scenarioo.rest.screenshot;

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

/**
 * Marks an image as a fallback image so that the user can see it's not the exact step he requested.
 */
public class FallbackImageMarker {
	
	public byte[] getMarkedImage(final File screenshot) throws IOException {
		BufferedImage image = ImageIO.read(screenshot);
		Graphics2D g = image.createGraphics();
		
		writeFallbackMessageInCenter(image, g);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();
	}
	
	private void addTransparentWhiteBackground(final BufferedImage image, final Graphics2D g, final int x, final int y,
			final Rectangle2D stringBounds) {
		Composite originalComposite = g.getComposite();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
		Color originalColor = g.getColor();
		
		g.setComposite(composite);
		g.setColor(Color.WHITE);
		g.fillRect(x, y, (int) stringBounds.getWidth(), (int) stringBounds.getHeight());
		
		g.setComposite(originalComposite);
		g.setColor(originalColor);
	}
	
	private void writeFallbackMessageInCenter(final BufferedImage image, final Graphics2D g) {
		g.setColor(Color.RED);
		int fontSize = image.getWidth() / 20;
		g.setFont(new Font("Arial", Font.PLAIN, fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();
		
		drawLine(g, fontMetrics, image, 0, "This is a fallback image!");
		drawLine(g, fontMetrics, image, 1, "The image you were looking for");
		drawLine(g, fontMetrics, image, 2, "does not exist.");
		drawLine(g, fontMetrics, image, 3, "Therefore we show you a different");
		drawLine(g, fontMetrics, image, 4, "image of the same page.");
	}
	
	private void drawLine(final Graphics2D g, final FontMetrics fontMetrics, final BufferedImage image, final int i,
			final String message) {
		Rectangle2D stringBounds = fontMetrics.getStringBounds(message, g);
		
		int x = (image.getWidth() / 2) - (int) stringBounds.getCenterX();
		int y = ((i + 2) * (int) stringBounds.getHeight());
		
		addTransparentWhiteBackground(image, g, x, y - fontMetrics.getAscent(), stringBounds);
		g.drawString(message, x, y);
	}
	
}
