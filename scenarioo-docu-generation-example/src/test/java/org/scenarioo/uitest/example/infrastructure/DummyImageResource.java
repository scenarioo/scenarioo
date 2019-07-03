package org.scenarioo.uitest.example.infrastructure;

/**
 * {@link DummyImageResource} contains the byte[] of an image as well as a flag to indicate whether it was a png image or not.
 */
public class DummyImageResource {

	private final byte[] screenshot;
	private final boolean png;

	public DummyImageResource(byte[] screenshot) {
		this(screenshot, true);
	}

	public DummyImageResource(byte[] screenshot, boolean png) {
		this.screenshot = screenshot;
		this.png = png;
	}

	public byte[] getScreenshot() {
		return screenshot;
	}

	public boolean isPng() {
		return png;
	}
}
