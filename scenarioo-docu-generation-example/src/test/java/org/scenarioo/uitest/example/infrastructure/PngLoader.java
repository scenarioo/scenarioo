package org.scenarioo.uitest.example.infrastructure;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class PngLoader {
	
	private static final Logger LOGGER = Logger.getLogger(PngLoader.class);
	
	/**
	 * Load example screenshot image as a base64 encoded image.
	 */
	public static byte[] loadPngFile(final String fileName) {
		URL url = getImageUrl(fileName);
		
		if (url == null) {
			throw new IllegalArgumentException(
					"Simulated application does not provide screenshot for current application state, example screenshot not found:"
							+ "example/screenshots/" + fileName + ".png");
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
		try {
			return FileUtils.readFileToByteArray(screenshotFile);
		} catch (Exception e) {
			throw new RuntimeException("Could not write image: "
					+ screenshotFile.getAbsolutePath(), e);
		}
	}
	
	/**
	 * It is possible to define a special image for each build run. If no special image is defined, the default is used.
	 */
	private static URL getImageUrl(final String fileName) {
		URL url = PngLoader.class.getClassLoader().getResource("example/screenshots/" + fileName + "." + MultipleBuildsRule.getCurrentBuildName() + ".png");
		if(url == null) {
			url = PngLoader.class.getClassLoader().getResource("example/screenshots/" + fileName + ".png");
		} else {
			LOGGER.info("Specific image for build run " + MultipleBuildsRule.getCurrentBuildName() + " found: " + fileName);
		}
		return url;
	}
	
}
