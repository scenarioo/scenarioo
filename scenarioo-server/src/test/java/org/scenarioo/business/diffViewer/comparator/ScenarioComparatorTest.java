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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.diffViewer.DiffViewerFiles;
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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

/**
 * Test cases for the scenario comparator with mocked docu data.
 */
@ExtendWith(MockitoExtension.class)
class ScenarioComparatorTest {

	private static String USE_CASE_NAME = "useCase";
	private static String SCENARIO_NAME_1 = "scenario_1";
	private static String SCENARIO_NAME_2 = "scenario_2";
	private static String SCENARIO_NAME_3 = "scenario_3";

	@TempDir
	static File rootDirectory;

	@Mock(lenient = true)
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@Mock(lenient = true)
	private AggregatedDocuDataReader aggregatedDataReader;

	@Mock(lenient = true)
	private StepComparator stepComparator;

	@InjectMocks
	private final ScenarioComparator scenarioComparator = new ScenarioComparator(getComparatorParameters());

	@BeforeAll
	static void setUpClass() {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(rootDirectory);
		File comparisonsFolder = new DiffViewerFiles().getComparisonDirectory(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		assertTrue(comparisonsFolder.mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	void testCompareBuildsEqual() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		assertEquals(0, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	void testCompareOneScenarioAdded() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertEquals(SCENARIO_NAME_3, useCaseDiffInfo.getAddedElements().get(0));
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	void testCompareMultipleScenariosAdded() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_2);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(2, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertEquals(SCENARIO_NAME_1, useCaseDiffInfo.getAddedElements().get(0));
		assertEquals(SCENARIO_NAME_3, useCaseDiffInfo.getAddedElements().get(1));
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	void testCompareScenarioChangedTo50Percentage() {
		double changeRatePerScenario = 50.0;
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(changeRatePerScenario, 1, 1, 1);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		double expectedChangeRate = changeRatePerScenario;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(3, useCaseDiffInfo.getChanged());
		assertEquals(0, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertTrue(useCaseDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	void testCompareOneScenarioRemoved() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2);
		Scenario removedScenario = comparisonScenarios.get(1);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(1, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedScenario.getName(), useCaseDiffInfo.getRemovedElements().get(0).getScenario().getName());
	}

	@Test
	void testCompareMultipleScenariosRemoved() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_2);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		Scenario removedScenario1 = comparisonScenarios.get(0);
		Scenario removedScenario2 = comparisonScenarios.get(2);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(USE_CASE_NAME);

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, useCaseDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, useCaseDiffInfo.getAdded());
		assertEquals(0, useCaseDiffInfo.getChanged());
		assertEquals(2, useCaseDiffInfo.getRemoved());
		assertTrue(useCaseDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedScenario1.getName(), useCaseDiffInfo.getRemovedElements().get(0).getScenario().getName());
		assertEquals(removedScenario2.getName(), useCaseDiffInfo.getRemovedElements().get(1).getScenario().getName());
	}

	@Test
	void testCompareEmptyBaseScenarioName() {
		List<Scenario> baseScenarios = getScenarios(SCENARIO_NAME_1, null);
		List<Scenario> comparisonScenarios = getScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(0, 0, 0, 0);

		initMocks(baseScenarios, comparisonScenarios, scenarioDiffInfo);

		assertThrows(RuntimeException.class, () -> scenarioComparator.compare(USE_CASE_NAME));
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

		UseCaseScenarios useCaseScenarios = getUseCaseScenarios(SCENARIO_NAME_1, SCENARIO_NAME_2, SCENARIO_NAME_3);
		when(aggregatedDataReader.loadUseCaseScenarios(any(BuildIdentifier.class), anyString())).thenReturn(
				useCaseScenarios);
	}

	private List<Scenario> getScenarios(final String... names) {
		List<Scenario> scenarios = new LinkedList<>();
		for (String name : names) {
			Scenario scenario = new Scenario();
			scenario.setName(name);
			scenarios.add(scenario);
		}
		return scenarios;
	}

	private UseCaseScenarios getUseCaseScenarios(final String... names) {
		List<ScenarioSummary> scenarioSummaries = new LinkedList<>();
		for (String name : names) {
			Scenario scenario = new Scenario();
			scenario.setName(name);
			ScenarioSummary scenarioSummary = new ScenarioSummary();
			scenarioSummary.setScenario(scenario);
			scenarioSummaries.add(scenarioSummary);
		}

		UseCaseScenarios useCaseScenarios = new UseCaseScenarios();
		useCaseScenarios.setScenarios(scenarioSummaries);

		return useCaseScenarios;
	}

	private ScenarioDiffInfo getScenarioDiffInfo(final double changeRate, final int added, final int changed, final int removed) {
		ScenarioDiffInfo scenarioDiffInfo = new ScenarioDiffInfo("fake scenario");
		scenarioDiffInfo.setChangeRate(changeRate);
		scenarioDiffInfo.setAdded(added);
		scenarioDiffInfo.setChanged(changed);
		scenarioDiffInfo.setRemoved(removed);
		return scenarioDiffInfo;
	}

	private static Configuration getTestConfiguration() {

		ComparisonConfiguration comparisonConfiguration = getComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonConfiguration.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonConfiguration.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonConfiguration.setName(COMPARISON_NAME);

		List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<>();
		comparisonConfigurations.add(comparisonConfiguration);

		Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonConfigurations(comparisonConfigurations);

		return configuration;
	}
}
