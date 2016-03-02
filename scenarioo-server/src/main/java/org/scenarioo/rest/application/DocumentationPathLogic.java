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

import java.io.File;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Provides methods to get the configured path to the documentation and the config file name.
 */
class DocumentationPathLogic {

	private static final Logger LOGGER = Logger.getLogger(DocumentationPathLogic.class);
	private static final String USER_HOME_BASE_DIRECTORY = ".scenarioo";

	private final SystemWrapper systemWrapper;

	public DocumentationPathLogic(final SystemWrapper systemWrapper) {
		this.systemWrapper = systemWrapper;
	}

	String getDocumentationPath(final ServletContextEvent servletContextEvent) {
		String configSource = "servlet context";
		String configurationDirectory = servletContextEvent.getServletContext().getInitParameter(
				"scenariooConfigurationDirectory");

		if (StringUtils.isBlank(configurationDirectory)) {
			configSource = "servlet context (old property name)";
			configurationDirectory = servletContextEvent.getServletContext().getInitParameter("configurationDirectory");
		}

		if (StringUtils.isBlank(configurationDirectory)) {
			configSource = "SCENARIOO_HOME environment variable";
			configurationDirectory = systemWrapper.getEnv("SCENARIOO_HOME");
		}

		if (StringUtils.isBlank(configurationDirectory)) {
			configSource = "default scenarioo home directory";
			configurationDirectory = new File(systemWrapper.getUserHome(), USER_HOME_BASE_DIRECTORY).getAbsolutePath();
		}

		LOGGER.info("   Configured documentation directory:  " + configurationDirectory + " from " + configSource);
		return configurationDirectory;
	}

	String getConfigFilenameFromServletContext(final ServletContextEvent servletContextEvent) {
		String configurationFilename = servletContextEvent.getServletContext().getInitParameter(
				"scenariooConfigurationFilename");
		if (StringUtils.isBlank(configurationFilename)) {
			// Fallback to old property name:
			configurationFilename = servletContextEvent.getServletContext().getInitParameter("configurationFilename");
		}
		if (StringUtils.isNotBlank(configurationFilename)) {
			LOGGER.info("   Overriding default configuration filename config.xml with:  " + configurationFilename);
			return configurationFilename;
		}
		return null;
	}
}
