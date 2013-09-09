package ngusd.dao;

import java.io.File;

import ngusd.dao.filesystem.XMLFileUtil;
import ngusd.model.configuration.Configuration;

/**
 * DAO for accessing USD configuration data from filesysem.
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
