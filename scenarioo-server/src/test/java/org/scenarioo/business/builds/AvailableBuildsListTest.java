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
package org.scenarioo.business.builds;

import static org.junit.Assert.*;
import static org.scenarioo.model.configuration.Configuration.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.rest.base.BuildIdentifier;

public class AvailableBuildsListTest {
	
	private AvailableBuildsList availableBuildsList;
	
	// Some builds for testing
	private final BuildLink build1 = createBuildSuccess("build1", 1);
	private final BuildLink build2 = createBuildSuccess("build2", 2);
	private final BuildLink build3 = createBuildSuccess("build3", 3);
	private final BuildLink build4 = createBuildFailed("build4", 4);
	private final BuildLink build5 = createBuildFailed("build5", 5);
	private final BuildLink build6 = createBuildFailed("build6", 6);
	private final BuildLink build7 = createBuildSuccess("build7", 7);
	
	@Before
	public void setUp() {
		ConfigurationDAO.injectConfiguration(new Configuration());
		availableBuildsList = new AvailableBuildsList();
	}
	
	@Test
	public void testInitializedEmpty() {
		assertEquals("Expected initialized as empty list", 0, availableBuildsList.getBranchBuildsList().size());
	}
	
	@Test
	public void testUpdatingAvailableBuildsFromNoSuccessfullyImported() {
		
		// Given: Some simple builds and none of them successfully imported.
		List<BranchBuilds> branchBuilds = new ArrayList<BranchBuilds>(Arrays.asList(
				createBranchBuilds("trunk", build5, build1, build6, build3, build7),
				createBranchBuilds("branch", build2, build4)));
		Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = createBuildImportSummaries(branchBuilds);
		
		// When: updating the list of available builds
		availableBuildsList.updateBuildsWithSuccessfullyImportedBuilds(branchBuilds, buildImportSummaries);
		
		// Then: No builds are part of the available builds.
		assertNumberOfBuilds("trunk", 0);
		assertNumberOfBuilds("branch", 0);
	}
	
	@Test
	public void testUpdatingAvailableBuildsFromSuccessfullyImported() {
		
		// Given: Some simple builds and some of them successfully imported.
		List<BranchBuilds> branchBuilds = new ArrayList<BranchBuilds>(Arrays.asList(
				createBranchBuilds("trunk", build5, build1, build6, build3, build7),
				createBranchBuilds("branch", build2, build4)));
		Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = createBuildImportSummaries(branchBuilds,
				"build1", "build2", "build3", "build4", "build5");
		
		// When: updating the list of available builds
		availableBuildsList.updateBuildsWithSuccessfullyImportedBuilds(branchBuilds, buildImportSummaries);
		
		// Then: only the successfully imported builds are part of the available list and the aliases ("recent" and
		// "current") are added correctly and in expected order.
		assertNumberOfBuilds("trunk", 5); // 3 builds plus 2 aliases
		assertAliasContainedAtPosition("trunk", DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD, 0, build3.getBuild());
		assertAliasContainedAtPosition("trunk", DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD, 1, build5.getBuild());
		assertBuildContainedAtPosition("trunk", 2, build5);
		assertBuildContainedAtPosition("trunk", 4, build1);
		assertNumberOfBuilds("branch", 4); // 2 builds plus 2 aliases
		assertAliasContainedAtPosition("branch", DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD, 0, build2.getBuild());
		assertAliasContainedAtPosition("branch", DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD, 1, build4.getBuild());
		assertBuildContainedAtPosition("branch", 2, build4);
		assertBuildContainedAtPosition("branch", 3, build2);
	}
	
	@Test
	public void testResolveBuildNamesWithSomeSuccessfullyImportedBuilds() {
		
		// Given: Some simple builds and some of them successfully imported.
		List<BranchBuilds> branchBuilds = new ArrayList<BranchBuilds>(Arrays.asList(
				createBranchBuilds("trunk", build5, build1, build6, build3, build7),
				createBranchBuilds("branch", build2, build4)));
		Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = createBuildImportSummaries(branchBuilds,
				"build1", "build2", "build3", "build4", "build5");
		
		// When: updating the list of available builds and resolving some build names
		availableBuildsList.updateBuildsWithSuccessfullyImportedBuilds(branchBuilds, buildImportSummaries);
		String trunkCurrentBuildName = availableBuildsList.resolveAliasBuildName("trunk",
				DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD);
		String trunkRecentBuildName = availableBuildsList.resolveAliasBuildName("trunk",
				DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD);
		String trunkBuild1BuildName = availableBuildsList.resolveAliasBuildName("trunk", "build1");
		String branchCurrentBuildName = availableBuildsList.resolveAliasBuildName("branch",
				DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD);
		String branchRecentBuildName = availableBuildsList.resolveAliasBuildName("branch",
				DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD);
		
		// Then: Resolved build names are as expected (resolving aliases works)
		assertEquals("build3", trunkCurrentBuildName);
		assertEquals("build5", trunkRecentBuildName);
		assertEquals("build1", trunkBuild1BuildName);
		assertEquals("build2", branchCurrentBuildName);
		assertEquals("build4", branchRecentBuildName);
	}
	
	@Test
	public void testAddImportedBuilds() {
		
		// Given: Some simple builds and none successfully imported yet.
		List<BranchBuilds> branchBuilds = new ArrayList<BranchBuilds>(Arrays.asList(
				createBranchBuilds("trunk", build5, build1, build3, build7),
				createBranchBuilds("branch", build2, build4, build6)));
		Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = createBuildImportSummaries(branchBuilds);
		
		// When: updating the list of available builds and adding some imported builds afterwards.
		availableBuildsList.updateBuildsWithSuccessfullyImportedBuilds(branchBuilds, buildImportSummaries);
		availableBuildsList.addImportedBuild(getImportedBuildSummary(buildImportSummaries, "trunk", "build1"));
		availableBuildsList.addImportedBuild(getImportedBuildSummary(buildImportSummaries, "trunk", "build3"));
		availableBuildsList.addImportedBuild(getImportedBuildSummary(buildImportSummaries, "trunk", "build5"));
		availableBuildsList.addImportedBuild(getImportedBuildSummary(buildImportSummaries, "branch", "build4"));
		availableBuildsList.addImportedBuild(getImportedBuildSummary(buildImportSummaries, "branch", "build6"));
		
		// Then: the imported builds are part of available builds and aliases are set correctly, all in expected order.
		assertNumberOfBuilds("trunk", 5); // 3 build plus 2 aliases
		assertAliasContainedAtPosition("trunk", DEFAULT_ALIAS_FOR_LAST_SUCCESSFUL_BUILD, 0, build3.getBuild());
		assertAliasContainedAtPosition("trunk", DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD, 1, build5.getBuild());
		assertBuildContainedAtPosition("trunk", 2, build5);
		assertBuildContainedAtPosition("trunk", 4, build1);
		assertNumberOfBuilds("branch", 3); // 2 builds plus 1 alias (no "current" because none successful)
		assertAliasContainedAtPosition("branch", DEFAULT_ALIAS_FOR_MOST_RECENT_BUILD, 0, build6.getBuild());
		assertBuildContainedAtPosition("branch", 1, build6);
		assertBuildContainedAtPosition("branch", 2, build4);
	}
	
	private BuildImportSummary getImportedBuildSummary(
			final Map<BuildIdentifier, BuildImportSummary> buildImportSummaries, final String branchName,
			final String buildName) {
		BuildIdentifier buildId = new BuildIdentifier(branchName, buildName);
		BuildImportSummary summary = buildImportSummaries.get(buildId);
		summary.setStatus(BuildImportStatus.SUCCESS);
		return summary;
	}
	
	private void assertBuildContainedAtPosition(final String branchName, final int index, final BuildLink expectedBuild) {
		BranchBuilds branchBuilds = getBranchBuilds(branchName);
		assertEquals("Expected build at position " + index + " for branch '" + branchName + "'.", expectedBuild,
				branchBuilds.getBuilds().get(index));
	}
	
	private void assertAliasContainedAtPosition(final String branchName, final String aliasName, final int index,
			final Build expectedBuild) {
		BranchBuilds branchBuilds = getBranchBuilds(branchName);
		BuildLink buildAlias = branchBuilds.getBuilds().get(index);
		assertEquals("Expected build alias at position " + index + " for branch '" + branchName + "'.", aliasName,
				buildAlias.getLinkName());
		assertSame("Expected build at position " + index + " for branch '" + branchName + "'.", expectedBuild,
				buildAlias.getBuild());
	}
	
	private void assertNumberOfBuilds(final String branchName, final int numberOfBuilds) {
		BranchBuilds branchBuilds = getBranchBuilds(branchName);
		assertEquals("Expected number of builds for branch '" + branchName + "'.", numberOfBuilds, branchBuilds
				.getBuilds().size());
	}
	
	private BranchBuilds getBranchBuilds(final String branchName) {
		BranchBuilds branchBuilds = availableBuildsList.getBranchBuilds(branchName);
		assertNotNull("Expected branch '" + branchName + "' in availableBuildsList.", branchBuilds);
		assertEquals("Expected correct branch name.", branchName, branchBuilds.getBranch().getName());
		return branchBuilds;
	}
	
	private Map<BuildIdentifier, BuildImportSummary> createBuildImportSummaries(
			final List<BranchBuilds> branchBuildsList, final String... successfulBuildNames) {
		Map<BuildIdentifier, BuildImportSummary> buildSummaries = new HashMap<BuildIdentifier, BuildImportSummary>();
		Set<String> successfulBuildNamesSet = new HashSet<String>(Arrays.asList(successfulBuildNames));
		for (BranchBuilds branchBuilds : branchBuildsList) {
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				BuildIdentifier buildIdentifier = new BuildIdentifier(branchBuilds.getBranch().getName(), buildLink
						.getBuild().getName());
				BuildImportSummary buildSummary = new BuildImportSummary(branchBuilds.getBranch().getName(),
						buildLink.getBuild());
				if (successfulBuildNamesSet.contains(buildLink.getLinkName())) {
					buildSummary.setStatus(BuildImportStatus.SUCCESS);
				}
				buildSummaries.put(buildIdentifier, buildSummary);
			}
		}
		return buildSummaries;
	}
	
	private BranchBuilds createBranchBuilds(final String branchName, final BuildLink... builds) {
		BranchBuilds result = new BranchBuilds();
		result.setBranch(new Branch(branchName));
		result.setBuilds(new ArrayList<BuildLink>(Arrays.asList(builds)));
		return result;
	}
	
	private BuildLink createBuildSuccess(final String name, final int minuteOfDate) {
		return createNewBuild(name, name, "success", minuteOfDate);
	}
	
	private BuildLink createBuildFailed(final String name, final int minuteOfDate) {
		return createNewBuild(name, name, "failed", minuteOfDate);
	}
	
	/**
	 * Create a usual build containing a valid date.
	 */
	private BuildLink createNewBuild(final String aliasName, final String name, final String status,
			final int minuteOfDate) {
		Build build = new Build(name);
		BuildLink result = new BuildLink(build, aliasName);
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MINUTE, minuteOfDate);
		build.setDate(cal.getTime());
		build.setRevision("1234");
		build.setStatus(status);
		return result;
	}
	
}
