package org.scenarioo.repository;

import org.scenarioo.dao.configuration.ConfigurationDao;
import org.scenarioo.dao.configuration.ConfigurationDaoImpl;
import org.scenarioo.dao.configuration.ConfigurationDaoInMemory;

/**
 * Offers access to all existing repositories.
 */
public enum RepositoryLocator {
	
	INSTANCE;
	
	private ConfigurationRepository configurationRepository;
	
	public void initializeConfigurationRepository(final String configurationDirectory,
			final String configurationFilename) {
		ConfigurationDao configurationDao = new ConfigurationDaoImpl(configurationDirectory, configurationFilename);
		configurationRepository = new ConfigurationRepository(configurationDao);
	}
	
	public void initializeConfigurationRepositoryForUnitTest() {
		ConfigurationDao configurationDao = new ConfigurationDaoInMemory();
		configurationRepository = new ConfigurationRepository(configurationDao);
	}
	
	public ConfigurationRepository getConfigurationRepository() {
		if (configurationRepository == null) {
			throw new IllegalStateException(
					"configurationRepository is not initialized. Call initializeConfigurationRepository() or initializeConfigurationRepositoryForUnitTest() first.");
		}
		return configurationRepository;
	}
	
}
