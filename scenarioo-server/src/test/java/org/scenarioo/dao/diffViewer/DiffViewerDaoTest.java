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

package org.scenarioo.dao.diffViewer;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.model.diffViewer.*;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DiffViewerDaoTest {

	private static final File ROOT_DIRECTORY = new File("tmpDiffViewerUnitTestFiles");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String BASE_BUILD_NAME = "baseBuild";
	private static final String COMPARISON_NAME = "comparisonName";
	private static final String USE_CASE_NAME = "useCase";
	private static final String SCENARIO_NAME = "scenario";
	private static final int STEP_INDEX = 1;
	private static final int STEP_IN_PAGE_OCCURENCE = 1;
	private static final int PAGE_OCCURENCE = 2;
	private static final String PAGE_NAME = "page";
	private static final double CHANGE_RATE = 2.5;
	private static final int ADDED_VALUE = 1;
	private static final int CHANGED_VALUE = 2;
	private static final int REMOVED_VALUE = 3;
	private static final int NUMBER_OF_FILES = 2;

	private DiffViewerDao dao;
	private DiffViewerBuildWriter buildWriter;

	@BeforeClass
	public static void setUpClass() {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(ROOT_DIRECTORY);
	}

	@AfterClass
	public static void tearDownClass() {
		try {
			FileUtils.deleteDirectory(ROOT_DIRECTORY);
		} catch (final IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Before
	public void setUp() {
		File comparisonsFolder = new DiffViewerFiles().getComparisonDirectory(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		TestFileUtils.createFolderAndClearContent(comparisonsFolder);
		dao = new DiffViewerDao();
		buildWriter = dao.getBuildDiffWriter(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
	}

	@Test
	public void testWriteAndReadBuildDiffInfo() {
		final BuildDiffInfo buildDiffInfo = getBuildDiffInfo(COMPARISON_NAME);

		buildWriter.saveBuildDiffInfo(buildDiffInfo);
		buildWriter.flush();

		final BuildDiffInfo actualBuildDiffInfo = dao.loadBuildDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME);

		assertStructueDiffInfo(actualBuildDiffInfo, COMPARISON_NAME);
	}

	@Test
	public void testWriteAndReadUseCaseDiffInfo() {
		final UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(USE_CASE_NAME);

		buildWriter.saveUseCaseDiffInfo(useCaseDiffInfo);
		buildWriter.flush();

		final UseCaseDiffInfo actualUseCaseDiffInfo = dao.loadUseCaseDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME, USE_CASE_NAME);

		assertStructueDiffInfo(actualUseCaseDiffInfo, USE_CASE_NAME);
	}

	@Test
	public void testWriteAndReadScenarioDiffInfo() {
		final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(SCENARIO_NAME);

		buildWriter.saveScenarioDiffInfo(scenarioDiffInfo, USE_CASE_NAME);
		buildWriter.flush();

		final ScenarioDiffInfo actualScenarioDiffInfo = dao.loadScenarioDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME);

		assertStructueDiffInfo(actualScenarioDiffInfo, SCENARIO_NAME);
	}

	@Test
	public void testWriteAndReadStepDiffInfo() {
		final StepDiffInfo stepDiffInfo = getStepDiffInfo(STEP_INDEX);

		buildWriter.saveStepDiffInfo(USE_CASE_NAME, SCENARIO_NAME, stepDiffInfo);
		buildWriter.flush();

		final StepDiffInfo actualStepDiffInfo = dao.loadStepDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME, STEP_INDEX);

		assertStepDiffInfo(actualStepDiffInfo, STEP_INDEX);
	}

	@Test
	public void testWriteAndReadBuildDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			buildWriter = dao.getBuildDiffWriter(BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME + i);
			final BuildDiffInfo buildDiffInfo = getBuildDiffInfo(COMPARISON_NAME + i);
			buildWriter.saveBuildDiffInfo(buildDiffInfo);
			buildWriter.flush();
		}

		final List<BuildDiffInfo> actualBuildDiffInfos = dao.loadBuildDiffInfos(BASE_BRANCH_NAME, BASE_BUILD_NAME);

		assertEquals(2, actualBuildDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualBuildDiffInfos.get(i), COMPARISON_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadUseCaseDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			final UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(USE_CASE_NAME + i);
			buildWriter.saveUseCaseDiffInfo(useCaseDiffInfo);
		}
		buildWriter.flush();

		final List<UseCaseDiffInfo> actualUseCaseDiffInfos = dao.loadUseCaseDiffInfos(BASE_BRANCH_NAME,
			BASE_BUILD_NAME,
			COMPARISON_NAME);

		assertEquals(2, actualUseCaseDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualUseCaseDiffInfos.get(i), USE_CASE_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadScenarioDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			final ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(SCENARIO_NAME + i);
			buildWriter.saveScenarioDiffInfo(scenarioDiffInfo, USE_CASE_NAME);
		}
		buildWriter.flush();

		final List<ScenarioDiffInfo> actualScenarioDiffInfos = dao.loadScenarioDiffInfos(BASE_BRANCH_NAME,
			BASE_BUILD_NAME, COMPARISON_NAME, USE_CASE_NAME);

		assertEquals(2, actualScenarioDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualScenarioDiffInfos.get(i), SCENARIO_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadStepDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			final StepDiffInfo stepDiffInfo = getStepDiffInfo(STEP_INDEX + i);
			buildWriter.saveStepDiffInfo(USE_CASE_NAME, SCENARIO_NAME, stepDiffInfo);
		}
		buildWriter.flush();

		final List<StepDiffInfo> actualStepDiffInfos = dao.loadStepDiffInfos(BASE_BRANCH_NAME, BASE_BUILD_NAME,
			COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME);

		assertEquals(2, actualStepDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStepDiffInfo(actualStepDiffInfos.get(i), STEP_INDEX + i);
		}
	}

	private <A, R> void assertStructueDiffInfo(final StructureDiffInfo<A, R> actualStructureDiffInfo,
											   final String expectedName) {
		assertEquals(expectedName, actualStructureDiffInfo.getName());
		assertEquals(CHANGE_RATE, actualStructureDiffInfo.getChangeRate(), 0.0);
		assertEquals(ADDED_VALUE, actualStructureDiffInfo.getAdded());
		assertEquals(CHANGED_VALUE, actualStructureDiffInfo.getChanged());
		assertEquals(REMOVED_VALUE, actualStructureDiffInfo.getRemoved());
		assertFalse(actualStructureDiffInfo.getAddedElements().isEmpty());
		assertFalse(actualStructureDiffInfo.getRemovedElements().isEmpty());
	}

	private BuildDiffInfo getBuildDiffInfo(final String name) {
		return (BuildDiffInfo) initStructureDiffInfo(new BuildDiffInfo(), name, USE_CASE_NAME,
			new UseCase(USE_CASE_NAME, null));
	}

	private UseCaseDiffInfo getUseCaseDiffInfo(final String name) {
		final ScenarioSummary scenarioSummary = new ScenarioSummary();
		scenarioSummary.setScenario(new Scenario(SCENARIO_NAME, null));
		return (UseCaseDiffInfo) initStructureDiffInfo(new UseCaseDiffInfo(), name, SCENARIO_NAME, scenarioSummary);
	}

	private ScenarioDiffInfo getScenarioDiffInfo(final String name) {
		return (ScenarioDiffInfo) initStructureDiffInfo(new ScenarioDiffInfo(), name, 0, new StepInfo());
	}

	private <A, R> StructureDiffInfo<A, R> initStructureDiffInfo(final StructureDiffInfo<A, R> diffInfo,
																 final String name,
																 final A addedElement,
																 final R removedElement) {
		diffInfo.setName(name);
		diffInfo.setChangeRate(CHANGE_RATE);
		diffInfo.setAdded(ADDED_VALUE);
		diffInfo.setChanged(CHANGED_VALUE);
		diffInfo.setRemoved(REMOVED_VALUE);
		diffInfo.getAddedElements().add(addedElement);
		diffInfo.getRemovedElements().add(removedElement);
		return diffInfo;
	}

	private void assertStepDiffInfo(final StepDiffInfo actualStepDiffInfo, final int expectedIndex) {
		assertEquals(CHANGE_RATE, actualStepDiffInfo.getChangeRate(), 0.0);
		assertEquals(expectedIndex, actualStepDiffInfo.getIndex());
		assertEquals(PAGE_NAME, actualStepDiffInfo.getPageName());
		assertEquals(PAGE_OCCURENCE, actualStepDiffInfo.getPageOccurrence());
		assertEquals(STEP_IN_PAGE_OCCURENCE, actualStepDiffInfo.getStepInPageOccurrence());
	}

	private StepDiffInfo getStepDiffInfo(final int index) {
		final StepDiffInfo stepDiffInfo = new StepDiffInfo();
		stepDiffInfo.setChangeRate(CHANGE_RATE);
		stepDiffInfo.setIndex(index);
		stepDiffInfo.setPageName(PAGE_NAME);
		stepDiffInfo.setPageOccurrence(PAGE_OCCURENCE);
		stepDiffInfo.setStepInPageOccurrence(STEP_IN_PAGE_OCCURENCE);
		return stepDiffInfo;
	}

}
