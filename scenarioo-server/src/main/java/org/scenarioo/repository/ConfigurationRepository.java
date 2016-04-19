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
		// TODO #478: I think we should be consistent "design data" or "sketcher" ?? Better use one or the other!
		// I recommend to use "design-data" in the path name, because we might want to use it for more than just for sketches, right?
		// This was once designed to contain all additional design data, that could be attached to scenario docu data.
		File rootFolder = getDocumentationDataDirectory();
		return new File(rootFolder, "scenarioo-application-data/sketcher");
	}

	public File getDiffViewerDirectory() {
		File rootFolder = getDocumentationDataDirectory();
		return new File(rootFolder, "scenarioo-application-data/diffViewer");
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

}
