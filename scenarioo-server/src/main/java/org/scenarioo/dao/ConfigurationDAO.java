package org.scenarioo.dao;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.configuration.Configuration;

/**
 * DAO for accessing USD configuration data from filesysem.
 */
public class ConfigurationDAO {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationDAO.class);
	
	private static String EXAMPLE_DOCUMENTATION_DIRECTORY = "documentationExample";
	
	private static final String CONFIG_FILE_NAME = "config.xml";
	private static final String DEFAULT_CONFIG_PATH = CONFIG_FILE_NAME;
	
	private static Configuration configuration = null;
	
	private static String configurationDirectory = null;
	
	public static void setConfigurationDirectory(final String configurationDirectory) {
		ConfigurationDAO.configurationDirectory = configurationDirectory;
		
	}
	
	public static Configuration getConfiguration() {
		if (configuration == null) {
			configuration = loadConfiguration();
		}
		return configuration;
	}
	
	private static Configuration loadConfiguration() {
		File configFile = getConfigFile();
		
		LOGGER.info("  loading configuration from file: " + configFile);
		if (!configFile.exists()) {
			LOGGER.warn("  file " + configFile + " does not exist: " + configFile.canRead());
			configFile = getDefaultConfigFile();
		}
		return ScenarioDocuXMLFileUtil.unmarshal(Configuration.class, configFile);
	}
	
	public static Configuration updateConfiguration(final Configuration configuration) {
		File configFile = getConfigFile();
		File configDirectory = configFile.getParentFile();
		configDirectory.mkdirs();
		ScenarioDocuXMLFileUtil.marshal(configuration, configFile);
		ConfigurationDAO.configuration = loadConfiguration();
		return configuration;
	}
	
	/**
	 * Get the place where customized configuration file is or will be stored (as soon as first configuration change has
	 * been applied).
	 */
	private static File getConfigFile() {
		if (StringUtils.isBlank(configurationDirectory)) {
			configurationDirectory = System.getProperty("user.home");
			if (StringUtils.isBlank(configurationDirectory)) {
				configurationDirectory = "";
			}
		}
		File configurationPath = new File(configurationDirectory);
		File configFile = new File(configurationPath, CONFIG_FILE_NAME);
		return configFile;
	}
	
	private static File getDefaultConfigFile() {
		URL resourceUrl = ConfigurationDAO.class.getClassLoader().getResource(DEFAULT_CONFIG_PATH);
		File defaultConfigFile = null;
		try {
			defaultConfigFile = new File(resourceUrl.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Default configuration file is not accessable.", e);
		}
		if (defaultConfigFile == null || !defaultConfigFile.exists()) {
			throw new IllegalStateException("Default configuration file is missing.");
		}
		return defaultConfigFile;
	}
	
	public static File getDocuDataDirectoryPath() {
		if (StringUtils.isBlank(configuration.getTestDocumentationDirPath())) {
			URL exampleDocuDataPath = Configuration.class.getClassLoader().getResource(EXAMPLE_DOCUMENTATION_DIRECTORY);
			File exampleDocuDataDirectoryPath = null;
			try {
				if (exampleDocuDataPath != null) {
					exampleDocuDataDirectoryPath = new File(exampleDocuDataPath.toURI());
				}
			} catch (URISyntaxException e) {
				LOGGER.error("Example documentation data is not accessible in resources.", e);
				return null;
			}
			if (exampleDocuDataDirectoryPath == null || !exampleDocuDataDirectoryPath.exists()) {
				LOGGER.error("Example documentation data is missing in resources.");
				return null;
			}
			return exampleDocuDataDirectoryPath;
		}
		else {
			return new File(configuration.getTestDocumentationDirPath());
		}
	}
}
