package org.scenarioo.uitest.dummy.toolkit;

import org.scenarioo.uitest.dummy.application.ApplicationsStateData;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.example.infrastructure.UITestToolkitAbstraction;

/**
 * Dummy implementation of a UI testing toolkit (could be Selenium for example).
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
	
}
