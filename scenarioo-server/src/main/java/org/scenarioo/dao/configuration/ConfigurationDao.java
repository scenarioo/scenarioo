package org.scenarioo.dao.configuration;

import java.io.File;

import org.scenarioo.model.configuration.Configuration;

public interface ConfigurationDao {
	
	public Configuration loadConfiguration();
	
	public void updateConfiguration(final Configuration configuration);
	
	public File getConfigurationDirectory();
}