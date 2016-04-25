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
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Test cases for the use case comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class UseCaseComparatorTest {

	private static final File ROOT_DIRECTORY = new File("tmpDiffViewerUnitTestFiles");
	private static final String BASE_BRANCH_NAME = "baseBranch";
	private static final String BASE_BUILD_NAME = "baseBuild";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranch";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuild";
	private static final String COMPARISON_NAME = "comparisonName";
	private static final String USE_CASE_NAME_1 = "useCase_1";
	private static final String USE_CASE_NAME_2 = "useCase_2";
	private static final String USE_CASE_NAME_3 = "useCase_3";

	@Mock
	private ScenarioDocuReader scenarioDocuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private ScenarioComparator scenarioComparator;

	@InjectMocks
	private UseCaseComparator useCaseComparator = new UseCaseComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME,
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
		} catch (IOException e) {
			throw new RuntimeException("Could not delete test data directory", e);
		}
	}

	@Test
	public void testCompareBuildsEqual() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		assertEquals(0, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneUseCaseAdded() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertEquals(baseUseCases.get(2), buildDiffInfo.getAddedElements().get(0));
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareMultipleUseCasesAdded() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_2);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(2, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertEquals(baseUseCases.get(0), buildDiffInfo.getAddedElements().get(0));
		assertEquals(baseUseCases.get(2), buildDiffInfo.getAddedElements().get(1));
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareUseCaseChangedTo50Percentage() {
		double changeRatePerUseCase = 50.0;
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(changeRatePerUseCase, 1, 1, 1);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		double expectedChangeRate = changeRatePerUseCase;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(3, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneUseCaseRemoved() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2);
		UseCase removedUseCase = comparisonUseCases.get(1);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(1, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedUseCase, buildDiffInfo.getRemovedElements().get(0));
	}

	@Test
	public void testCompareMultipleUseCasesRemoved() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_2);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		UseCase removedUseCase1 = comparisonUseCases.get(0);
		UseCase removedUseCase2 = comparisonUseCases.get(2);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		BuildDiffInfo buildDiffInfo = useCaseComparator.compare();

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(2, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedUseCase1, buildDiffInfo.getRemovedElements().get(0));
		assertEquals(removedUseCase2, buildDiffInfo.getRemovedElements().get(1));
	}

	@Test(expected = RuntimeException.class)
	public void testCompareEmptyBaseUseCaseName() {
		List<UseCase> baseUseCases = getUseCases(USE_CASE_NAME_1, null);
		List<UseCase> comparisonUseCases = getUseCases(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		UseCaseDiffInfo useCaseDiffInfo = getUseCaseDiffInfo(0, 0, 0, 0);

		initMocks(baseUseCases, comparisonUseCases, useCaseDiffInfo);

		useCaseComparator.compare();
	}

	private void initMocks(List<UseCase> baseUseCases, List<UseCase> comparisonUseCases,
			UseCaseDiffInfo useCaseDiffInfo) {
		when(scenarioDocuReader.loadUsecases(BASE_BRANCH_NAME, BASE_BUILD_NAME)).thenReturn(
				baseUseCases);
		when(scenarioDocuReader.loadUsecases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME)).thenReturn(
				comparisonUseCases);
		when(scenarioComparator.compare(anyString())).thenReturn(useCaseDiffInfo);
	}

	public List<UseCase> getUseCases(String... names) {
		List<UseCase> useCases = new LinkedList<UseCase>();
		for (String name : names) {
			UseCase useCase = new UseCase();
			useCase.setName(name);
			useCases.add(useCase);
		}
		return useCases;
	}

	private UseCaseDiffInfo getUseCaseDiffInfo(double changeRate, int added, int changed, int removed) {
		UseCaseDiffInfo useCaseDiffInfo = new UseCaseDiffInfo();
		useCaseDiffInfo.setChangeRate(changeRate);
		useCaseDiffInfo.setAdded(added);
		useCaseDiffInfo.setChanged(changed);
		useCaseDiffInfo.setRemoved(removed);
		return useCaseDiffInfo;
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
