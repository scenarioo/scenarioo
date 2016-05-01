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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Handles requests for global diff informations.
 */
@Path("/rest/diffViewer")
public class GlobalDiffResource {

	private static final Logger LOGGER = Logger.getLogger(GlobalDiffResource.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReader(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/comparisonAlias/{comparisonName}")
	public Response getComparisonAlias(@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getComparisonAlias(" + comparisonName + ")");

		try {
			ComparisonAlias comparisonAlias = diffReader.getComparisonAlias(comparisonName);
			return Response.ok(mapper.writeValueAsString(comparisonAlias), MediaType.APPLICATION_JSON).build();
		} catch (ResourceNotFoundException e) {
			LOGGER.warn("Unable to get comparison alias", e);
			return Response.noContent().build();
		} catch (Throwable e) {
			LOGGER.warn("Unable to get comparison alias", e);
			return Response.serverError().build();
		}
	}

}
