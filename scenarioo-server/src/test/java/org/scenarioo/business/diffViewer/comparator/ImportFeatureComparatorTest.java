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
import org.scenarioo.model.diffViewer.FeatureDiffInfo;
import org.scenarioo.model.docu.entities.ImportFeature;
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
 * Test cases for the feature comparator with mocked docu data.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImportFeatureComparatorTest {

	private static String USE_CASE_NAME_1 = "feature_1";
	private static String USE_CASE_NAME_2 = "feature_2";
	private static String USE_CASE_NAME_3 = "feature_3";


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
	private FeatureComparator featureComparator = new FeatureComparator(BASE_BRANCH_NAME, BASE_BUILD_NAME,
		getComparisonConfiguration());

	@BeforeClass
	public static void setUpClass() throws IOException {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(folder.newFolder());
		assertTrue(DiffFiles.getDiffViewerDirectory().mkdirs());
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Test
	public void testCompareBuildsEqual() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

		assertEquals(0, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneFeatureAdded() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

		double expectedChangeRate = 100.0 / 3.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(1, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertEquals(USE_CASE_NAME_3, buildDiffInfo.getAddedElements().get(0));
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareMultipleFeaturesAdded() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_2);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

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
	public void testCompareFeatureChangedTo50Percentage() {
		double changeRatePerFeature = 50.0;
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(changeRatePerFeature, 1, 1, 1);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

		double expectedChangeRate = changeRatePerFeature;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(3, buildDiffInfo.getChanged());
		assertEquals(0, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertTrue(buildDiffInfo.getRemovedElements().isEmpty());
	}

	@Test
	public void testCompareOneFeatureRemoved() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2);
		ImportFeature removedImportFeature = comparisonImportFeatures.get(1);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

		double expectedChangeRate = 100.0 / 2.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(1, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedImportFeature, buildDiffInfo.getRemovedElements().get(0));
	}

	@Test
	public void testCompareMultipleFeaturesRemoved() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_2);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		ImportFeature removedImportFeature1 = comparisonImportFeatures.get(0);
		ImportFeature removedImportFeature2 = comparisonImportFeatures.get(2);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		BuildDiffInfo buildDiffInfo = featureComparator.compare();

		double expectedChangeRate = 200.0 / 3.0;
		assertEquals(expectedChangeRate, buildDiffInfo.getChangeRate(), 0.0);
		assertEquals(0, buildDiffInfo.getAdded());
		assertEquals(0, buildDiffInfo.getChanged());
		assertEquals(2, buildDiffInfo.getRemoved());
		assertTrue(buildDiffInfo.getAddedElements().isEmpty());
		assertEquals(removedImportFeature1, buildDiffInfo.getRemovedElements().get(0));
		assertEquals(removedImportFeature2, buildDiffInfo.getRemovedElements().get(1));
	}

	@Test(expected = RuntimeException.class)
	public void testCompareEmptyBaseFeatureName() {
		List<ImportFeature> baseImportFeatures = getFeatures(USE_CASE_NAME_1, null);
		List<ImportFeature> comparisonImportFeatures = getFeatures(USE_CASE_NAME_1, USE_CASE_NAME_2, USE_CASE_NAME_3);
		FeatureDiffInfo featureDiffInfo = getFeatureDiffInfo(0, 0, 0, 0);

		initMocks(baseImportFeatures, comparisonImportFeatures, featureDiffInfo);

		featureComparator.compare();
	}

	private void initMocks(List<ImportFeature> baseImportFeatures, List<ImportFeature> comparisonImportFeatures,
						   FeatureDiffInfo featureDiffInfo) {
		when(docuBuildsManager.resolveBranchAndBuildAliases(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME))
			.thenReturn(new BuildIdentifier(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME));
		when(docuReader.loadFeatures(BASE_BRANCH_NAME, BASE_BUILD_NAME)).thenReturn(
			baseImportFeatures);
		when(docuReader.loadFeatures(COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME)).thenReturn(
			comparisonImportFeatures);
		when(scenarioComparator.compare(anyString())).thenReturn(featureDiffInfo);
	}

	public List<ImportFeature> getFeatures(String... names) {
		List<ImportFeature> importFeatures = new LinkedList<ImportFeature>();
		for (String name : names) {
			ImportFeature importFeature = new ImportFeature();
			importFeature.setName(name);
			importFeatures.add(importFeature);
		}
		return importFeatures;
	}

	private FeatureDiffInfo getFeatureDiffInfo(double changeRate, int added, int changed,
											   int removed) {
		FeatureDiffInfo featureDiffInfo = new FeatureDiffInfo();
		featureDiffInfo.setChangeRate(changeRate);
		featureDiffInfo.setAdded(added);
		featureDiffInfo.setChanged(changed);
		featureDiffInfo.setRemoved(removed);
		return featureDiffInfo;
	}
}
