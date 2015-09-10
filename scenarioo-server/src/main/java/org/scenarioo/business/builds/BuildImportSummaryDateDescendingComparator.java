package org.scenarioo.business.builds;

import java.util.Comparator;
import java.util.Date;

import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;

public class BuildImportSummaryDateDescendingComparator implements Comparator<BuildImportSummary>  {

	@Override
	public int compare(final BuildImportSummary left, final BuildImportSummary right) {
		Date leftDate = getDate(left);
		Date rightDate = getDate(right);

		if (leftDate == null && rightDate == null) {
			return 0;
		}

		if (leftDate == null && rightDate != null) {
			return 1;
		}

		if (leftDate != null && rightDate == null) {
			return -1;
		}

		return rightDate.compareTo(leftDate);
	}

	private Date getDate(final BuildImportSummary left) {
		if (left == null || left.getBuildDescription() == null) {
			return null;
		}

		return left.getBuildDescription().getDate();
	}

}
