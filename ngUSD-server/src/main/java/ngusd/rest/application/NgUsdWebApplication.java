package ngusd.rest.application;

import java.io.File;

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
		Configuration config = ConfigurationDAO.getConfiguration();
		LOGGER.info("  Configuration loaded.");
		LOGGER.info("  Updating documentation content directory ...");
		LOGGER.info("  Configured documentation content directory: " +
				config.getTestDocumentationDirPath());
		
		File docuDirectory = ConfigurationDAO.getDocuDataDirectoryPath();
		if (!docuDirectory.exists()) {
			LOGGER.error("No valid documentation directory is configured: " + docuDirectory.getAbsolutePath());
			LOGGER.error("Please configure valid documentation directory in configuration UI");
		}
		else {
			LOGGER.info("  Processing documentation content data in directory: " + docuDirectory.getAbsoluteFile());
			LOGGER.info("  Calculating aggregated data in derived XML files, this may take a while ...");
			UserScenarioDocuManager.INSTANCE.updateAll();
			LOGGER.info("  Documentation content directory has been processed and updated.");
		}
		
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
