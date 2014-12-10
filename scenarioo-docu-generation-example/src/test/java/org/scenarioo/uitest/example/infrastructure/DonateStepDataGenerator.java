package org.scenarioo.uitest.example.infrastructure;

import static org.scenarioo.api.util.IdentifierSanitizer.*;
import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;

/**
 * This is a new generator that does not try to emulate an actual UI testing framework. Unlike
 * {@link UITestToolkitAbstraction} it is more straight forward and it's only purpose is a simple generation of test
 * data.
 */
public class DonateStepDataGenerator {
	
	private UITest uiTest;
	private int stepIndex = 0;
	
	private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, EXAMPLE_BRANCH_NAME,
			MultipleBuildsRule.getCurrentBuildName());
	
	public DonateStepDataGenerator(UITest uiTest) {
		this.uiTest = uiTest;
	}
	
	public void openWikipediaHomePage() {
		saveStepWithScreenshot("0_home", "startSearch.jsp", "startSearch.jsp", "startSearch.jsp");
	}
	
	public void clickGermanLink() {
		saveStepWithScreenshot("1_home_german", "portal.jsp", null, null);
	}
	
	public void enterTextThanks() {
		saveStepWithScreenshot("2_home_thanks", "portal.jsp", "startSearch.jsp", null);
	}
	
	public void enterTextIWantToDonate() {
		saveStepWithScreenshot("3_home_donate", "portal.jsp", "startSearch.jsp", "startSearch.jsp");
	}
	
	public void selectLanguageEnglish() {
		saveStepWithScreenshot("4_home_english", null, "startSearch.jsp", "startSearch.jsp");
	}
	
	public void clickSearchButtonToSeeResults() {
		saveStepWithScreenshot("5_results_donate", "searchResults.jsp", "searchResults.jsp", "searchResults.jsp");
	}
	
	public void clickDonateLinkToSeeDonatePage() {
		saveStepWithScreenshot("6_donate", "donate.jsp", "donate.jsp", "donate.jsp");
	}
	
	public void clickPrivacyPolicyToSeePrivacyPolicy() {
		saveStepWithScreenshot("7_privacy_policy", null, null, "privacyPolicy.jsp");
	}
	
	public void clickSwissGermanLink() {
		saveStepWithScreenshot("8_donate_swiss_german", "donate.jsp", null, null);
	}
	
	private void saveStepWithScreenshot(String fileName, String pageNameJanuary, String pageNameFebruary, String pageNameMarch) {
		String pageName = getPageNameForBuildRun(pageNameJanuary, pageNameFebruary, pageNameMarch);
		if(pageName == null) {
			// a page name of null means that this step does not exist in a certain build run.
			return;
		}
		byte[] screenshot = PngLoader.loadPngFile("donate/" + fileName);
		saveStepWithScreenshot(screenshot, pageName);
	}
	
	private void saveStepWithScreenshot(final byte[] screenshot, String pageName) {
		String useCaseName = uiTest.getUseCase().getName();
		String scenarioName = uiTest.getScenario().getName();
		Step step = createStep(pageName);
		docuWriter.saveStep(useCaseName, scenarioName, step);
		docuWriter.saveScreenshotAsPng(useCaseName, scenarioName, stepIndex, screenshot);
		stepIndex++;
	}
	
	private String getPageNameForBuildRun(String pageNameJanuary, String pageNameFebruary, String pageNameMarch) {
		if (BuildRun.JANUARY.equals(MultipleBuildsRule.getCurrentBuildRun())) {
			return pageNameJanuary;
		} else if (BuildRun.FEBRUARY.equals(MultipleBuildsRule.getCurrentBuildRun())) {
			return pageNameFebruary;
		} else if (BuildRun.MARCH.equals(MultipleBuildsRule.getCurrentBuildRun())) {
			return pageNameMarch;
		} else {
			throw new RuntimeException("Illegal build run " + MultipleBuildsRule.getCurrentBuildRun());
		}
	}
	
	private Step createStep(String pageName) {
		Step step = new Step();
		step.setStepDescription(createStepDescription());
		step.setPage(createPage(pageName));
		return step;
	}
	
	private StepDescription createStepDescription() {
		StepDescription stepDescription = new StepDescription();
		stepDescription.setStatus(Status.SUCCESS);
		stepDescription.setIndex(stepIndex);
		return stepDescription;
	}
	
	private Page createPage(String pageName) {
		return new Page(sanitize(pageName));
	}

	public void flush() {
		docuWriter.flush();
	}
	
}
