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

package org.scenarioo.business.diffViewer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Test cases for the step comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class StepComparatorTest {

	private static final File ROOT_DIRECTORY = new File("tmpDiffViewerUnitTestFiles");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String BASE_BUILD_NAME = "baseBuild";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranch";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuild";
	private static final String COMPARISON_NAME = "comparisonName";
	private static final String USE_CASE_NAME = "useCase";
	private static final String SCENARIO_NAME = "scenario";
	private static final String PAGE_NAME_1 = "page1";
	private static final String PAGE_NAME_2 = "page2";

	@Mock
	private ScenarioDocuReader scenarioDocuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private ScreenshotComparator screenshotComparator;

	@InjectMocks
	private final StepComparator stepComparator = new StepComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);

	@BeforeClass
	public static void setUpClass() {
		ROOT_DIRECTORY.mkdirs();
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(ROOT_DIRECTORY);
		RepositoryLocator.INSTANCE.getConfigurationRepository().getDiffViewerDirectory().mkdirs();
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@AfterClass
	public static void tearDownClass() {
		try {
			FileUtils.deleteDirectory(ROOT_DIRECTORY);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Test
	public void testCompareBuildsEqual() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		assertEquals(0, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, actualDiffInfo.getAdded());
		assertEquals(0, actualDiffInfo.getChanged());
		assertEquals(0, actualDiffInfo.getRemoved());
	}

	@Test
	public void testCompareOneStepAdded() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, actualDiffInfo.getAdded());
		assertEquals(0, actualDiffInfo.getChanged());
		assertEquals(0, actualDiffInfo.getRemoved());
	}

	@Test
	public void testCompareMultipleStepsAdded() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(2, actualDiffInfo.getAdded());
		assertEquals(0, actualDiffInfo.getChanged());
		assertEquals(0, actualDiffInfo.getRemoved());
	}

	@Test
	public void testCompareStepChangedTo50Percentage() {
		double changeRatePerStep = 50.0;
		List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, changeRatePerStep);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = changeRatePerStep;
		assertEquals(expectedChangeRate, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, actualDiffInfo.getAdded());
		assertEquals(3, actualDiffInfo.getChanged());
		assertEquals(0, actualDiffInfo.getRemoved());
	}

	@Test
	public void testCompareOneStepRemoved() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, actualDiffInfo.getAdded());
		assertEquals(0, actualDiffInfo.getChanged());
		assertEquals(1, actualDiffInfo.getRemoved());
	}

	@Test
	public void testCompareMultipleStepsRemoved() {
		List<Step> baseSteps = getSteps(PAGE_NAME_1);
		List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		StructureDiffInfo actualDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, actualDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, actualDiffInfo.getAdded());
		assertEquals(0, actualDiffInfo.getChanged());
		assertEquals(2, actualDiffInfo.getRemoved());
	}

	private void initMocks(final List<Step> baseSteps, final List<Step> comparisonSteps,
			final double changeRate) {
		when(scenarioDocuReader.loadSteps(BASE_BRANCH_NAME, BASE_BUILD_NAME, USE_CASE_NAME, SCENARIO_NAME)).thenReturn(
				baseSteps);
		when(scenarioDocuReader.loadSteps(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME, USE_CASE_NAME, SCENARIO_NAME))
				.thenReturn(comparisonSteps);
		when(screenshotComparator.compare(anyString(), anyString(), any(ComparisonAlias.class), any(StepLink.class),
				any(StepLink.class)))
				.thenReturn(changeRate);
	}

	public List<Step> getSteps(final String... names) {
		List<Step> steps = new LinkedList<Step>();
		for (String name : names) {
			Step step = new Step();
			step.setPage(new Page(name));
			steps.add(step);
		}
		return steps;
	}

	private static Configuration getTestConfiguration() {

		ComparisonAlias comparisonAlias = new ComparisonAlias();
		comparisonAlias.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonAlias.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonAlias.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonAlias.setComparisonName(COMPARISON_NAME);

		List<ComparisonAlias> comparisonAliases = new LinkedList<ComparisonAlias>();
		comparisonAliases.add(comparisonAlias);

		Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonAliases(comparisonAliases);

		return configuration;
	}
}
