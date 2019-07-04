package org.scenarioo.uitest.example.infrastructure;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class StepImageLoader {

	private static final Logger LOGGER = Logger.getLogger(StepImageLoader.class);

	/**
	 * Load example screenshot image as a base64 encoded image.
	 *
	 * @param fileName image file name without extension
	 */
	public static DummyImageResource loadStepImageFile(final String fileName) {
		File screenshotFile = getImageFile(fileName);
		boolean png = screenshotFile.getName().endsWith(".png");

		try {
			byte[] bytes = FileUtils.readFileToByteArray(screenshotFile);
			return new DummyImageResource(bytes, png);
		} catch (Exception e) {
			throw new RuntimeException("Could not write image: "
				+ screenshotFile.getAbsolutePath(), e);
		}
	}

	/**
	 * It is possible to define a special image for each build run. If no special image is defined for curent build run, a build unrelated image is tried to be loaded (without build name in image file name).
	 */
	private static File getImageFile(final String fileName) {
		URL url = getImageUrl(fileName);
		if (url == null) {
			throw new IllegalArgumentException(
				"Simulated application does not provide screenshot for current application state, example screenshot not found:"
					+ "example/screenshots/" + fileName + ".png (or .jpeg)");
		}
		File screenshotFile;
		try {
			screenshotFile = new File(url.toURI());
			if (!screenshotFile.exists()) {
				throw new IllegalArgumentException(
					"Simulated application does not provide screenshot for current application state, file not found:"
						+ screenshotFile.getAbsolutePath());
			}
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(
				"Simulated application does not provide screenshot for current application state, file not found:"
					+ url, e);
		}
		return screenshotFile;
	}

	private static URL getImageUrl(final String fileName) {
		URL url = getImageUrl(fileName, ".png");
		if (url == null) {
			url = getImageUrl(fileName, ".jpg");
		}
		return url;
	}

	private static URL getImageUrl(final String fileName, String extension) {
		URL url = StepImageLoader.class.getClassLoader().getResource("example/screenshots/" + fileName + "." + MultipleBuildsRule.getCurrentBuildName() + extension);
		if (url == null) {
			url = StepImageLoader.class.getClassLoader().getResource("example/screenshots/" + fileName + extension);
		} else {
			LOGGER.info("Specific image for build run " + MultipleBuildsRule.getCurrentBuildName() + " found: " + fileName);
		}
		return url;
	}

}
