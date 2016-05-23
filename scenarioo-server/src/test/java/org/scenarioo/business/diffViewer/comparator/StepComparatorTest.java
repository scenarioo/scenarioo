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
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Test cases for the step comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class StepComparatorTest {

	private static final File ROOT_DIRECTORY = new File("tmp");
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
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private ScreenshotComparator screenshotComparator;

	@InjectMocks
	private final StepComparator stepComparator = new StepComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME);

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
		} catch (final IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Test
	public void testCompareBuildsEqual() {
		final List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		assertEquals(0, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneStepAdded() {
		final List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		final double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertEquals(new Integer(2), scenarioDiffInfo.getAddedElements().get(0));
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareMultipleStepsAdded() {
		final List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		final double expectedChangeRate = 200.0 / 3.0;
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
		final double changeRatePerStep = 50.0;
		final List<Step> baseSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, changeRatePerStep);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		final double expectedChangeRate = changeRatePerStep;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(3, scenarioDiffInfo.getChanged());
		assertEquals(0, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertTrue(scenarioDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneStepRemoved() {
		final List<Step> baseSteps = getSteps(PAGE_NAME_1);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1);

		initMocks(baseSteps, comparisonSteps, 0.0);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		final double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(1, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertEquals(PAGE_NAME_1, scenarioDiffInfo.getRemovedElements().get(0).getStepLink().getPageName());
	}

	@Test
	public void testCompareMultipleStepsRemoved() {
		final List<Step> baseSteps = getSteps(PAGE_NAME_1);
		final List<Step> comparisonSteps = getSteps(PAGE_NAME_1, PAGE_NAME_1, PAGE_NAME_2);

		initMocks(baseSteps, comparisonSteps, 0.0);

		final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(USE_CASE_NAME, SCENARIO_NAME);

		final double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, scenarioDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, scenarioDiffInfo.getAdded());
		assertEquals(0, scenarioDiffInfo.getChanged());
		assertEquals(2, scenarioDiffInfo.getRemoved());
		assertTrue(scenarioDiffInfo.getAddedElements().isEmpty());
		assertEquals(PAGE_NAME_1, scenarioDiffInfo.getRemovedElements().get(0).getStepLink().getPageName());
		assertEquals(PAGE_NAME_2, scenarioDiffInfo.getRemovedElements().get(1).getStepLink().getPageName());
	}

	private void initMocks(final List<Step> baseSteps, final List<Step> comparisonSteps,
			final double changeRate) {
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

	public List<Step> getSteps(final String... names) {
		final List<Step> steps = new LinkedList<Step>();
		int index = 0;
		for (final String name : names) {
			final StepDescription stepDescription = new StepDescription();
			stepDescription.setIndex(index++);

			final Step step = new Step();
			step.setPage(new Page(name));
			step.setStepDescription(stepDescription);
			steps.add(step);
		}
		return steps;
	}

	private static Configuration getTestConfiguration() {

		final ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonConfiguration.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonConfiguration.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonConfiguration.setName(COMPARISON_NAME);

		final List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(comparisonConfiguration);

		final Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonConfigurations(comparisonConfigurations);

		return configuration;
	}
}
