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

package org.scenarioo.rest.diffViewer;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Handles requests for comparison configurations.
 */
@Path("/rest/diffViewer")
public class ComparisonConfigurationResource {

	private static final Logger LOGGER = Logger.getLogger(ComparisonConfigurationResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GET
	@Produces("application/json")
	@Path("/comparisonConfiguration/comparisonName/{comparisonName}")
	public ComparisonConfiguration getComparisonConfiguration(@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getComparisonConfiguration(" + comparisonName + ")");

		return getComparisonConfigurationByName(comparisonName);
	}

	private ComparisonConfiguration getComparisonConfigurationByName(final String comparisonName) {
		final Configuration configuration = configurationRepository.getConfiguration();
		final List<ComparisonConfiguration> comparisonConfigurations = configuration.getComparisonConfigurations();
		for (final ComparisonConfiguration comparisonConfiguration : comparisonConfigurations) {
			if (comparisonConfiguration.getName().equals(comparisonName)) {
				return comparisonConfiguration;
			}
		}
		throw new ResourceNotFoundException(
				"No comparisonConfiguration found for comparisonName '" + comparisonName + "'.");
	}

}
