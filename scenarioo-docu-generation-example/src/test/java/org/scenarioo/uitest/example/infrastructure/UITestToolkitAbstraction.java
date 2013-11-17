package org.scenarioo.uitest.example.infrastructure;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.StepHtml;
import org.scenarioo.model.docu.entities.StepMetadata;
import org.scenarioo.uitest.dummy.toolkit.UITestToolkit;


/**
 * Dummy example implementation of an abstraction layer that wrapps the used UI testing toolkit.
 * 
 * That's the place where you would usually use the real testing toolkit API that you use for your UI tests.
 * 
 * This abstraction layer is helpful to separate your testing code from the used UI testing toolkit and to have the
 * ability to put some cross cutting testing concerns (like generation of documentation) inside.
 */
public class UITestToolkitAbstraction {
	
	private static final String TITLE_ELEMENT_ID = "pagetitle";
	
	private UITestToolkit toolkit;
	
	private UITest test;
	
	private ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, EXAMPLE_BRANCH_NAME,
			EXAMPLE_BUILD_NAME);
	
	private String lastScreenshot = "";
	
	private int stepIndex = 0;
	
	public UITestToolkitAbstraction(final UITestToolkit toolkit, final UITest test) {
		this.toolkit = toolkit;
		this.test = test;
	}
	
	public UITest getTest() {
		return test;
	}
	
	public ScenarioDocuWriter getDocuWriter() {
		return docuWriter;
	}
	
	public void loadUrl(final String url) {
		toolkit.loadPage(url);
		saveStepWithScreenshot();
	}
	
	public void enterText(final String textFieldId, final String text) {
		toolkit.type(textFieldId, text);
	}
	
	public void clickButton(final String buttonId) {
		saveStepWithScreenshotIfChanged();
		toolkit.click(buttonId);
		saveStepWithScreenshot();
	}
	
	public void clickLink(final String linkText) {
		saveStepWithScreenshotIfChanged();
		toolkit.clickLinkWithText(linkText);
		saveStepWithScreenshot();
	}
	
	/**
	 * Save current step with screenshot, if the current screen is different than on last screenshot.
	 */
	public void saveStepWithScreenshotIfChanged() {
		String screenshot = toolkit.takeScreenshot();
		if (!lastScreenshot.equals(screenshot)) {
			saveStepWithScreenshot(screenshot, "success");
		}
	}
	
	/**
	 * Save current step with screenshot, in any case
	 */
	public void saveStepWithScreenshot() {
		String screenshot = toolkit.takeScreenshot();
		saveStepWithScreenshot(screenshot, "success");
	}
	
	/**
	 * Save current step with screenshot on error (status will be "error".
	 */
	public void saveStepErrorWithScreenshot() {
		String screenshot = toolkit.takeScreenshot();
		saveStepWithScreenshot(screenshot, "error");
	}
	
	private void saveStepWithScreenshot(final String screenshot, final String status) {
		
		// Save step
		String useCaseName = test.getUseCase().getName();
		String scenarioName = test.getScenario().getName();
		Step step = createStep(status);
		docuWriter.saveStep(useCaseName, scenarioName, step);
		
		// Save screenshot
		docuWriter.saveScreenshot(useCaseName, scenarioName, stepIndex, screenshot);
		
		// increase step index
		stepIndex++;
	}
	
	private Step createStep(final String status) {
		Step step = new Step();
		step.setStepDescription(createStepDescription(status));
		step.setPage(createPage());
		step.setMetadata(createStepMetadata());
		step.setHtml(new StepHtml(toolkit.getHtmlSource()));
		return step;
	}
	
	private StepDescription createStepDescription(final String status) {
		StepDescription stepDescription = new StepDescription();
		stepDescription.setTitle(toolkit.getTextFromElement(TITLE_ELEMENT_ID));
		stepDescription.setStatus(status);
		stepDescription.setIndex(stepIndex);
		stepDescription.addDetails("url", toolkit.getBrowserUrl());
		stepDescription.setScreenshotFileName(docuWriter.getScreenshotFile(test.getUseCase().getName(),
				test.getScenario()
						.getName(), stepIndex).getName());
		return stepDescription;
	}
	
	private Page createPage() {
		Page page = new Page(toolkit.getApplicationsState().getPageName());
		return page;
	}
	
	private StepMetadata createStepMetadata() {
		StepMetadata metadata = new StepMetadata();
		metadata.addDetail("simulationConfiguration", toolkit.getApplicationsState()
				.getCurrentSimulationConfiguration());
		metadata.addDetail("callTree", toolkit.getApplicationsState().getCallTree());
		metadata.setVisibleText("Bla bla bla bla bla ... This is the visible text as generated from dummy test.");
		return metadata;
	}
	
	public void assertElementPresent(final String string) {
		// dummy implementation: never fails
	}
	
	public void assertElementNotPresent(final String string) {
		// dummy implementation: never fails
	}
	
	public void assertTextPresent(final String string) {
		// dummy implementation: never fails
	}
	
}
