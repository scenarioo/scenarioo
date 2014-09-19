package org.scenarioo.dao.configuration;

import org.scenarioo.model.configuration.Configuration;

public interface ConfigurationDao {
	
	public abstract Configuration loadConfiguration();
	
	public abstract void updateConfiguration(final Configuration configuration);
	
}