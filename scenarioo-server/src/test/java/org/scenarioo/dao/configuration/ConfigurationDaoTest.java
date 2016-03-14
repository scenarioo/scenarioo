package org.scenarioo.dao.configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.configuration.LabelConfiguration;

/**
 * Smoke tests for reading and writing a configuration.
 */
public class ConfigurationDaoTest {
	
	private final ConfigurationDao configurationDao = new ConfigurationDaoImpl("tmp");
	
	@AfterClass
	public static void removeTemporaryData() throws IOException {
		FileUtils.deleteDirectory(new File("tmp"));
	}
	
	@Test
	public void writeAndReadConfiguration() {
		Configuration configuration = new Configuration();
		Map<String, LabelConfiguration> labelConfigurations = createLabelConfigurations();
		configuration.setLabelConfigurations(labelConfigurations);
		
		configurationDao.updateConfiguration(configuration);
		Configuration loadedConfiguration = configurationDao.loadConfiguration();
		
		assertEquals(labelConfigurations, loadedConfiguration.getLabelConfigurations());
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
	
}
