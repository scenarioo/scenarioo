package org.scenarioo.dao.configuration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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
		Map<String, LabelConfiguration> labelConfigurations = createLabelConfigurations();
		configuration.setLabelConfigurations(labelConfigurations);
		
		ConfigurationDAO.updateConfiguration(configuration);
		Configuration loadedConfiguration = ConfigurationDAO.loadConfiguration();
		
		assertEquals(labelConfigurations, loadedConfiguration.getLabelConfigurations());
	}

	private Map<String, LabelConfiguration> createLabelConfigurations() {
		Map<String, LabelConfiguration> labelConfigurations = new LinkedHashMap<>();
		labelConfigurations.put("test", createLabelConfig("#1231231", "#1234231"));
		labelConfigurations.put("test2", createLabelConfig("red", "#1234231"));
		labelConfigurations.put("test3", createLabelConfig("black", "#1234231"));
		
		return labelConfigurations;
	}

	private LabelConfiguration createLabelConfig(String foregroundColor, String backgroundColor) {
		LabelConfiguration labelConfig = new LabelConfiguration();
		labelConfig.setForegroundColor(foregroundColor);
		labelConfig.setBackgroundColor(backgroundColor);
		return labelConfig;
	}
}
