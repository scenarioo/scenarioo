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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Provides methods to get the configured path to the documentation and the config file name.
 */
class ScenariooDataPathLogic {

	private static final Logger LOGGER = Logger.getLogger(ScenariooDataPathLogic.class);
	private static final String USER_HOME_BASE_DIRECTORY = ".scenarioo";

	private final SystemEnvironment systemEnvironment;

	ScenariooDataPathLogic(final SystemEnvironment systemEnvironment) {
		this.systemEnvironment = systemEnvironment;
	}

	String getDataPath(final ServletContext servletContext) {
		String configSource = "servlet context";
		String configurationDirectory = servletContext.getInitParameter(
				"scenariooDataDirectory");

		if (StringUtils.isBlank(configurationDirectory)) {
			configSource = "SCENARIOO_DATA environment variable or scenarioo.data application property";
			configurationDirectory = systemEnvironment.getScenariooDataDirectory();
		}

		if (StringUtils.isBlank(configurationDirectory)) {
			configSource = "default scenarioo data directory";
			configurationDirectory = new File(systemEnvironment.getUserHome(), USER_HOME_BASE_DIRECTORY).getAbsolutePath();
		}

		LOGGER.info("   Taking documentation data path from " + configSource + ": " + configurationDirectory);
		return configurationDirectory;
	}
}
