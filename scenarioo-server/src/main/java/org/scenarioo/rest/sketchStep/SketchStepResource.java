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

package org.scenarioo.rest.sketchStep;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.design.aggregates.ScenarioSketchAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.entities.SketchStep;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.sketchStep.logic.ProposalLoader;
import org.scenarioo.rest.sketchStep.logic.SketchStepIndexResolver;
import org.scenarioo.rest.sketchStep.logic.SketchStepLoader;
import org.scenarioo.rest.sketchStep.logic.SketchStepLoaderResult;
import org.scenarioo.rest.sketchStep.logic.SketchStepResponseFactory;
import org.scenarioo.rest.step.StepResource;
import org.scenarioo.rest.step.logic.LabelsQueryParamParser;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/sketchstep/{sketchStepName}")
public class SketchStepResource {

	private static final Logger LOGGER = Logger.getLogger(StepResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();
	private final ScenarioSketchAggregationDAO aggregatedDataReader = new ScenarioSketchAggregationDAO(
			configurationRepository.getDesignDataDirectory(), longObjectNamesResolver);

	private final LabelsQueryParamParser labelsQueryParamParser = new LabelsQueryParamParser();
	private final ProposalLoader proposalLoader = new ProposalLoader(aggregatedDataReader);
	private final SketchStepIndexResolver sketchStepIndexResolver = new SketchStepIndexResolver();
	private final SketchStepLoader sketchStepLoader = new SketchStepLoader(proposalLoader, sketchStepIndexResolver);

	// todo: ProposalReader (?)
	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			configurationRepository.getDesignDataDirectory());

	private final SketchStepResponseFactory sketchStepResponseFactory = new SketchStepResponseFactory(
			aggregatedDataReader,
			scenarioDocuReader);

	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());



	/**
	 * Get a step with all its data (meta data, html, ...) together with additional calculated navigation data
	 */
	@GET
	@Produces({ "application/json" })
	public Response loadSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("sketchStepName") final int sketchStepName,
			@QueryParam("fallback") final boolean addFallbackInfo, @QueryParam("labels") final String labels) {

		BuildIdentifier buildIdentifierBeforeAliasResolution = new BuildIdentifier(branchName, "");
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				"");

		StepIdentifier sketchStepIdentifier = new StepIdentifier(buildIdentifier, issueId, scenarioSketchId, "",
				0, sketchStepName, labelsQueryParamParser.parseLabels(labels));

		LOGGER.info("loadSketchStep(" + sketchStepIdentifier + ")");

		SketchStepLoaderResult sketchStepLoaderResult = sketchStepLoader.loadStep(sketchStepIdentifier);

		return sketchStepResponseFactory.createResponse(sketchStepLoaderResult, sketchStepIdentifier,
				buildIdentifierBeforeAliasResolution, addFallbackInfo);
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json", "application/xml" })
	public Response storeSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final SketchStep sketchStep) {
		LOGGER.info("Now storing a sketchStep.");
		LOGGER.info(sketchStep);
		LOGGER.info("-----------------------------------");
		files.writeSketchStepToFile(branchName, issueId, scenarioSketchId, sketchStep);
		files.writeSVGToFile(branchName, issueId, scenarioSketchId, sketchStep);
		return Response.ok().build();
	}

}
