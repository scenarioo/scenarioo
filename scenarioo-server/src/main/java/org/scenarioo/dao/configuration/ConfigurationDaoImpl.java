/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.dao.configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.configuration.Configuration;

/**
 * Responsible for reading and writing the Scenarioo config file. The file is usually named "config.xml", but it can
 * also have a different name.
 */
public class ConfigurationDaoImpl implements ConfigurationDao {

	private static final Logger LOGGER = Logger.getLogger(ConfigurationDaoImpl.class);

	private static final String CONFIG_FILE_NAME = "config.xml";

	private final String configurationDirectory;

	public ConfigurationDaoImpl(final String configurationDirectory) {
		this.configurationDirectory = configurationDirectory;
	}

	@Override
	public Configuration loadConfiguration() {
		Configuration config= loadConfigFile();
		String elasticSearchEndpointOverride = System.getenv("SCENARIOO_ELASTIC_SEARCH_ENDPOINT");
		if (elasticSearchEndpointOverride != null) {
			LOGGER.info("Config elasticSearchEndpoint has been overwritten by environment variable SCENARIOO_ELASTIC_SEARCH_ENDPOINT: " + elasticSearchEndpointOverride);
			config.setElasticSearchEndpoint(elasticSearchEndpointOverride);
		}
		return config;
	}

	private Configuration loadConfigFile() {
		File configFile = getFileSystemConfigFile();
		if (!configFile.exists()) {
			LOGGER.warn("  file " + configFile + " does not exist --> loading default config.xml from classpath");
			return ScenarioDocuXMLFileUtil.unmarshal(Configuration.class, getDefaultConfigFile());
		}
		else {
			LOGGER.info("  loading configuration from file: " + configFile);
			return ScenarioDocuXMLFileUtil.unmarshal(Configuration.class, configFile);
		}
	}

	@Override
	public File getConfigurationDirectory() {
		return new File(configurationDirectory);
	}

	@Override
	public void updateConfiguration(final Configuration configuration) {
		final File configFile = getFileSystemConfigFile();
		final File configDirectory = configFile.getParentFile();
		configDirectory.mkdirs();
		ScenarioDocuXMLFileUtil.marshal(configuration, configFile);
	}

	private File getFileSystemConfigFile() {
		final File configurationPath = new File(configurationDirectory);
		return new File(configurationPath, CONFIG_FILE_NAME);
	}

	private File getDefaultConfigFile() {
		final URL resourceUrl = ConfigurationDaoImpl.class.getClassLoader().getResource(CONFIG_FILE_NAME);
		File defaultConfigFile = null;
		try {
			defaultConfigFile = new File(resourceUrl.toURI());
		} catch (final URISyntaxException e) {
			throw new IllegalStateException("Default configuration file is not accessable.", e);
		}
		if (defaultConfigFile == null || !defaultConfigFile.exists()) {
			throw new IllegalStateException("Default configuration file is missing.");
		}
		return defaultConfigFile;
	}

}
