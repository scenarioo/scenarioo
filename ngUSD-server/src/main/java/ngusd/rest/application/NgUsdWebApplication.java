package ngusd.rest.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ngusd.configuration.NgusdConfiguration;
import ngusd.manager.UserScenarioDocuManager;

/**
 * Ng USD Application context that initializes data on startup of server.
 */
public class NgUsdWebApplication implements ServletContextListener {
	
	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		System.out.println("====================================");
		System.out.println("ngUSD Application starting up ...");
		System.out.println("====================================");
		System.out.println("  Updating documentation content directory ...");
		System.out.println("  Configured documentation content directory: " +
				NgusdConfiguration.DOCU_ROOT_DIR);
		System.out.println("  Processing documentation content data and calculating aggregated data, " +
				"this may take a while ...");
		UserScenarioDocuManager.INSTANCE.updateAll();
		System.out.println("  Documentation content directory has been processed and updated.");
		System.out.println("====================================");
		System.out.println("ngUSD Application started succesfully.");
		System.out.println("====================================");
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		System.out.println("ngUSD Application got destroyed");
	}
	
}
