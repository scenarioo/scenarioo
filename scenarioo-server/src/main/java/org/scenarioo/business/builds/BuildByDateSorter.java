package org.scenarioo.business.builds;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;

public class BuildByDateSorter {

	public static List<BuildImportSummary> sortBuildsByDateDescending(final Collection<BuildImportSummary> unsortedSummaries) {
		List<BuildImportSummary> summaries = new LinkedList<BuildImportSummary>();
		summaries.addAll(unsortedSummaries);

		Collections.sort(summaries, new BuildImportSummaryDateDescendingComparator());

		return summaries;
	}

}
