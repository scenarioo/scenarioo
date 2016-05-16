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
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Handles requests for step diff information.
 */
@Path("/rest/diffViewer/{baseBranchName}/{baseBuildName}/{comparisonName}/{useCaseName}/{scenarioName}/")
public class StepDiffResource {

	private static final Logger LOGGER = Logger.getLogger(StepDiffResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReaderXmlImpl(configurationRepository.getDiffViewerDirectory());

	@GET
	@Produces("application/json")
	@Path("/{stepIndex}/stepDiffInfo")
	public Response getStepDiffInfo(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName,
			@PathParam("stepIndex") final String stepIndex) {

		LOGGER.info("REQUEST: getStepDiffInfo(" + baseBranchName + ", " + comparisonName
				+ ", " + useCaseName + ", " + scenarioName + ", " + stepIndex + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		try {
			final StepDiffInfo stepDiffInfo = diffReader.loadStepDiffInfo(buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(),
					comparisonName, useCaseName, scenarioName, Integer.parseInt(stepIndex));
			return Response.ok(stepDiffInfo, MediaType.APPLICATION_JSON).build();
		} catch (final ResourceNotFoundException e) {
			StepDiffInfo stepDiffInfo = new StepDiffInfo();
			stepDiffInfo.setChangeRate(100);
			LOGGER.warn("Unable to get step diff info", e);
			return Response.ok(stepDiffInfo, MediaType.APPLICATION_JSON).build();
		} catch (final Throwable e) {
			LOGGER.warn("Unable to get step diff info", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/stepDiffInfos")
	public Response getStepDiffInfos(@PathParam("baseBranchName") final String baseBranchName,
			@PathParam("baseBuildName") final String baseBuildName,
			@PathParam("comparisonName") final String comparisonName,
			@PathParam("useCaseName") final String useCaseName,
			@PathParam("scenarioName") final String scenarioName) {
		LOGGER.info("REQUEST: getStepDiffInfos(" + baseBranchName + ", " + baseBranchName + ", " + comparisonName
				+ ", " + useCaseName + ", " + scenarioName + ")");

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				baseBranchName,
				baseBuildName);

		try {
			final List<StepDiffInfo> stepDiffInfos = diffReader.loadStepDiffInfos(buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(),
					comparisonName, useCaseName, scenarioName);

			return Response.ok(getStepDiffInfoMap(stepDiffInfos), MediaType.APPLICATION_JSON).build();
		} catch (final ResourceNotFoundException e) {
			LOGGER.warn("Unable to get scenario diff infos", e);
			return Response.noContent().build();
		} catch (final Throwable e) {
			LOGGER.warn("Unable to get scenario diff infos", e);
			return Response.serverError().build();
		}
	}

	private Map<Integer, StepDiffInfo> getStepDiffInfoMap(final List<StepDiffInfo> stepDiffInfos) {
		final Map<Integer, StepDiffInfo> stepDiffInfoMap = new HashMap<Integer, StepDiffInfo>();
		for (final StepDiffInfo stepDiffInfo : stepDiffInfos) {
			stepDiffInfoMap.put(stepDiffInfo.getIndex(), stepDiffInfo);
		}
		return stepDiffInfoMap;
	}
}
