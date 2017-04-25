package org.scenarioo.model.docu.feature.model.screenAnnotations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScreenRegion {

	private int x;
	private int y;
	private int width;
	private int height;

	/**
	 * Default constructor, only for use by JAXB.
	 */
	public ScreenRegion() {
	}

	/**
	 * @param x
	 *            distance from left end of the screenshot in pixels
	 * @param y
	 *            distance from top end of the screenshot in pixels
	 * @param width
	 *            width of the region in pixels
	 * @param height
	 *            height of the region in pixels
	 */
	public ScreenRegion(final int x, final int y, final int width, final int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

}
