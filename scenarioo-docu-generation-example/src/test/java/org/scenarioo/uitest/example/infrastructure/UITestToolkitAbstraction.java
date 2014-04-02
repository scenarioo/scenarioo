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

package org.scenarioo.uitest.example.infrastructure;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.StepHtml;
import org.scenarioo.model.docu.entities.StepMetadata;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.uitest.dummy.toolkit.UITestToolkit;

/**
 * Example implementation of an abstraction layer that wrapps the used UI testing toolkit.
 * 
 * That's the place where you would usually use the real testing toolkit API that you use for your UI tests.
 * 
 * This abstraction layer is helpful to separate your testing code from the used UI testing toolkit and to have the
 * ability to put some cross cutting testing concerns (like generation of documentation) inside.
 */
public class UITestToolkitAbstraction {
	
	private static final String TITLE_ELEMENT_ID = "pagetitle";
	private static final Details TIME_MEASUREMENTS;
	
	static {
		TIME_MEASUREMENTS = new Details();
		TIME_MEASUREMENTS.addDetail("timeSpan", "1223");
		TIME_MEASUREMENTS.addDetail("timeOffset", "12332");
	}
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
		saveStepWithScreenshot(screenshot, "failed");
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
				test.getScenario().getName(), stepIndex).getName());
		return stepDescription;
	}
	
	private Page createPage() {
		Page page = new Page(toolkit.getApplicationsState().getPageName());
		return page;
	}
	
	private StepMetadata createStepMetadata() {
		StepMetadata metadata = new StepMetadata();
		metadata.addDetail("Simulation Configuration", toolkit.getApplicationsState()
				.getCurrentSimulationConfiguration());
		metadata.addDetail("Call Tree", toolkit.getApplicationsState().getCallTree());
		metadata.addDetail("Huge Metadata Tree", createHugeMetadataTree());
		metadata.addDetail("Made By", "Scenarioo-Team");
		metadata.setVisibleText("Bla bla bla bla bla ... This is the visible text as generated from dummy test.");
		metadata.addDetail("List", createMetadataList());
		return metadata;
	}
	
	private ObjectList<String> createMetadataList() {
		ObjectList<String> objectList = new ObjectList<String>();
		for(int i = 0; i < 5; i++) {
			objectList.add("Listentry " + i);
		}
		return objectList;
	}

	// This is used to make sure the display mechanism can handle large amounts of data
	private Object createHugeMetadataTree() {
		return createMetadataWithDepth(5);
	}
	
	private ObjectTreeNode<String> createMetadataWithDepth(final int depth) {
		ObjectTreeNode<String> node = new ObjectTreeNode<String>("NodeItem" + depth);
		node.addDetail("timeMeasurements", TIME_MEASUREMENTS);
		if (depth > 1) {
			for (int i = 0; i < 4; i++) {
				node.addChild(createMetadataWithDepth(depth - 1));
			}
		}
		return node;
	}
	
	public void assertElementPresent(final String elementId) {
		toolkit.assertElementPresent(elementId);
	}
	
	public void assertElementNotPresent(final String elementId) {
		toolkit.assertElementNotPresent(elementId);
	}
	
	public void assertTextPresent(final String text) {
		toolkit.assertTextPresent(text);
	}
	
}
