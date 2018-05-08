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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.TestFileUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.getComparisonConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class ComparisonExecutorTest {

	private static int NUMBER_OF_COMPARISONS_FOR_BRANCH_1 = 5;
	private static int NUMBER_OF_COMPARISONS_FOR_BRANCH_2 = 1;
	private static File ROOT_DIRECTORY = new File("tmpDir");
	private static String BRANCH_NAME_1 = "branch1";
	private static String BRANCH_NAME_2 = "branch2";
	private static String BUILD_NAME_1 = "build1";
	private static String BUILD_NAME_2 = "build2";
	private static String BUILD_NAME_3 = "build3";
	private static String BUILD_NAME_ALIAS_LAST_SUCCESSFUL = "last successful";
	private static String BUILD_NAME_ALIAS_MOST_RECENT = "most recent";
	private static String COMPARISON_NAME = "comparison";


	private static ComparisonConfiguration comparisonConfiguration1;
	private static ComparisonConfiguration comparisonConfiguration2;
	private static ComparisonConfiguration comparisonConfiguration3;
	private static ComparisonConfiguration comparisonConfiguration4;
	private static ComparisonConfiguration comparisonConfiguration5;
	private static ComparisonConfiguration comparisonConfiguration6;

	private Build build1 = getBuild(BUILD_NAME_1, Status.SUCCESS, getDateBeforeDays(0));
	private Build build2 = getBuild(BUILD_NAME_2, Status.FAILED, getDateBeforeDays(1));
	private Build build3 = getBuild(BUILD_NAME_3, Status.SUCCESS, getDateBeforeDays(2));

	@Mock
	private ScenarioDocuBuildsManager docuBuildsManager;

	@Mock
	private ScenarioDocuReader docuReader;

	@InjectMocks
	private ComparisonExecutor comparisonExecutor = new ComparisonExecutor(null, docuBuildsManager);

	@BeforeClass
	public static void setUpClass() {
		TestFileUtils.createFolderAndSetItAsRootInConfigurationForUnitTest(ROOT_DIRECTORY);
		File diffViewerFolder = new File(ROOT_DIRECTORY, "scenarioo-application-data/diffViewer");
		diffViewerFolder.mkdirs();
		comparisonConfiguration1 = getComparisonConfiguration(BRANCH_NAME_1,
			BRANCH_NAME_1, BUILD_NAME_ALIAS_LAST_SUCCESSFUL, COMPARISON_NAME);
		comparisonConfiguration2 = getComparisonConfiguration(BRANCH_NAME_1,
			BRANCH_NAME_1, BUILD_NAME_ALIAS_MOST_RECENT, COMPARISON_NAME);
		comparisonConfiguration3 = getComparisonConfiguration(BRANCH_NAME_1,
			BRANCH_NAME_2, BUILD_NAME_ALIAS_LAST_SUCCESSFUL, COMPARISON_NAME);
		comparisonConfiguration4 = getComparisonConfiguration(BRANCH_NAME_1,
			BRANCH_NAME_1, BUILD_NAME_1, COMPARISON_NAME);
		comparisonConfiguration5 = getComparisonConfiguration(BRANCH_NAME_1,
			BRANCH_NAME_2, BUILD_NAME_2, COMPARISON_NAME);
		comparisonConfiguration6 = getComparisonConfiguration(BRANCH_NAME_2,
			BRANCH_NAME_2, BUILD_NAME_3, COMPARISON_NAME);
		RepositoryLocator.INSTANCE.getConfigurationRepository().updateConfiguration(getTestConfiguration());
	}

	@Before
	public void setUp() {
		when(docuBuildsManager.resolveBranchAlias(BRANCH_NAME_1)).thenReturn(BRANCH_NAME_1);
		when(docuBuildsManager.resolveBranchAlias(BRANCH_NAME_2)).thenReturn(BRANCH_NAME_2);

		when(docuBuildsManager.resolveBranchAndBuildAliases(BRANCH_NAME_1, BUILD_NAME_1))
			.thenReturn(new BuildIdentifier(BRANCH_NAME_1, BUILD_NAME_1));
		when(docuBuildsManager.resolveBranchAndBuildAliases(BRANCH_NAME_2, BUILD_NAME_ALIAS_LAST_SUCCESSFUL))
			.thenReturn(new BuildIdentifier(BRANCH_NAME_2, BUILD_NAME_1));
		when(docuBuildsManager.resolveBranchAndBuildAliases(BRANCH_NAME_2, BUILD_NAME_2))
			.thenReturn(new BuildIdentifier(BRANCH_NAME_2, BUILD_NAME_2));
		when(docuBuildsManager.resolveBranchAndBuildAliases(BRANCH_NAME_2, BUILD_NAME_3))
			.thenReturn(new BuildIdentifier(BRANCH_NAME_2, BUILD_NAME_3));

		when(docuReader.loadBuild(BRANCH_NAME_1, BUILD_NAME_1)).thenReturn(build1);
		when(docuReader.loadBuild(BRANCH_NAME_1, BUILD_NAME_2)).thenReturn(build2);
		when(docuReader.loadBuild(BRANCH_NAME_1, BUILD_NAME_3)).thenReturn(build3);

		when(docuReader.loadBuilds(BRANCH_NAME_1)).thenReturn(getBuilds());
	}

	@Test
	public void testGetComparisonConfigurationsForBaseBranch1() {
		List<ComparisonConfiguration> result = comparisonExecutor
			.getComparisonConfigurationsForBaseBranch(BRANCH_NAME_1);
		assertEquals(NUMBER_OF_COMPARISONS_FOR_BRANCH_1, result.size());
		assertEquals(comparisonConfiguration1, result.get(0));
		assertEquals(comparisonConfiguration2, result.get(1));
		assertEquals(comparisonConfiguration3, result.get(2));
		assertEquals(comparisonConfiguration4, result.get(3));
		assertEquals(comparisonConfiguration5, result.get(4));
	}

	@Test
	public void testGetComparisonConfigurationsForBaseBranch2() {
		List<ComparisonConfiguration> result = comparisonExecutor
			.getComparisonConfigurationsForBaseBranch(BRANCH_NAME_2);
		assertEquals(NUMBER_OF_COMPARISONS_FOR_BRANCH_2, result.size());
		assertEquals(comparisonConfiguration6, result.get(0));
	}

	@Test
	public void testResolveComparisonConfigurationLastSuccessfulSameBranch() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration1, BUILD_NAME_1);
		assertEquals(BRANCH_NAME_1, result.getBaseBranchName());
		assertEquals(BRANCH_NAME_1, result.getComparisonBranchName());
		assertEquals(BUILD_NAME_3, result.getComparisonBuildName());
	}

	@Test
	public void testResolveComparisonConfigurationMostRecentSameBranch() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration2, BUILD_NAME_1);
		assertEquals(BRANCH_NAME_1, result.getBaseBranchName());
		assertEquals(BRANCH_NAME_1, result.getComparisonBranchName());
		assertEquals(BUILD_NAME_2, result.getComparisonBuildName());
	}

	@Test
	public void testResolveComparisonConfigurationLastSuccessfulOtherBranch() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration3, BUILD_NAME_2);
		assertEquals(BRANCH_NAME_1, result.getBaseBranchName());
		assertEquals(BRANCH_NAME_2, result.getComparisonBranchName());
		assertEquals(BUILD_NAME_1, result.getComparisonBuildName());
	}

	@Test
	public void testResolveComparisonConfigurationSameBranchAndSameBuild() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration4, BUILD_NAME_1);
		assertTrue(result == null);
	}

	@Test
	public void testResolveComparisonConfigurationSameBranchAndBuildWithoutAlias() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration5, BUILD_NAME_1);
		assertEquals(BRANCH_NAME_1, result.getBaseBranchName());
		assertEquals(BRANCH_NAME_2, result.getComparisonBranchName());
		assertEquals(BUILD_NAME_2, result.getComparisonBuildName());
	}

	@Test
	public void testResolveComparisonConfigurationOtherBranchAndBuildWithoutAlias() {
		ComparisonConfiguration result = comparisonExecutor.resolveComparisonConfiguration(
			comparisonConfiguration6, BUILD_NAME_1);
		assertEquals(BRANCH_NAME_2, result.getBaseBranchName());
		assertEquals(BRANCH_NAME_2, result.getComparisonBranchName());
		assertEquals(BUILD_NAME_3, result.getComparisonBuildName());
	}

	private static Configuration getTestConfiguration() {

		List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(comparisonConfiguration1);
		comparisonConfigurations.add(comparisonConfiguration2);
		comparisonConfigurations.add(comparisonConfiguration3);
		comparisonConfigurations.add(comparisonConfiguration4);
		comparisonConfigurations.add(comparisonConfiguration5);
		comparisonConfigurations.add(comparisonConfiguration6);

		Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonConfigurations(comparisonConfigurations);

		return configuration;
	}

	private Build getBuild(String name, Status status, Date date) {
		Build build = new Build(name);
		build.setDate(date);
		build.setStatus(status);
		return build;
	}

	private Date getDateBeforeDays(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTime();
	}

	private List<ObjectFromDirectory<Build>> getBuilds() {
		List<ObjectFromDirectory<Build>> builds = new LinkedList<ObjectFromDirectory<Build>>();
		builds.add(new ObjectFromDirectory<Build>(build1, ROOT_DIRECTORY.getName()));
		builds.add(new ObjectFromDirectory<Build>(build2, ROOT_DIRECTORY.getName()));
		builds.add(new ObjectFromDirectory<Build>(build3, ROOT_DIRECTORY.getName()));
		return builds;
	}

}
