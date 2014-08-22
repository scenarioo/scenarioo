package org.scenarioo.repository;

import java.io.File;

import org.scenarioo.dao.configuration.ConfigurationDao;
import org.scenarioo.dao.configuration.ConfigurationDaoImpl;
import org.scenarioo.dao.configuration.ConfigurationDaoInMemory;

/**
 * Offers access to all existing repositories.
 */
public enum RepositoryLocator {
	
	INSTANCE;
	
	private ConfigurationRepository configurationRepository;
	private LastSuccessfulScenarioBuildRepository lastSuccessfulScenarioBuildRepository;
	
	private RepositoryLocator() {
		
	}
	
	public void initializeConfigurationRepository(final String configurationDirectory,
			final String configurationFilename) {
		ConfigurationDao configurationDao = new ConfigurationDaoImpl(configurationDirectory, configurationFilename);
		configurationRepository = new ConfigurationRepository(configurationDao);
		initializeRepositories();
	}
	
	public void initializeConfigurationRepositoryForUnitTest(final File documentationDataDirectory) {
		ConfigurationDao configurationDao = new ConfigurationDaoInMemory(documentationDataDirectory);
		configurationRepository = new ConfigurationRepository(configurationDao);
		initializeRepositories();
	}
	
	private void initializeRepositories() {
		lastSuccessfulScenarioBuildRepository = new LastSuccessfulScenarioBuildRepository(configurationRepository);
	}
	
	public ConfigurationRepository getConfigurationRepository() {
		if (configurationRepository == null) {
			throw new IllegalStateException(
					"configurationRepository is not initialized. Call initializeConfigurationRepository() or initializeConfigurationRepositoryForUnitTest() first.");
		}
		return configurationRepository;
	}
	
	public LastSuccessfulScenarioBuildRepository getLastSuccessfulScenarioBuildRepository() {
		return lastSuccessfulScenarioBuildRepository;
	}
	
}
