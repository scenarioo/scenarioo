package org.scenarioo.business.diffViewer.comparator;

import org.scenarioo.model.configuration.ComparisonConfiguration;

import java.awt.*;

public class ComparatorParameter {
	private final String baseBranchName;
	private final String baseBuildName;
	private final ComparisonConfiguration comparisonConfiguration;
	private final Color diffImageColor;

	public ComparatorParameter(String baseBranchName, String baseBuildName, ComparisonConfiguration comparisonConfiguration,
							   Color diffImageColor) {

		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonConfiguration = comparisonConfiguration;
		this.diffImageColor = diffImageColor;
	}

	public String getBaseBranchName() {
		return baseBranchName;
	}

	public String getBaseBuildName() {
		return baseBuildName;
	}

	public ComparisonConfiguration getComparisonConfiguration() {
		return comparisonConfiguration;
	}

	public Color getDiffImageColor() {
		return diffImageColor;
	}
}
