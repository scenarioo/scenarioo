package org.scenarioo.repository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.dao.configuration.ConfigurationDao;
import org.scenarioo.model.configuration.Configuration;

public class ConfigurationRepository {

	private static final Logger LOGGER = Logger.getLogger(ConfigurationRepository.class);
	private static String EXAMPLE_DOCUMENTATION_DIRECTORY = "documentationExample";
	private static String EXAMPLE_DESIGN_DIRECTORY = "designExample";

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
		loadConfigurationIfNotLoadedYet();
		String documentationDataDirectoryFromConfig = configuration.getTestDocumentationDirPath();

		if (StringUtils.isBlank(documentationDataDirectoryFromConfig)) {
			return getExampleDocumentationDirectoryAsFallback();
		} else {
			return new File(documentationDataDirectoryFromConfig);
		}
	}

	public File getDesignDataDirectory() {
		loadConfigurationIfNotLoadedYet();
		String designDataDirectoryFromConfig = configuration.getDesignDocumentationDirPath();

		if (StringUtils.isBlank(designDataDirectoryFromConfig)) {
			return getExampleDesignDirectoryAsFallback();
		} else {
			return new File(designDataDirectoryFromConfig);
		}
	}

	private File getExampleDocumentationDirectoryAsFallback() {
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
	}

	private File getExampleDesignDirectoryAsFallback() {
		final URL exampleDesignDataPath = Configuration.class.getClassLoader().getResource(
				EXAMPLE_DESIGN_DIRECTORY);
		File exampleDesignDataDirectoryPath = null;
		try {
			if (exampleDesignDataPath != null) {
				exampleDesignDataDirectoryPath = new File(exampleDesignDataPath.toURI());
			}
		} catch (final URISyntaxException e) {
			LOGGER.error("Example design data is not accessible in resources.", e);
			return null;
		}
		if (exampleDesignDataDirectoryPath == null || !exampleDesignDataDirectoryPath.exists()) {
			LOGGER.error("Example design data is missing in resources.");
			return null;
		}
		return exampleDesignDataDirectoryPath;
	}

}
