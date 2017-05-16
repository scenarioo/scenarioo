/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.business.diffViewer.comparator;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.dao.diffViewer.impl.DiffFiles;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

/**
 * Test cases for the step comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class StepComparatorTest {

	private static String USE_CASE_NAME = "useCase";
	private static String SCENARIO_NAME = "scenario";
	private static String PAGE_NAME_1 = "page1";
	private static String PAGE_NAME_2 = "page2";

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@Mock
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private ScreenshotComparator screenshotComparator;

	@InjectMocks
	private StepComparator stepComparator = new StepComparator(COMPARATOR_PARAMETERS);

	@BeforeClass
	public static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder.newFolder());
		assertTrue(DiffFiles.getDiffViewerDirectory().mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	public void testCompareBuildsEqual() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		assertEquals(0, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneStepAdded() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertEquals(new Integer(2), scenarioDiffInfo.getAddedElements().get(0));
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareMultipleStepsAdded() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(2, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertEquals(new Integer(1), scenarioDiffInfo.getAddedElements().get(0));
		assertEquals(new Integer(2), scenarioDiffInfo.getAddedElements().get(1));
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareStepChangedTo50Percentage() {
		double changeRatePerStep = 50.0;
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, changeRatePerStep);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = changeRatePerStep;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(3, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneStepRemoved() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(1, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertEquals(PAGE_NAME_1, scenarioDiffInfo.getRemovedElements().get(0).getStepLink().getPageName());
	}

	@Test
	public void testCompareMultipleStepsRemoved() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(2, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertEquals(PAGE_NAME_1, scenarioDiffInfo.getRemovedElements().get(0).getStepLink().getPageName());
		assertEquals(PAGE_NAME_2, scenarioDiffInfo.getRemovedElements().get(1).getStepLink().getPageName());
	}

	private void initMocks(List<Step> baseSteps, List<Step> comparisonSteps,
			double changeRate) {
		when(docuBuildsManager.resolveBranchAndBuildAliases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME))
				.thenReturn(new BuildIdentifier(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME));
		when(docuReader.loadSteps(BASE_BRANCH_NAME, BASE_BUILD_NAME, USE_CASE_NAME, SCENARIO_NAME)).thenReturn(
				baseSteps);
		when(docuReader.loadSteps(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME, USE_CASE_NAME, SCENARIO_NAME))
				.thenReturn(comparisonSteps);
		when(screenshotComparator.compare(anyString(), anyString(), any(StepLink.class),
				anyString()))
						.thenReturn(changeRate);
	}

	public List<Step> getSteps(String... names) {
		List<Step> steps = new LinkedList<Step>();
		int index = 0;
		for (String name : names) {
			StepDescription stepDescription = new StepDescription();
			stepDescription.setIndex(index++);

			Step step = new Step();
			step.setPage(new Page(name));
			step.setStepDescription(stepDescription);
			steps.add(step);
		}
		return steps;
	}
}
