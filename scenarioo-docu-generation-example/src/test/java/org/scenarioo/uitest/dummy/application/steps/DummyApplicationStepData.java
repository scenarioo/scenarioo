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

package org.scenarioo.uitest.dummy.application.steps;

import java.util.Map;

import org.scenarioo.uitest.dummy.application.ApplicationsStateData;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;

/**
 * Dummy example data for one step of the application to be simulated for the
 * example.
 */
public class DummyApplicationStepData {

	private DummySimulationConfig simulationConfig;
	private String startBrowserUrl;
	private int index;

	private String screenshotFileName;
	private String browserUrl;
	private Map<String, String> elementTexts;
	private ApplicationsStateData applicationStateData;

	public DummySimulationConfig getSimulationConfig() {
		return simulationConfig;
	}

	public void setSimulationConfig(DummySimulationConfig simulationConfig) {
		this.simulationConfig = simulationConfig;
	}

	public String getStartBrowserUrl() {
		return startBrowserUrl;
	}

	public void setStartBrowserUrl(String startBrowserUrl) {
		this.startBrowserUrl = startBrowserUrl;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getScreenshotFileName() {
		return screenshotFileName;
	}

	public void setScreenshotFileName(String screenshotFileName) {
		this.screenshotFileName = screenshotFileName;
	}

	public String getBrowserUrl() {
		return browserUrl;
	}

	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}

	public Map<String, String> getElementTexts() {
		return elementTexts;
	}

	public void setElementTexts(Map<String, String> elementTexts) {
		this.elementTexts = elementTexts;
	}

	public ApplicationsStateData getApplicationStateData() {
		return applicationStateData;
	}

	public void setApplicationStateData(
			ApplicationsStateData applicationStateData) {
		this.applicationStateData = applicationStateData;
	}

	public String getTextFromElement(final String elementId) {
		String text = elementTexts.get(elementId);
		if (text == null) {
			throw new IllegalArgumentException("UI element does not exist: "
					+ elementId);
		}
		return text;
	}

}
