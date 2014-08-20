package org.scenarioo.dao.configuration;

import org.scenarioo.model.configuration.Configuration;

/**
 * For unit tests.
 */
public class ConfigurationDaoInMemory implements ConfigurationDao {
	
	private Configuration configuration = new Configuration();
	
	@Override
	public Configuration loadConfiguration() {
		return configuration;
	}
	
	@Override
	public void updateConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}
	
}
