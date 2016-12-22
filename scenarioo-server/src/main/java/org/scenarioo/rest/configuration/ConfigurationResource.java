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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.dao.diffViewer.GraphicsMagickConfiguration;
import org.scenarioo.dao.version.ApplicationVersionHolder;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.search.SearchEngineStatus;

@Path("/rest/configuration/")
public class ConfigurationResource {

	private static final Logger LOGGER = Logger.getLogger(ConfigurationResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GET
	@Produces({ "application/json", "application/xml" })
	public GetConfigurationResponse getConfiguration() {
		return new GetConfigurationResponse(configurationRepository.getConfiguration(), configurationRepository
				.getDocumentationDataDirectory().getAbsolutePath());
	}

	@GET
	@Path("/applicationStatus")
	@Produces({ "application/json", "application/xml" })
	public ApplicationStatus getApplicationStatus() {
		ApplicationStatus applicationStatus = new ApplicationStatus();

		DiffViewerStatus status = new DiffViewerStatus();
		status.setGraphicsMagickAvailable(GraphicsMagickConfiguration.isAvailable());
		applicationStatus.setDiffViewerStatus(status);
		applicationStatus.setConfiguration(configurationRepository.getConfiguration());
		applicationStatus.setSearchEngineStatus(SearchEngineStatus.create());
		applicationStatus.setVersion(ApplicationVersionHolder.INSTANCE.getApplicationVersion());
		return applicationStatus;
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public void updateConfiguration(final Configuration configuration) {
		LOGGER.info("Saving configuration.");
		configurationRepository.updateConfiguration(configuration);
	}

}
