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

package org.scenarioo.rest.application;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Scenarioo REST Services Web Application context that initializes data on startup of server.
 */
public class ScenariooWebApplication implements ServletContextListener {
	
	private static final Logger LOGGER = Logger.getLogger(ScenariooWebApplication.class);
	private final DocumentationPathLogic documentationPathLogic = new DocumentationPathLogic(new SystemWrapper());

	@Override
	public void contextInitialized(final ServletContextEvent servletContextEvent) {
		LOGGER.info("====================================================");
		LOGGER.info("Scenarioo Viewer is starting up ...");
		LOGGER.info("====================================================");
		
		initializeApplicationVersion(servletContextEvent.getServletContext());
		loadConfiguration(servletContextEvent);
		
		LOGGER.info("  Updating documentation content directory (will be done asynchronously ...)");
		ScenarioDocuBuildsManager.INSTANCE.updateAllBuildsAndSubmitNewBuildsForImport();
		
		LOGGER.info("====================================================");
		LOGGER.info("Scenarioo Viewer started succesfully.");
		LOGGER.info("====================================================");
	}
	
	private void loadConfiguration(final ServletContextEvent servletContextEvent) {
		LOGGER.info("  Loading configuration ...");
		
		String configurationDirectory = documentationPathLogic
				.getDocumentationPath(servletContextEvent);
		String configurationFilename = documentationPathLogic
				.getConfigFilenameFromServletContext(servletContextEvent);
		
		RepositoryLocator.INSTANCE.initializeConfigurationRepository(configurationDirectory, configurationFilename);
		RepositoryLocator.INSTANCE.getConfigurationRepository().getConfiguration();

		LOGGER.info("  Configuration loaded.");
	}
	
	private void initializeApplicationVersion(final ServletContext servletContext) {
		Properties properties = new Properties();
		InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/classes/version.properties");
		
		if (inputStream == null) {
			LOGGER.warn("  version.properties not found, no version information available");
			ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown");
			return;
		}
		
		try {
			properties.load(inputStream);
			ApplicationVersionHolder.INSTANCE.initializeFromProperties(properties);
		} catch (Exception e) {
			ApplicationVersionHolder.INSTANCE.initialize("unknown", "unknown", "unknown", "unknown");
			e.printStackTrace();
		}
		
		LOGGER.info("  Version: " + ApplicationVersionHolder.INSTANCE.getApplicationVersion().getVersion());
		LOGGER.info("  Build date: " + ApplicationVersionHolder.INSTANCE.getApplicationVersion().getBuildDate());
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		LOGGER.info("===================================================");
		LOGGER.info("Scenarioo Viewer stopped.");
		LOGGER.info("===================================================");
	}

}
