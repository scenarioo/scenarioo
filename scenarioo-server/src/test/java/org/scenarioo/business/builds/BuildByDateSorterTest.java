package org.scenarioo.business.builds;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Build;

public class BuildByDateSorterTest {

	private final BuildImportSummary OLDER_BUILD = createBuildImportSummary(new Date(1000L));
	private final BuildImportSummary MIDDLE_BUILD = createBuildImportSummary(new Date(2000L));
	private final BuildImportSummary NEWER_BUILD = createBuildImportSummary(new Date(3000L));
	private final BuildImportSummary BUILD_WITHOUT_DATE = createBuildImportSummary(null);

	@Test
	public void sortBuildsByDateDescending_givenTwoBuildsInAscendingOder_resultsInTwoBuildsInDescendingOrder() {
		List<BuildImportSummary> buildsAscending = new LinkedList<BuildImportSummary>();
		buildsAscending.add(OLDER_BUILD);
		buildsAscending.add(NEWER_BUILD);
		
		List<BuildImportSummary> buildsByDateDescending = BuildByDateSorter.sortBuildsByDateDescending(buildsAscending);
		
		assertBuildDescriptionIsEqual(NEWER_BUILD, buildsByDateDescending.get(0));
		assertBuildDescriptionIsEqual(OLDER_BUILD, buildsByDateDescending.get(1));
	}

	@Test
	public void sortBuildsByDateDescending_givenOneBuildWithoutDate_buildWithDateComesFirst() {
		List<BuildImportSummary> buildsAscending = new LinkedList<BuildImportSummary>();
		buildsAscending.add(BUILD_WITHOUT_DATE);
		buildsAscending.add(NEWER_BUILD);

		List<BuildImportSummary> buildsByDateDescending = BuildByDateSorter.sortBuildsByDateDescending(buildsAscending);

		assertBuildDescriptionIsEqual(NEWER_BUILD, buildsByDateDescending.get(0));
		assertBuildDescriptionIsEqual(BUILD_WITHOUT_DATE, buildsByDateDescending.get(1));
	}

	@Test
	public void sortBuildsByDateDescending_givenFourBuilds_theyAreSortedInDescendingOrder() {
		List<BuildImportSummary> buildsAscending = new LinkedList<BuildImportSummary>();
		buildsAscending.add(BUILD_WITHOUT_DATE);
		buildsAscending.add(NEWER_BUILD);
		buildsAscending.add(MIDDLE_BUILD);
		buildsAscending.add(OLDER_BUILD);

		List<BuildImportSummary> buildsByDateDescending = BuildByDateSorter.sortBuildsByDateDescending(buildsAscending);

		assertBuildDescriptionIsEqual(NEWER_BUILD, buildsByDateDescending.get(0));
		assertBuildDescriptionIsEqual(MIDDLE_BUILD, buildsByDateDescending.get(1));
		assertBuildDescriptionIsEqual(OLDER_BUILD, buildsByDateDescending.get(2));
		assertBuildDescriptionIsEqual(BUILD_WITHOUT_DATE, buildsByDateDescending.get(3));
	}

	private BuildImportSummary createBuildImportSummary(final Date date) {
		BuildImportSummary summary = new BuildImportSummary();
		summary.setBuildDescription(createBuildDescription(date));
		return summary;
	}

	private Build createBuildDescription(final Date date) {
		Build build = new Build();
		build.setDate(date);
		return build;
	}

	private void assertBuildDescriptionIsEqual(final BuildImportSummary expected, final BuildImportSummary actual) {
		Assert.assertEquals(expected.getBuildDescription(), actual.getBuildDescription());
	}

}
