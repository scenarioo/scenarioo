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

import static org.scenarioo.api.util.IdentifierSanitizer.*;
import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.StepHtml;
import org.scenarioo.model.docu.entities.StepMetadata;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenAnnotation;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenAnnotationClickAction;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenAnnotationStyle;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenRegion;
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

	private static final Logger LOGGER = Logger.getLogger(UITestToolkitAbstraction.class);

	private static final String TITLE_ELEMENT_ID = "pagetitle";
	private static final Details TIME_MEASUREMENTS;

	static {
		TIME_MEASUREMENTS = new Details();
		TIME_MEASUREMENTS.addDetail("timeSpan", "1223");
		TIME_MEASUREMENTS.addDetail("timeOffset", "12332");
	}
	private final UITestToolkit toolkit;

	private final UITest test;

	private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(SCENARIOO_DATA_DIRECTORY, MultipleBuildsRule.getCurrentBranchName(),
			MultipleBuildsRule.getCurrentBuildName());

	private byte[] lastScreenshot = new byte[0];

	private Step lastStep = null;

	private final List<ScreenAnnotation> screenAnnotations = new LinkedList<ScreenAnnotation>();

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
		addScreenAnnotation(textFieldId, ScreenAnnotationStyle.KEYBOARD, text, "Typed '" + text + "'",
				"User entered text '" + text + "'.");
		toolkit.type(textFieldId, text);
	}

	public void clickButton(final String buttonId) {
		addScreenAnnotation(buttonId, ScreenAnnotationStyle.CLICK, "", "Clicked Button",
				"User clicked on button.");
		saveStepWithScreenshotIfChanged();
		toolkit.click(buttonId);
		saveStepWithScreenshot();
	}

	private void addScreenAnnotation(final String elementId, final ScreenAnnotationStyle style,
			final String screenText,
			final String title, final String description) {
		addScreenAnnotation(elementId, style, screenText, title, description, null);
	}

	private void addScreenAnnotation(final String elementId, final ScreenAnnotationStyle style,
			final String screenText, final String title, final String description, final String clickActionUrl) {
		ScreenRegion region = toolkit.getElementRegion(elementId);
		if (region != null) {
			addScreenAnnotation(elementId, style, screenText, title, description, clickActionUrl, region);
		}
		else {
			LOGGER.warn("event on UI element with id='"
					+ elementId
					+ "' with undefined region in dummy data for screen snnotation --> no screen annotation is generated in scenarioo documentation data for this event");
		}
	}

	private void addScreenAnnotation(final String elementId, final ScreenAnnotationStyle style,
			final String screenText, final String title,
			final String description, final String clickActionUrl, final ScreenRegion region) {
		ScreenAnnotation annotation = new ScreenAnnotation(region.getX(), region.getY(), region.getWidth(),
				region.getHeight());
		annotation.setStyle(style);
		annotation.setScreenText(screenText);
		annotation.setTitle(title);
		annotation.setDescription(description);
		if (clickActionUrl != null) {
			annotation.setClickAction(ScreenAnnotationClickAction.TO_URL);
			annotation.setClickActionUrl(clickActionUrl);
			annotation.setClickActionText("Open external documentation for this element");
		}
		else if (style == ScreenAnnotationStyle.CLICK) {
			// Create click events with go to next step (for testing clickActions on annotations)
			annotation.setClickAction(ScreenAnnotationClickAction.TO_NEXT_STEP);
			// no clickActionText here for testing generic text for click actions
		}
		ObjectDescription elementDescription = new ObjectDescription("uiElement", elementId);
		elementDescription.addDetail("elementId", elementId);
		elementDescription
				.addDetail(
						"info",
						"this is just an example to demonstrate that also screen annotations can have objects as detail data attached, e.g. the description of a ui element that was interacted with");
		annotation.addDetail("element", elementDescription);
		screenAnnotations.add(annotation);
	}

	public void addScreenAnnotationOnSameStep(final String annotationText, final ScreenAnnotationStyle annotationStyle,
			final ScreenRegion region) {
		if (annotationStyle != null) {
		addScreenAnnotation(
				"elementWithText=" + annotationText /* just a dummy element id (not realistic) */,
				annotationStyle,
				annotationText,
				annotationStyle.name() + "-Annotation '" + annotationText + "'",
				"Just an annotation to demonstarte annotation of type '"
						+ annotationStyle
						+ "' with text '"
						+ annotationText
							+ "' and a link to an external documentation as clickAction.\nAll annotations are produced on same step here, just for demo purposes.",
					"https://en.wikipedia.org/wiki/" + annotationText, region);
		}
		else {
			addScreenAnnotation(
					"elementWithText=" + annotationText /* just a dummy element id (not realistic) */,
					annotationStyle,
					annotationText,
					"Annotation '" + annotationText + "'",
					"Just a default annotation with text '"
							+ annotationText
							+ "' and no special click action (styled slightly different, because of missing click action --> popup opens also when clicking annotation (not only on icon).\nAll annotations are produced on same step here, just for demo purposes.",
					null, region);
		}
		toolkit.clickLinkWithText(annotationText);
	}

	public void clickLink(final String linkText) {
		addScreenAnnotation("linkWithText=" + linkText /* just a dummy element id (not realistic) */,
				ScreenAnnotationStyle.CLICK, "", "Clicked Link '" + linkText + "'", "User clicked on link with text '"
						+ linkText + "'.");
		saveStepWithScreenshotIfChanged();
		toolkit.clickLinkWithText(linkText);
		saveStepWithScreenshot();
	}

	/**
	 * Save current step with screenshot, if the current screen is different than on last screenshot.
	 * Otherwise additional annotation data is attached to the last written step.
	 */
	public void saveStepWithScreenshotIfChanged() {
		byte[] screenshot = toolkit.takeScreenshot();
		if (!lastScreenshot.equals(screenshot)) {
			saveStepWithScreenshot(screenshot, "success");
		}
		else {
			appendScreenAnnotationsToLastStep();
		}
	}

	private void appendScreenAnnotationsToLastStep() {
		if (screenAnnotations.size() > 0) {
			lastStep.getScreenAnnotations().addAll(screenAnnotations);
			saveStepData(lastStep);
			screenAnnotations.clear();
		}
	}

	/**
	 * Save current step with screenshot, in any case
	 */
	public void saveStepWithScreenshot() {
		byte[] screenshot = toolkit.takeScreenshot();
		saveStepWithScreenshot(screenshot, "success");
	}

	/**
	 * Save current step with screenshot on error (status will be "error".
	 */
	public void saveStepErrorWithScreenshot() {
		byte[] screenshot = toolkit.takeScreenshot();
		saveStepWithScreenshot(screenshot, "failed");
	}

	private void saveStepData(final Step step) {
		String useCaseName = test.getUseCase().getName();
		String scenarioName = test.getScenario().getName();
		docuWriter.saveStep(useCaseName, scenarioName, step);
	}

	private void saveStepWithScreenshot(final byte[] screenshot, final String status) {
		String useCaseName = test.getUseCase().getName();
		String scenarioName = test.getScenario().getName();
		saveStepData(createStep(status));
		docuWriter.saveScreenshotAsPng(useCaseName, scenarioName, stepIndex, screenshot);
		lastScreenshot = screenshot;
		stepIndex++;
	}

	private Step createStep(final String status) {
		Step step = new Step();
		step.setStepDescription(createStepDescription(status));
		step.setPage(createPage());
		step.setMetadata(createStepMetadata());
		step.setHtml(new StepHtml(toolkit.getHtmlSource()));
		step.getScreenAnnotations().addAll(screenAnnotations);
		screenAnnotations.clear();
		lastStep = step;
		return step;
	}

	private StepDescription createStepDescription(final String status) {
		StepDescription stepDescription = new StepDescription();
		stepDescription.setTitle(toolkit.getTextFromElement(TITLE_ELEMENT_ID));
		stepDescription.setStatus(status);
		stepDescription.setIndex(stepIndex);
		stepDescription.addDetail("url", toolkit.getBrowserUrl());
		stepDescription.addLabel("step-label-" + stepIndex).addLabel("public");
		return stepDescription;
	}

	private Page createPage() {
		Page page = new Page(sanitize(toolkit.getApplicationsState().getPageName()));
		page.getLabels().addLabel("page-label1").addLabel("page-label2");
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
		for (int i = 0; i < 5; i++) {
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

	public void selectLanguage(final String language) {
		toolkit.selectLanguage(language);
	}

}
