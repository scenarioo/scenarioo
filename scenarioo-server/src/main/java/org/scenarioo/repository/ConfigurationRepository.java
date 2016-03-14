package org.scenarioo.repository;

import java.io.File;

import org.scenarioo.dao.configuration.ConfigurationDao;
import org.scenarioo.model.configuration.Configuration;

public class ConfigurationRepository {

	private final ConfigurationDao configurationDAO;
	private Configuration configuration;

	public ConfigurationRepository(final ConfigurationDao configurationDao) {
		this.configurationDAO = configurationDao;
	}

	public Configuration getConfiguration() {
		loadConfigurationIfNotLoadedYet();
		return configuration;
	}

	private void loadConfigurationIfNotLoadedYet() {
		if (configuration == null) {
			configuration = configurationDAO.loadConfiguration();
		}
	}

	public Configuration updateConfiguration(final Configuration configuration) {
		configurationDAO.updateConfiguration(configuration);
		this.configuration = configurationDAO.loadConfiguration();
		return this.configuration;
	}

	public File getDocumentationDataDirectory() {
		return configurationDAO.getConfigurationDirectory();
	}
	
	public File getDesignDataDirectory() {
		File rootFolder = getDocumentationDataDirectory();
		return new File(rootFolder, "scenarioo-application-data/sketcher");
	}

}
