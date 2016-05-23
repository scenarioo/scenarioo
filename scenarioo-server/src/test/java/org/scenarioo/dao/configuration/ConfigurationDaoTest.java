package org.scenarioo.dao.configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;

/**
 * Smoke tests for reading and writing a configuration.
 */
public class ConfigurationDaoTest {

	private static final String BASE_BRANCH_NAME = "baseBranchName";
	private static final String COMPARISON_BRANCH_NAME = "comparisonBranchName";
	private static final String COMPARISON_BUILD_NAME = "comparisonBuildName";
	private static final String COMPARISON_NAME1 = "comparisonName1";
	private static final String COMPARISON_NAME2 = "comparisonName2";
	private final ConfigurationDao configurationDao = new ConfigurationDaoImpl("tmp", null);

	@AfterClass
	public static void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}

	@Test
	public void writeAndReadConfiguration() {
		final Configuration configuration = new Configuration();
		final Map<String, LabelConfiguration> labelConfigurations = createLabelConfigurations();
		configuration.setLabelConfigurations(labelConfigurations);
		configuration.setComparisonConfigurations(createComparisonConfigurations());

		configurationDao.updateConfiguration(configuration);
		final Configuration loadedConfiguration = configurationDao.loadConfiguration();

		assertEquals(labelConfigurations, loadedConfiguration.getLabelConfigurations());
		assertEquals(2, loadedConfiguration.getComparisonConfigurations().size());
		assertComparisonConfiguration(COMPARISON_NAME1, loadedConfiguration.getComparisonConfigurations().get(0));
		assertComparisonConfiguration(COMPARISON_NAME2, loadedConfiguration.getComparisonConfigurations().get(1));
	}

	private Map<String, LabelConfiguration> createLabelConfigurations() {
		final Map<String, LabelConfiguration> labelConfigurations = new LinkedHashMap<String, LabelConfiguration>();
		labelConfigurations.put("test", createLabelConfig("#1231231", "#1234231"));
		labelConfigurations.put("test2", createLabelConfig("red", "#1234231"));
		labelConfigurations.put("test3", createLabelConfig("black", "#1234231"));

		return labelConfigurations;
	}

	private LabelConfiguration createLabelConfig(final String foregroundColor, final String backgroundColor) {
		final LabelConfiguration labelConfig = new LabelConfiguration();
		labelConfig.setForegroundColor(foregroundColor);
		labelConfig.setBackgroundColor(backgroundColor);
		return labelConfig;
	}

	private void assertComparisonConfiguration(final String expectedComparisonName,
			final ComparisonConfiguration comparisonConfiguration) {
		assertEquals(expectedComparisonName, comparisonConfiguration.getName());
		assertEquals(BASE_BRANCH_NAME, comparisonConfiguration.getBaseBranchName());
		assertEquals(COMPARISON_BRANCH_NAME, comparisonConfiguration.getComparisonBranchName());
		assertEquals(COMPARISON_BUILD_NAME, comparisonConfiguration.getComparisonBuildName());
	}

	private List<ComparisonConfiguration> createComparisonConfigurations() {
		final List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(createComparisonConfiguration(COMPARISON_NAME1));
		comparisonConfigurations.add(createComparisonConfiguration(COMPARISON_NAME2));
		return comparisonConfigurations;
	}

	private ComparisonConfiguration createComparisonConfiguration(final String comparisonName) {
		final ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setBaseBranchName(BASE_BRANCH_NAME);
		comparisonConfiguration.setComparisonBranchName(COMPARISON_BRANCH_NAME);
		comparisonConfiguration.setComparisonBuildName(COMPARISON_BUILD_NAME);
		comparisonConfiguration.setName(comparisonName);
		return comparisonConfiguration;
	}

}
