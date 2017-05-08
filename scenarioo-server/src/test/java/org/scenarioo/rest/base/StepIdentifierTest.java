package org.scenarioo.rest.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.rest.step.logic.StepTestData;

import java.io.File;

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
		assertEquals("/rest/branch/bugfix-branch/build/build-2014-08-12/usecase/Find the answer/scenario/Actually find it/pageName/pageName1/pageOccurrence/0/stepInPageOccurrence/0/image.jpeg",
				stepIdentifier.getScreenshotUriForRedirect("jpeg").getPath());
		assertEquals("fallback=true", stepIdentifier.getScreenshotUriForRedirect(".jpeg").getQuery());
	}

	@Test
	public void redirectUrlForStep() {
		assertEquals(
				"/rest/branch/bugfix-branch/build/build-2014-08-12/usecase/Find the answer/scenario/Actually find it/pageName/pageName1/pageOccurrence/0/stepInPageOccurrence/0",
				stepIdentifier.getStepUriForRedirect().getPath());
		assertEquals("fallback=true", stepIdentifier.getStepUriForRedirect().getQuery());
	}

}
