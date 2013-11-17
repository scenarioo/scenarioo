package org.scenarioo.uitest.dummy.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.codec.binary.Base64;
import org.scenarioo.uitest.dummy.application.steps.DummyApplicationStepData;
import org.scenarioo.uitest.dummy.application.steps.DummyApplicationStepDataFactory;


/**
 * Just a simple fake application simulator for the example.
 * 
 * In your real application you would not simulate the whole application but maybe some webservices, databases or other
 * resources that your real application needs.
 * 
 * Therefore you might even have something similar to this in your tests for setting up the simulation configuration.
 * But of course not simulating all your application and UI screens.
 * 
 * Example application screenshots taken from http://www.wikipedia.org/
 */
public class DummyApplicationSimulator {
	
	private static final ThreadLocal<DummySimulationConfig> currentConfiguration = new ThreadLocal<DummySimulationConfig>();
	
	public static void setConfiguration(final DummySimulationConfig config) {
		currentConfiguration.set(config);
	}
	
	public static DummySimulationConfig getConfiguration() {
		return currentConfiguration.get();
	}
	
	public Map<StepKey, DummyApplicationStepData> applicationStepData = createDummyApplicationData();
	
	public String getScreenshot(final String url, final int index) {
		String fileName = getApplicationStepData(url, index).getScreenshotFileName();
		return loadPngFile(fileName);
	}
	
	/**
	 * Load example screenshot image as a base64 encoded image.
	 */
	private String loadPngFile(final String fileName) {
		URL url = getClass().getClassLoader().getResource("example/screenshots/" + fileName);
		if (url == null) {
			throw new IllegalArgumentException(
					"Simulated application does not provide screenshot for current application state, example screenshot not found:"
							+ "example/screenshots/" + fileName);
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
					"Simulated application does not provide screenshot for current application state, file not found:" +
							url, e);
		}
		try {
			BufferedImage image = ImageIO.read(screenshotFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			final byte[] encodedScreenshot = Base64.encodeBase64(baos.toByteArray());
			return new String(encodedScreenshot);
		} catch (Exception e) {
			throw new RuntimeException("Could not write image: " + screenshotFile.getAbsolutePath(), e);
		}
		
	}
	
	public String getBrowserUrl(final String url, final int index) {
		return getApplicationStepData(url, index).getBrowserUrl();
	}
	
	public ApplicationsStateData getApplicationsState(final String url, final int index) {
		return getApplicationStepData(url, index).getApplicationStateData();
	}
	
	public String getTextFromElement(final String url, final int index, final String elementId) {
		return getApplicationStepData(url, index).getTextFromElement(elementId);
	}
	
	private DummyApplicationStepData getApplicationStepData(final String url, final int index) {
		DummyApplicationStepData result = applicationStepData.get(new StepKey(url, index));
		if (result == null) {
			throw new IllegalArgumentException(
					"Simulation step data not found in dummy application implementation for current step: url = " + url
							+ ", index = " + index + ", config = " + DummyApplicationSimulator.getConfiguration());
		}
		return result;
	}
	
	private Map<StepKey, DummyApplicationStepData> createDummyApplicationData() {
		Map<StepKey, DummyApplicationStepData> dummyData = new HashMap<StepKey, DummyApplicationStepData>();
		for (DummyApplicationStepData stepData : DummyApplicationStepDataFactory.createDummyStepData()) {
			StepKey key = new StepKey(stepData.getSimulationConfig(), stepData.getStartBrowserUrl(),
					stepData.getIndex());
			dummyData.put(key, stepData);
		}
		return dummyData;
	}
	
	@Data
	@EqualsAndHashCode
	private static final class StepKey {
		private DummySimulationConfig config;
		private String url;
		private int index;
		
		public StepKey(final String url, final int index) {
			this.config = DummyApplicationSimulator.getConfiguration();
			this.url = url;
			this.index = index;
		}
		
		public StepKey(final DummySimulationConfig config, final String url, final int index) {
			this.config = config;
			this.url = url;
			this.index = index;
		}
	}
	
}
