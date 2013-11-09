package ngusd.rest.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ngusd.dao.ConfigurationDAO;
import ngusd.manager.UserScenarioDocuManager;
import ngusd.model.configuration.Configuration;

import org.apache.log4j.Logger;

/**
 * Ng USD Application context that initializes data on startup of server.
 */
public class NgUsdWebApplication implements ServletContextListener {
	
	private static final Logger LOGGER = Logger.getLogger(NgUsdWebApplication.class);
	
	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		LOGGER.info("====================================");
		LOGGER.info("ngUSD Application starting up ...");
		LOGGER.info("====================================");
		LOGGER.info("  Loading configuration ...");
		
		String configurationDirectory = arg0.getServletContext().getInitParameter("configurationDirectory");
		LOGGER.info("  " + configurationDirectory);
		ConfigurationDAO.setConfigurationDirectory(configurationDirectory);
		Configuration config = ConfigurationDAO.getConfiguration();
		LOGGER.info("  Configuration loaded.");
		LOGGER.info("  Updating documentation content directory ...");
		LOGGER.info("  Configured documentation content directory: " +
				config.getTestDocumentationDirPath());
		
		UserScenarioDocuManager.INSTANCE.updateAll();
		
		LOGGER.info("====================================");
		LOGGER.info("ngUSD Application started succesfully.");
		LOGGER.info("====================================");
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		LOGGER.info("====================================");
		LOGGER.info("ngUSD Application got destroyed");
		LOGGER.info("====================================");
	}
}
