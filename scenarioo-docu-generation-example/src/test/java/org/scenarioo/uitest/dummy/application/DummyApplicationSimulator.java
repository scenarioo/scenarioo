/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.uitest.dummy.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.scenarioo.uitest.dummy.application.steps.DummyApplicationStepData;
import org.scenarioo.uitest.dummy.application.steps.DummyApplicationStepDataFactory;

/**
 * Just a simple fake application simulator for the example.
 * 
 * In your real application you would not simulate the whole application but
 * maybe some webservices, databases or other resources that your real
 * application needs.
 * 
 * Therefore you might even have something similar to this in your tests for
 * setting up the simulation configuration. But of course not simulating all
 * your application and UI screens.
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
		String fileName = getApplicationStepData(url, index)
				.getScreenshotFileName();
		return loadPngFile(fileName);
	}

	/**
	 * Load example screenshot image as a base64 encoded image.
	 */
	private String loadPngFile(final String fileName) {
		URL url = getClass().getClassLoader().getResource(
				"example/screenshots/" + fileName);
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
					"Simulated application does not provide screenshot for current application state, file not found:"
							+ url, e);
		}
		try {
			BufferedImage image = ImageIO.read(screenshotFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			final byte[] encodedScreenshot = Base64.encodeBase64(baos
					.toByteArray());
			return new String(encodedScreenshot);
		} catch (Exception e) {
			throw new RuntimeException("Could not write image: "
					+ screenshotFile.getAbsolutePath(), e);
		}

	}

	public String getBrowserUrl(final String url, final int index) {
		return getApplicationStepData(url, index).getBrowserUrl();
	}

	public ApplicationsStateData getApplicationsState(final String url,
			final int index) {
		return getApplicationStepData(url, index).getApplicationStateData();
	}

	public String getTextFromElement(final String url, final int index,
			final String elementId) {
		return getApplicationStepData(url, index).getTextFromElement(elementId);
	}

	private DummyApplicationStepData getApplicationStepData(final String url,
			final int index) {
		DummyApplicationStepData result = applicationStepData.get(new StepKey(
				url, index));
		if (result == null) {
			throw new IllegalArgumentException(
					"Simulation step data not found in dummy application implementation for current step: url = "
							+ url
							+ ", index = "
							+ index
							+ ", config = "
							+ DummyApplicationSimulator.getConfiguration());
		}
		return result;
	}

	private Map<StepKey, DummyApplicationStepData> createDummyApplicationData() {
		Map<StepKey, DummyApplicationStepData> dummyData = new HashMap<StepKey, DummyApplicationStepData>();
		for (DummyApplicationStepData stepData : DummyApplicationStepDataFactory
				.createDummyStepData()) {
			StepKey key = new StepKey(stepData.getSimulationConfig(),
					stepData.getStartBrowserUrl(), stepData.getIndex());
			dummyData.put(key, stepData);
		}
		return dummyData;
	}

	private static final class StepKey {
		private final DummySimulationConfig config;
		private final String url;
		private final int index;

		public StepKey(final String url, final int index) {
			this.config = DummyApplicationSimulator.getConfiguration();
			this.url = url;
			this.index = index;
		}

		public StepKey(final DummySimulationConfig config, final String url,
				final int index) {
			this.config = config;
			this.url = url;
			this.index = index;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((config == null) ? 0 : config.hashCode());
			result = prime * result + index;
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			StepKey other = (StepKey) obj;
			if (config != other.config) {
				return false;
			}
			if (index != other.index) {
				return false;
			}
			if (url == null) {
				if (other.url != null) {
					return false;
				}
			} else if (!url.equals(other.url)) {
				return false;
			}
			return true;
		}
	}

}
