package org.scenarioo.uitest.example.infrastructure;

import static org.scenarioo.api.util.IdentifierSanitizer.*;
import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import java.util.HashMap;
import java.util.Map;

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
	private Map<BuildRun, Map<String, String>> buildToPageName;

	private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(SCENARIOO_DATA_DIRECTORY, MultipleBuildsRule.getCurrentBranchName(),
			MultipleBuildsRule.getCurrentBuildName());

	public DonateStepDataGenerator(UITest uiTest) {
		this.uiTest = uiTest;
		initBuildToPageNameMap();
	}

	public void openWikipediaHomePage() {
		saveStepWithScreenshot("0_home");
	}

	public void clickGermanLink() {
		saveStepWithScreenshot("1_home_german");
	}

	public void enterTextThanks() {
		saveStepWithScreenshot("2_home_thanks");
	}

	public void enterTextIWantToDonate() {
		saveStepWithScreenshot("3_home_donate");
	}

	public void selectLanguageEnglish() {
		saveStepWithScreenshot("4_home_english");
	}

	public void clickSearchButtonToSeeResults() {
		saveStepWithScreenshot("5_results_donate");
	}

	public void clickDonateLinkToSeeDonatePage() {
		saveStepWithScreenshot("6_donate");
	}

	public void clickPrivacyPolicyToSeePrivacyPolicy() {
		saveStepWithScreenshot("7_privacy_policy");
	}

	public void clickSwissGermanLink() {
		saveStepWithScreenshot("8_donate_swiss_german");
	}

	public void flush() {
		docuWriter.flush();
	}

	private void saveStepWithScreenshot(String fileName) {
		String pageName = getPageNameForBuildRun(fileName);
		if (pageName == null) {
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

	private String getPageNameForBuildRun(String stepName) {
		Map<String, String> stepToPageName = buildToPageName.get(MultipleBuildsRule.getCurrentBuildRun());
		return stepToPageName.get(stepName);
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

	private void initBuildToPageNameMap() {

		Map<String, String> janStepToPageNameMap = getStepToPageNameMap(
				"startSearch.jsp",
				"portal.jsp",
				"portal.jsp",
				"portal.jsp",
				null,
				"searchResults.jsp",
				"donate.jsp",
				null,
				"donate.jsp");

		Map<String, String> febStepToPageNameMap = getStepToPageNameMap(
				"startSearch.jsp",
				null,
				"startSearch.jsp",
				"startSearch.jsp",
				"startSearch.jsp",
				"searchResults.jsp",
				"donate.jsp",
				null,
				null);

		Map<String, String> generalStepToPageNameMap = getStepToPageNameMap(
				"startSearch.jsp",
				null,
				null,
				"startSearch.jsp",
				"startSearch.jsp",
				"searchResults.jsp",
				"donate.jsp",
				"privacyPolicy.jsp",
				null);

		buildToPageName = new HashMap<BuildRun, Map<String, String>>();
		buildToPageName.put(BuildRun.JANUARY, janStepToPageNameMap);
		buildToPageName.put(BuildRun.FEBRUARY, febStepToPageNameMap);
		buildToPageName.put(BuildRun.MARCH, generalStepToPageNameMap);
		buildToPageName.put(BuildRun.APRIL, generalStepToPageNameMap);
		buildToPageName.put(BuildRun.MAY, generalStepToPageNameMap);
	}

	private Map<String, String> getStepToPageNameMap(String... pageNames) {
		int index = 0;
		Map<String, String> stepToPageName = new HashMap<String, String>();
		stepToPageName.put("0_home", pageNames[index++]);
		stepToPageName.put("1_home_german", pageNames[index++]);
		stepToPageName.put("2_home_thanks", pageNames[index++]);
		stepToPageName.put("3_home_donate", pageNames[index++]);
		stepToPageName.put("4_home_english", pageNames[index++]);
		stepToPageName.put("5_results_donate", pageNames[index++]);
		stepToPageName.put("6_donate", pageNames[index++]);
		stepToPageName.put("7_privacy_policy", pageNames[index++]);
		stepToPageName.put("8_donate_swiss_german", pageNames[index++]);
		return stepToPageName;
	}
}
