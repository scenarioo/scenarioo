package org.scenarioo.business.diffViewer.comparator;

import org.scenarioo.dao.diffViewer.DiffViewerBuildWriter;
import org.scenarioo.model.configuration.ComparisonConfiguration;

import java.awt.*;

public class ComparisonParameters {

	private final String baseBranchName;
	private final String baseBuildName;
	private final ComparisonConfiguration comparisonConfiguration;
	private final Color diffImageColor;
	private final DiffViewerBuildWriter diffWriter;

	public ComparisonParameters(String baseBranchName, String baseBuildName, ComparisonConfiguration comparisonConfiguration,
								Color diffImageColor) {

		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonConfiguration = comparisonConfiguration;
		this.diffImageColor = diffImageColor;

		diffWriter = new DiffViewerBuildWriter(baseBranchName, baseBuildName,
			comparisonConfiguration.getName());
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

	public DiffViewerBuildWriter getDiffWriter() { return diffWriter; }

}
