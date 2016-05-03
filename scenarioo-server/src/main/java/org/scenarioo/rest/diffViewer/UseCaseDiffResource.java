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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Handles requests for use case diff information.
 */
@Path("/rest/diffViewer/{baseBranchName}/{baseBuildName}/{comparisonName}")
public class UseCaseDiffResource {

	private static final Logger LOGGER = Logger.getLogger(UseCaseDiffResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReaderXmlImpl(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/{useCaseName}/useCaseDiffInfo")
	public Response getUseCaseDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName) {
		LOGGER.info("REQUEST: getUseCaseDiffInfo(" + baseBranchName + ", " + baseBuildName + ", " + comparisonName
				+ ", " + useCaseName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		try {
			final UseCaseDiffInfo useCaseDiffInfo = diffReader.loadUseCaseDiffInfo(buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(), comparisonName, useCaseName);
			return Response.ok(useCaseDiffInfo, MediaType.APPLICATION_JSON).build();
		} catch (final ResourceNotFoundException e) {
			LOGGER.warn("Unable to get use case diff info", e);
			return Response.noContent().build();
		} catch (final Throwable e) {
			LOGGER.warn("Unable to get use case diff info", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/useCaseDiffInfos")
	public Response getUseCaseDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName) {
		LOGGER.info("REQUEST: getUseCaseDiffInfos(" + baseBranchName + ", " + baseBuildName + ", " + comparisonName
				+ ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		try {
			final List<UseCaseDiffInfo> useCaseDiffInfos = diffReader.loadUseCaseDiffInfos(
					buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(),
					comparisonName);
			return Response.ok(getUseCaseDiffInfoMap(useCaseDiffInfos), MediaType.APPLICATION_JSON).build();
		} catch (final ResourceNotFoundException e) {
			LOGGER.warn("Unable to get use case diff infos", e);
			return Response.noContent().build();
		} catch (final Throwable e) {
			LOGGER.warn("Unable to get use case diff infos", e);
			return Response.serverError().build();
		}
	}

	private Map<String, UseCaseDiffInfo> getUseCaseDiffInfoMap(final List<UseCaseDiffInfo> useCaseDiffInfos) {
		final Map<String, UseCaseDiffInfo> useCaseDiffInfoMap = new HashMap<String, UseCaseDiffInfo>();
		for (final UseCaseDiffInfo useCaseDiffInfo : useCaseDiffInfos) {
			useCaseDiffInfoMap.put(useCaseDiffInfo.getName(), useCaseDiffInfo);
		}
		return useCaseDiffInfoMap;
	}

}
