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
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

/**
 * Test cases for the use case comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class UseCaseComparatorTest {

	private static String USE_CASE_NAME_1 = "useCase_1";
	private static String USE_CASE_NAME_2 = "useCase_2";
	private static String USE_CASE_NAME_3 = "useCase_3";


	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@Mock
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@Mock
	private DiffWriter diffWriter;

	@Mock
	private ScenarioComparator scenarioComparator;

	@InjectMocks
	private UseCaseComparator useCaseComparator = new UseCaseComparator(getComparatorParameters());

	@BeforeClass
	public static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder.newFolder());
		assertTrue(DiffFiles.getDiffViewerDirectory().mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
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
		assertEquals(USE_CASE_NAME_3, buildDiffInfo.getAddedElements().get(0));
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
		assertEquals(USE_CASE_NAME_1, buildDiffInfo.getAddedElements().get(0));
		assertEquals(USE_CASE_NAME_3, buildDiffInfo.getAddedElements().get(1));
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
		when(docuBuildsManager.resolveBranchAndBuildAliases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME))
			.thenReturn(new BuildIdentifier(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME));
		when(docuReader.loadUsecases(BASE_BRANCH_NAME, BASE_BUILD_NAME)).thenReturn(
			baseUseCases);
		when(docuReader.loadUsecases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME)).thenReturn(
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

	private UseCaseDiffInfo getUseCaseDiffInfo(double changeRate, int added, int changed,
											   int removed) {
		UseCaseDiffInfo useCaseDiffInfo = new UseCaseDiffInfo();
		useCaseDiffInfo.setChangeRate(changeRate);
		useCaseDiffInfo.setAdded(added);
		useCaseDiffInfo.setChanged(changed);
		useCaseDiffInfo.setRemoved(removed);
		return useCaseDiffInfo;
	}
}
