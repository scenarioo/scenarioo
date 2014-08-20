package org.scenarioo.repository;

/**
 * Offers access to all existing repositories.
 */
public enum RepositoryLocator {
	
	INSTANCE;
	
	private final ConfigurationRepository configurationRepository = new ConfigurationRepository();
	
	public ConfigurationRepository getConfigurationRepository() {
		return configurationRepository;
	}
	
}
