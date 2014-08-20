package org.scenarioo.repository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.configuration.Configuration;

public class ConfigurationRepository {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationRepository.class);
	private static String EXAMPLE_DOCUMENTATION_DIRECTORY = "documentationExample";
	
	private String configurationDirectory = null;
	private String configurationFilename = null;
	private ConfigurationDAO configurationDAO;
	private Configuration configuration;
	
	public ConfigurationRepository() {
		initializeConfigurationDao();
	}
	
	public void setConfigurationDirectory(final String configurationDirectory) {
		this.configurationDirectory = configurationDirectory;
		initializeConfigurationDao();
	}
	
	public void setConfigurationFilename(final String configurationFilename) {
		this.configurationFilename = configurationFilename;
		initializeConfigurationDao();
	}
	
	private void initializeConfigurationDao() {
		configurationDAO = new ConfigurationDAO(configurationDirectory, configurationFilename);
		// delete currently loaded configuration, because the directory or the filename could have changed
		configuration = null;
	}
	
	public Configuration getConfiguration() {
		if (configuration == null) {
			configuration = configurationDAO.loadConfiguration();
		}
		return configuration;
	}
	
	public Configuration updateConfiguration(final Configuration configuration) {
		configurationDAO.updateConfiguration(configuration);
		this.configuration = configurationDAO.loadConfiguration();
		return this.configuration;
	}
	
	// TODO [repository] Refactor so that we don't need this.
	/**
	 * Only for testing
	 */
	public void injectConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}
	
	public File getDocuDataDirectoryPath() {
		if (StringUtils.isBlank(configuration.getTestDocumentationDirPath())) {
			final URL exampleDocuDataPath = Configuration.class.getClassLoader().getResource(
					EXAMPLE_DOCUMENTATION_DIRECTORY);
			File exampleDocuDataDirectoryPath = null;
			try {
				if (exampleDocuDataPath != null) {
					exampleDocuDataDirectoryPath = new File(exampleDocuDataPath.toURI());
				}
			} catch (final URISyntaxException e) {
				LOGGER.error("Example documentation data is not accessible in resources.", e);
				return null;
			}
			if (exampleDocuDataDirectoryPath == null || !exampleDocuDataDirectoryPath.exists()) {
				LOGGER.error("Example documentation data is missing in resources.");
				return null;
			}
			return exampleDocuDataDirectoryPath;
		} else {
			return new File(configuration.getTestDocumentationDirPath());
		}
	}
	
}
