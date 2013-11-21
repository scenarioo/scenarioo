package org.scenarioo.rest.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.scenarioo.dao.ConfigurationDAO;
import org.scenarioo.manager.ScenarioDocuBuildsManager;
import org.scenarioo.model.configuration.Configuration;

/**
 * Scenarioo REST Services Web Application context that initializes data on startup of server.
 */
public class ScenariooWebApplication implements ServletContextListener {
	
	private static final Logger LOGGER = Logger.getLogger(ScenariooWebApplication.class);
	
	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		LOGGER.info("====================================================");
		LOGGER.info("Scenarioo webapplication server is starting up ...  ");
		LOGGER.info("====================================================");
		LOGGER.info("  Loading configuration ...");
		
		String configurationDirectory = arg0.getServletContext().getInitParameter("configurationDirectory");
		LOGGER.info("  " + configurationDirectory);
		ConfigurationDAO.setConfigurationDirectory(configurationDirectory);
		Configuration config = ConfigurationDAO.getConfiguration();
		LOGGER.info("  Configuration loaded.");
		LOGGER.info("  Configured documentation content directory: " +
				config.getTestDocumentationDirPath());
		LOGGER.info("  Updating documentation content directory (will be done asynchronously ...)");
		
		ScenarioDocuBuildsManager.INSTANCE.updateAll();
		
		LOGGER.info("====================================================");
		LOGGER.info("Scenarioo webapplication server started succesfully.");
		LOGGER.info("====================================================");
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		LOGGER.info("===================================================");
		LOGGER.info("Scenarioo webapplication stopped.");
		LOGGER.info("===================================================");
	}
}
