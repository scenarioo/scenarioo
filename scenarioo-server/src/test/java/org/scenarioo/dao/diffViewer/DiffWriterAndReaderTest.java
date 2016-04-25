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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;

/**
 * Test the write and read functionality of the {@link DiffWriter} and {@link DiffReader}.
 */
public class DiffWriterAndReaderTest {

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

	private DiffWriter writer;
	private DiffReader reader;

	@Before
	public void setUp() {
		ROOT_DIRECTORY.mkdirs();
		writer = new DiffWriter(ROOT_DIRECTORY, BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME);
		reader = new DiffReader(ROOT_DIRECTORY);
	}

	@After
	public void tearDown() {
		try {
			FileUtils.deleteDirectory(ROOT_DIRECTORY);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Test
	public void testWriteAndReadBuildDiffInfo() {
		BuildDiffInfo buildDiffInfo = getBuildDiffInfo(COMPARISON_NAME);

		writer.saveBuildDiffInfo(buildDiffInfo);
		writer.flush();

		BuildDiffInfo actualBuildDiffInfo = reader.loadBuildDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME);

		assertStructueDiffInfo(actualBuildDiffInfo, COMPARISON_NAME);
	}

	@Test
	public void testWriteAndReadUseCaseDiffInfo() {
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(USE_CASE_NAME);

		writer.saveUseCaseDiffInfo(useCaseDiffInfo);
		writer.flush();

		UseCaseDiffInfo actualUseCaseDiffInfo = reader.loadUseCaseDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME, USE_CASE_NAME);

		assertStructueDiffInfo(actualUseCaseDiffInfo, USE_CASE_NAME);
	}

	@Test
	public void testWriteAndReadScenarioDiffInfo() {
		ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(SCENARIO_NAME);

		writer.saveScenarioDiffInfo(scenarioDiffInfo, USE_CASE_NAME);
		writer.flush();

		ScenarioDiffInfo actualScenarioDiffInfo = reader.loadScenarioDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME);

		assertStructueDiffInfo(actualScenarioDiffInfo, SCENARIO_NAME);
	}

	@Test
	public void testWriteAndReadStepDiffInfo() {
		StepDiffInfo stepDiffInfo = getStepDiffInfo(STEP_INDEX);

		writer.saveStepDiffInfo(USE_CASE_NAME, SCENARIO_NAME, stepDiffInfo);
		writer.flush();

		StepDiffInfo actualStepDiffInfo = reader.loadStepDiffInfo(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME, STEP_INDEX);

		assertStepDiffInfo(actualStepDiffInfo, STEP_INDEX);
	}

	@Test
	public void testWriteAndReadBuildDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			writer = new DiffWriter(ROOT_DIRECTORY, BASE_BRANCH_NAME, BASE_BUILD_NAME, COMPARISON_NAME + i);
			BuildDiffInfo buildDiffInfo = getBuildDiffInfo(COMPARISON_NAME + i);
			writer.saveBuildDiffInfo(buildDiffInfo);
			writer.flush();
		}

		List<BuildDiffInfo> actualBuildDiffInfos = reader.loadBuildDiffInfos(BASE_BRANCH_NAME, BASE_BUILD_NAME);

		assertEquals(2, actualBuildDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualBuildDiffInfos.get(i), COMPARISON_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadUseCaseDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(USE_CASE_NAME + i);
			writer.saveUseCaseDiffInfo(useCaseDiffInfo);
		}
		writer.flush();

		List<UseCaseDiffInfo> actualUseCaseDiffInfos = reader.loadUseCaseDiffInfos(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME);

		assertEquals(2, actualUseCaseDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualUseCaseDiffInfos.get(i), USE_CASE_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadScenarioDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			ScenarioDiffInfo scenarioDiffInfo = getScenarioDiffInfo(SCENARIO_NAME + i);
			writer.saveScenarioDiffInfo(scenarioDiffInfo, USE_CASE_NAME);
		}
		writer.flush();

		List<ScenarioDiffInfo> actualScenarioDiffInfos = reader.loadScenarioDiffInfos(BASE_BRANCH_NAME,
				BASE_BUILD_NAME, COMPARISON_NAME, USE_CASE_NAME);

		assertEquals(2, actualScenarioDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStructueDiffInfo(actualScenarioDiffInfos.get(i), SCENARIO_NAME + i);
		}
	}

	@Test
	public void testWriteAndReadStepDiffInfos() {
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			StepDiffInfo stepDiffInfo = getStepDiffInfo(STEP_INDEX + i);
			writer.saveStepDiffInfo(USE_CASE_NAME, SCENARIO_NAME, stepDiffInfo);
		}
		writer.flush();

		List<StepDiffInfo> actualStepDiffInfos = reader.loadStepDiffInfos(BASE_BRANCH_NAME, BASE_BUILD_NAME,
				COMPARISON_NAME, USE_CASE_NAME, SCENARIO_NAME);

		assertEquals(2, actualStepDiffInfos.size());
		for (int i = 0; i < NUMBER_OF_FILES; i++) {
			assertStepDiffInfo(actualStepDiffInfos.get(i), STEP_INDEX + i);
		}
	}

	private <T> void assertStructueDiffInfo(final StructureDiffInfo<T> actualStructureDiffInfo, final String expectedName) {
		assertEquals(expectedName, actualStructureDiffInfo.getName());
		assertEquals(CHANGE_RATE, actualStructureDiffInfo.getChangeRate(), 0.0);
		assertEquals(ADDED_VALUE, actualStructureDiffInfo.getAdded());
		assertEquals(CHANGED_VALUE, actualStructureDiffInfo.getChanged());
		assertEquals(REMOVED_VALUE, actualStructureDiffInfo.getRemoved());
		assertFalse(actualStructureDiffInfo.getAddedElements().isEmpty());
		assertFalse(actualStructureDiffInfo.getRemovedElements().isEmpty());
	}

	private BuildDiffInfo getBuildDiffInfo(final String name){
		return (BuildDiffInfo) initStructureDiffInfo(new BuildDiffInfo(), name, new UseCase(USE_CASE_NAME, null));
	}

	private UseCaseDiffInfo getUseCaseDiffInfo(final String name) {
		return (UseCaseDiffInfo) initStructureDiffInfo(new UseCaseDiffInfo(), name, new Scenario(SCENARIO_NAME, null));
	}

	private ScenarioDiffInfo getScenarioDiffInfo(final String name) {
		return (ScenarioDiffInfo) initStructureDiffInfo(new ScenarioDiffInfo(), name, new StepLink());
	}

	private <T> StructureDiffInfo<T> initStructureDiffInfo(final StructureDiffInfo<T> diffInfo, final String name,
			final T element) {
		diffInfo.setName(name);
		diffInfo.setChangeRate(CHANGE_RATE);
		diffInfo.setAdded(ADDED_VALUE);
		diffInfo.setChanged(CHANGED_VALUE);
		diffInfo.setRemoved(REMOVED_VALUE);
		diffInfo.getAddedElements().add(element);
		diffInfo.getRemovedElements().add(element);
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
		StepDiffInfo stepDiffInfo = new StepDiffInfo();
		stepDiffInfo.setChangeRate(CHANGE_RATE);
		stepDiffInfo.setIndex(index);
		stepDiffInfo.setPageName(PAGE_NAME);
		stepDiffInfo.setPageOccurrence(PAGE_OCCURENCE);
		stepDiffInfo.setStepInPageOccurrence(STEP_IN_PAGE_OCCURENCE);
		return stepDiffInfo;
	}

}
