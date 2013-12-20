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

package org.scenarioo.uitest.dummy.toolkit;

import org.scenarioo.uitest.dummy.application.ApplicationsStateData;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.example.infrastructure.UITestToolkitAbstraction;

/**
 * Dummy implementation of a UI testing toolkit (could be Selenium for example, or Robotium or SWTBot, or whatever UI
 * toolkit you like or use in your project).
 * 
 * You would replace this class by a real UI Testing API in your real UI tests and adopt the wrapper
 * {@link UITestToolkitAbstraction} accordingly.
 */
public class UITestToolkit {
	
	private DummyApplicationSimulator dummyApplicationSimulator = new DummyApplicationSimulator();
	
	private String currentUrl = "";
	
	private boolean interactionSinceLastScreenshotOrPageLoad = false;
	
	private int index = 0;
	
	public void loadPage(final String url) {
		// just a dummy implementation
		this.currentUrl = url;
		index = 0;
		interactionSinceLastScreenshotOrPageLoad = false;
	}
	
	/**
	 * Get current screenshot as base 64 encoded image.
	 */
	public String takeScreenshot() {
		// just a dummy implementation
		if (interactionSinceLastScreenshotOrPageLoad) {
			index++;
		}
		String screenshot = dummyApplicationSimulator.getScreenshot(currentUrl, index);
		interactionSinceLastScreenshotOrPageLoad = false;
		return screenshot;
	}
	
	public void type(final String elementId, final String text) {
		// just a dummy implementation
		interactionSinceLastScreenshotOrPageLoad = true;
	}
	
	public void click(final String elementId) {
		// just a dummy implementation
		interactionSinceLastScreenshotOrPageLoad = true;
	}
	
	public void clickLinkWithText(final String linkText) {
		interactionSinceLastScreenshotOrPageLoad = true;
	}
	
	public String getBrowserUrl() {
		// just a dummy implementation
		return dummyApplicationSimulator.getBrowserUrl(currentUrl, index);
	}
	
	/**
	 * This is very dependent on your application and on the used testing toolkit how you would implement getting some
	 * internal data out of your application. See {@link ApplicationsStateData} documentation for more information how
	 * this could be realized.
	 */
	public ApplicationsStateData getApplicationsState() {
		// just a dummy implementation
		return dummyApplicationSimulator.getApplicationsState(currentUrl, index);
	}
	
	public String getTextFromElement(final String elementId) {
		// just a dummy implementation
		return dummyApplicationSimulator.getTextFromElement(currentUrl, index, elementId);
	}
	
	public String getHtmlSource() {
		// just some dummy html (same for all pages, for simplicity)
		return "<html>\n<head>\n</head>\n<body>\n   <p>just some dummy html code</p>\n</body>\n</html>";
	}
	
	public void assertElementPresent(final String elementId) {
		// dummy implementation: never fails
	}
	
	public void assertElementNotPresent(final String elementId) {
		// dummy implementation: never fails
	}
	
	public void assertTextPresent(final String text) {
		// dummy implementation: never fails
	}
	
}
