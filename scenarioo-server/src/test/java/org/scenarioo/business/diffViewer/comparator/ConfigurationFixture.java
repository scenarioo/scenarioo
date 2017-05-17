package org.scenarioo.business.diffViewer.comparator;

import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.RepositoryLocator;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class ConfigurationFixture {
	public static String BASE_BRANCH_NAME = "baseBranch";
	public static String BASE_BUILD_NAME = "baseBuild";
	public static String COMPARISON_BRANCH_NAME = "comparisonBranch";
	public static String COMPARISON_BUILD_NAME = "comparisonBuild";
	public static String COMPARISON_NAME = "comparisonName";

	public static ComparisonParameters COMPARATOR_PARAMETERS = new ComparisonParameters(BASE_BRANCH_NAME, BASE_BUILD_NAME,
		getComparisonConfiguration(), new Color(255, 0, 0, 200));

	public static Configuration getTestConfiguration() {

		ComparisonConfiguration comparisonConfiguration = getComparisonConfiguration();

		List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(comparisonConfiguration);

		Configuration configuration = RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();
		configuration.setComparisonConfigurations(comparisonConfigurations);

		return configuration;
	}

	public static ComparisonConfiguration getComparisonConfiguration(String baseBranch, String comparisonBranch, String comparisonBuild, String comparisonName) {
		ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(baseBranch);
		comparisonConfiguration.setComparisonBranchName(comparisonBranch);
		comparisonConfiguration.setComparisonBuildName(comparisonBuild);
		comparisonConfiguration.setName(comparisonName);
		return comparisonConfiguration;
	}

	public static ComparisonConfiguration getComparisonConfiguration() {
		return getComparisonConfiguration(BASE_BRANCH_NAME, COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME, COMPARISON_NAME);
	}
}
