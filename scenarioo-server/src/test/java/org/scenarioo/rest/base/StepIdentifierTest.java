package org.scenarioo.rest.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.step.logic.StepTestData;

public class StepIdentifierTest {
	
	private BuildIdentifier buildIdentifier;
	private ScenarioIdentifier scenarioIdentifier;
	private StepIdentifier stepIdentifier;
	
	@Before
	public void setupTest() {
		buildIdentifier = new BuildIdentifier(StepTestData.BRANCH_NAME_VALID, StepTestData.BUILD_NAME_VALID);
		scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, StepTestData.USECASE_NAME_VALID,
				StepTestData.SCENARIO_NAME_VALID);
		stepIdentifier = new StepIdentifier(scenarioIdentifier, StepTestData.PAGE_NAME_VALID_1, 0, 0);
	}
	
	@Test
	public void redirectUrlForScreenshot() {
		assertEquals(
				"/rest/branch/bugfix-branch/build/build-2014-08-12/usecase/Find the answer/scenario/Actually find it/pageName/pageName1/pageOccurrence/0/stepInPageOccurrence/0.png",
				stepIdentifier.getScreenshotUriForRedirect().getPath());
		assertEquals("fallback=true", stepIdentifier.getScreenshotUriForRedirect().getQuery());
	}
	
	@Test
	public void redirectUrlForStep() {
		assertEquals(
				"/rest/branch/bugfix-branch/build/build-2014-08-12/usecase/Find the answer/scenario/Actually find it/pageName/pageName1/pageOccurrence/0/stepInPageOccurrence/0",
				stepIdentifier.getStepUriForRedirect().getPath());
		assertEquals("fallback=true", stepIdentifier.getStepUriForRedirect().getQuery());
	}
	
}
