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
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.dao.diffViewer.impl.DiffFiles;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

/**
 * Test cases for the scenario comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScenarioComparatorTest {

	private static final File ROOT_DIRECTORY = new File("tmp");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String BASE_BUILD_NAME = "baseBuild";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranch";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuild";
	private static final String COMPARISON_NAME = "comparisonName";
	private static final String USE_CASE_NAME = "useCase";
	private static final String SCENARIO_NAME_1 = "scenario_1";
	private static final String SCENARIO_NAME_2 = "scenario_2";
	private static final String SCENARIO_NAME_3 = "scenario_3";

	@Mock
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private AggregatedDocuDataReader aggregatedDataReader;

	@Mock
	private StepComparator stepComparator;

	@InjectMocks
	private ScenarioComparator scenarioComparator = new ScenarioComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			getComparisonConfiguration());

	@BeforeClass
	public static void setUpClass() {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(ROOT_DIRECTORY);
		assertTrue(DiffFiles.getDiffViewerDirectory().mkdirs());
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
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		assertEquals(0, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneScenarioAdded() {
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		final double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertEquals(SCENARIO_NAME_3, useCaseDiffInfo.getAddedElements().get(0));
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareMultipleScenariosAdded() {
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_2);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		final double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(2, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertEquals(SCENARIO_NAME_1, useCaseDiffInfo.getAddedElements().get(0));
		assertEquals(SCENARIO_NAME_3, useCaseDiffInfo.getAddedElements().get(1));
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareScenarioChangedTo50Percentage() {
		final double changeRatePerScenario = 50.0;
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(changeRatePerScenario, 1, 1, 1);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		final double expectedChangeRate = changeRatePerScenario;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(3, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneScenarioRemoved() {
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2);
		final Scenario removedScenario = comparisonScenarios.get(1);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		final double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(1, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedScenario.getName(), useCaseDiffInfo.getRemovedElements().get(0).getScenario().getName());
	}

	@Test
	public void testCompareMultipleScenariosRemoved() {
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_2);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final Scenario removedScenario1 = comparisonScenarios.get(0);
		final Scenario removedScenario2 = comparisonScenarios.get(2);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		final double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(2, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedScenario1.getName(), useCaseDiffInfo.getRemovedElements().get(0).getScenario().getName());
		assertEquals(removedScenario2.getName(), useCaseDiffInfo.getRemovedElements().get(1).getScenario().getName());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareEmptyBaseScenarioName() {
		final List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, null);
		final List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		scenarioComparator.compare(USE_CASE_NAME);
	}

	private void initMocks(final List<Scenario> baseScenarios, final List<Scenario> comparisonScenarios,
			final ScenarioDiffInfo scenarioDiffInfo) {
		when(docuBuildsManager.resolveBranchAndBuildAliases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME))
				.thenReturn(new BuildIdentifier(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME));
		when(docuReader.loadScenarios(BASE_BRANCH_NAME, BASE_BUILD_NAME, USE_CASE_NAME)).thenReturn(
				baseScenarios);
		when(docuReader.loadScenarios(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME, USE_CASE_NAME))
				.thenReturn(comparisonScenarios);
		when(stepComparator.compare(anyString(), anyString())).thenReturn(scenarioDiffInfo);

		final UseCaseScenarios useCaseScenarios = getUseCaseScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		when(aggregatedDataReader.loadUseCaseScenarios(any(BuildIdentifier.class), anyString())).thenReturn(
				useCaseScenarios);
	}

	private List<Scenario> getScenarios(final String... names) {
		final List<Scenario> scenarios = new LinkedList<Scenario>();
		for (final String name : names) {
			final Scenario scenario = new Scenario();
			scenario.setName(name);
			scenarios.add(scenario);
		}
		return scenarios;
	}

	private UseCaseScenarios getUseCaseScenarios(final String... names) {
		final List<ScenarioSummary> scenarioSummaries = new LinkedList<ScenarioSummary>();
		for (final String name : names) {
			final Scenario scenario = new Scenario();
			scenario.setName(name);
			final ScenarioSummary scenarioSummary = new ScenarioSummary();
			scenarioSummary.setScenario(scenario);
			scenarioSummaries.add(scenarioSummary);
		}

		final UseCaseScenarios useCaseScenarios = new UseCaseScenarios();
		useCaseScenarios.setScenarios(scenarioSummaries);

		return useCaseScenarios;
	}

	private ScenarioDiffInfo getScenarioDiffInfo(final double changeRate, final int added, final int changed, final int removed) {
		final ScenarioDiffInfo scenarioDiffInfo = new ScenarioDiffInfo();
		scenarioDiffInfo.setChangeRate(changeRate);
		scenarioDiffInfo.setAdded(added);
		scenarioDiffInfo.setChanged(changed);
		scenarioDiffInfo.setRemoved(removed);
		return scenarioDiffInfo;
	}

	private static Configuration getTestConfiguration() {

		final ComparisonConfiguration comparisonConfiguration = getComparisonConfiguration();
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

	// TODO danielsuter duplicated code
	private static ComparisonConfiguration getComparisonConfiguration() {
		final ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonConfiguration.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonConfiguration.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonConfiguration.setName(COMPARISON_NAME);
		return comparisonConfiguration;
	}
}
