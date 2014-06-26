package org.scenarioo.dao.configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;

/**
 * Smoke tests for reading and writing a configuration. 
 */
public class ConfigurationDAOTest {
	
	
	@Before
	public void init() {
		ConfigurationDAO.setConfigurationDirectory("tmp");
	}
	
	@After
	public void reset() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}
	
	@Test
	public void writeRead() {
		Configuration configuration = new Configuration();
		List<LabelConfiguration> labelConfigurations = createLabelConfigurations();
		configuration.setLabelConfigurations(labelConfigurations);
		
		ConfigurationDAO.updateConfiguration(configuration);
		Configuration loadedConfiguration = ConfigurationDAO.loadConfiguration();
		
		assertEquals(labelConfigurations, loadedConfiguration.getLabelConfigurations());
	}

	private List<LabelConfiguration> createLabelConfigurations() {
		List<LabelConfiguration> labelConfigurations = new LinkedList<>();
		labelConfigurations.add(createLabelConfig("test", "#1231231", "#1234231"));
		labelConfigurations.add(createLabelConfig("test2", "red", "#1234231"));
		labelConfigurations.add(createLabelConfig("test3", "black", "#1234231"));
		
		return labelConfigurations;
	}

	private LabelConfiguration createLabelConfig(String labelName, String foregroundColor, String backgroundColor) {
		LabelConfiguration labelConfig = new LabelConfiguration();
		labelConfig.setName(labelName);
		labelConfig.setForegroundColor(foregroundColor);
		labelConfig.setBackgroundColor(backgroundColor);
		return labelConfig;
	}
}
