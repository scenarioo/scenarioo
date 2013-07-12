package ngusd.dao;

import java.io.File;

import ngusd.model.configuration.Configuration;

/**
 * DAO for accessing user scenario docu content from filesystem, that is either
 * generated or already precalculated.
 * 
 * The DAO should in general only access data by reading one file and should not
 * have to calculate additional data or read a lot of different files or even
 * strip unwanted data.
 * 
 * Data that is not available directly from a file should be precalculated in
 * {@link UserScenarioDocuAggregator} to make it easily available for DAO.
 */
public class ConfigurationDAO {
	
	private static final String CONFIG_PATH = "/home/ngUSD/webtestDocuContentExample/config.xml";
	private static Configuration configuration;
	
	public static Configuration getConfiguration() {
		if (configuration == null) {
			configuration = loadConfiguration();
		}
		return configuration;
	}
	
	private static Configuration loadConfiguration() {
		File configFile = getConfigFile();
		return XMLFileUtil.unmarshal(configFile, Configuration.class);
	}
	
	public static Configuration updateConfiguration(final Configuration configuration) {
		File configFile = getConfigFile();
		XMLFileUtil.marshal(configuration, configFile, Configuration.class);
		ConfigurationDAO.configuration = loadConfiguration();
		return configuration;
	}
	
	private static File getConfigFile() {
		File configFile = new File(CONFIG_PATH);
		if (!configFile.exists()) {
			throw new RuntimeException("Can't find the configuration: " + CONFIG_PATH);
		}
		return configFile;
	}
}
