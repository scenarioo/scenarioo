package org.scenarioo.dao.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.scenarioo.business.diffViewer.comparator.ConfigurationFixture.*;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.scenarioo.business.diffViewer.comparator.ConfigurationFixture;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;

/**
 * Smoke tests for reading and writing a configuration.
 */
public class ConfigurationDaoTest {

	private static final String COMPARISON_NAME1 = "comparisonName1";
	private static final String COMPARISON_NAME2 = "comparisonName2";
	private ConfigurationDao configurationDao;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void init() throws IOException {
		configurationDao = new ConfigurationDaoImpl(folder.newFolder().getPath(), null);
	}

	@Test
	public void writeAndReadConfiguration() {
		Configuration configuration = new Configuration();
		Map<String, LabelConfiguration> labelConfigurations = createLabelConfigurations();
		configuration.setLabelConfigurations(labelConfigurations);
		configuration.setComparisonConfigurations(createComparisonConfigurations());
		configuration.setDiffImageColor(Color.yellow);

		configurationDao.updateConfiguration(configuration);
		Configuration loadedConfiguration = configurationDao.loadConfiguration();

		assertEquals(labelConfigurations, loadedConfiguration.getLabelConfigurations());
		assertEquals(2, loadedConfiguration.getComparisonConfigurations().size());
		assertComparisonConfiguration(COMPARISON_NAME1, loadedConfiguration.getComparisonConfigurations().get(0));
		assertComparisonConfiguration(COMPARISON_NAME2, loadedConfiguration.getComparisonConfigurations().get(1));

		assertThat(loadedConfiguration.getDiffImageColor(), is(Color.yellow));
	}

	private Map<String, LabelConfiguration> createLabelConfigurations() {
		Map<String, LabelConfiguration> labelConfigurations = new LinkedHashMap<String, LabelConfiguration>();
		labelConfigurations.put("test", createLabelConfig("#1231231", "#1234231"));
		labelConfigurations.put("test2", createLabelConfig("red", "#1234231"));
		labelConfigurations.put("test3", createLabelConfig("black", "#1234231"));

		return labelConfigurations;
	}

	private LabelConfiguration createLabelConfig(final String foregroundColor, final String backgroundColor) {
		LabelConfiguration labelConfig = new LabelConfiguration();
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
		List<ComparisonConfiguration> comparisonConfigurations = new LinkedList<ComparisonConfiguration>();
		comparisonConfigurations.add(createComparisonConfiguration(COMPARISON_NAME1));
		comparisonConfigurations.add(createComparisonConfiguration(COMPARISON_NAME2));
		return comparisonConfigurations;
	}

	private ComparisonConfiguration createComparisonConfiguration(final String comparisonName) {
		return ConfigurationFixture.getComparisonConfiguration(BASE_BRANCH_NAME, COMPARISON_BRANCH_NAME, COMPARISON_BUILD_NAME, comparisonName);
	}

}
