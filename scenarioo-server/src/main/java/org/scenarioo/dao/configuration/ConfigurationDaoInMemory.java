package org.scenarioo.dao.configuration;

import java.io.File;

import org.scenarioo.model.configuration.Configuration;

/**
 * For unit tests.
 */
public class ConfigurationDaoInMemory implements ConfigurationDao {
	
	private Configuration configuration = new Configuration();
	private File documentationDataDirectory;
	
	public ConfigurationDaoInMemory(final File documentationDataDirectory) {
		if (documentationDataDirectory == null) {
			return;
		}
		this.documentationDataDirectory = documentationDataDirectory;
	}
	
	@Override
	public Configuration loadConfiguration() {
		return configuration;
	}
	
	@Override
	public void updateConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public File getConfigurationDirectory() {
		return documentationDataDirectory;
	}
	
}
