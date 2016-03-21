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

package org.scenarioo.rest.configuration;

import org.scenarioo.model.configuration.Configuration;

/**
 * Defines the representation of the configuration data against the frontend.
 * Contains other data that is not included in the config file itself.
 */
public class GetConfigurationResponse {

	private final Configuration configuration;
	private final String dataDirectory;

	public GetConfigurationResponse(final Configuration configuration, final String dataDirectory) {
		this.configuration = configuration;
		this.dataDirectory = dataDirectory;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

}
