/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.design.aggregates.ScenarioSketchAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.entities.SketchStep;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.sketchStep.logic.ProposalLoader;
import org.scenarioo.rest.sketchStep.logic.SketchStepIndexResolver;
import org.scenarioo.rest.sketchStep.logic.SketchStepLoader;
import org.scenarioo.rest.sketchStep.logic.SketchStepResponseFactory;
import org.scenarioo.rest.step.logic.LabelsQueryParamParser;
import org.scenarioo.utils.design.readers.DesignReader;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/sketchstep")
public class SketchStepResource {

	private static final Logger LOGGER = Logger.getLogger(SketchStepResource.class);

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
	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());

	private final ScenarioDocuFiles docuFiles = new ScenarioDocuFiles(
			configurationRepository.getDocumentationDataDirectory());

	/**
	 * Get a step with all its data (meta data, html, ...) together with additional calculated navigation data
	 */
	@GET
	@Produces({ "application/json" })
	@Path("/{sketchStepName}")
	public SketchStep loadSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("sketchStepName") final int sketchStepName,
			@QueryParam("fallback") final boolean addFallbackInfo, @QueryParam("labels") final String labels) {

		return reader.loadSketchStep(branchName, issueId, scenarioSketchId, sketchStepName);

		/*
		 * final BuildIdentifier buildIdentifierBeforeAliasResolution = new BuildIdentifier(branchName, "");
		 * final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
		 * branchName,
		 * "");
		 * 
		 * final StepIdentifier sketchStepIdentifier = new StepIdentifier(buildIdentifier, issueId, scenarioSketchId,
		 * "",
		 * 0, sketchStepName, labelsQueryParamParser.parseLabels(labels));
		 * 
		 * LOGGER.info("loadSketchStep(" + sketchStepIdentifier + ")");
		 * 
		 * final SketchStepLoaderResult sketchStepLoaderResult = sketchStepLoader.loadStep(sketchStepIdentifier);
		 * 
		 * return sketchStepResponseFactory.createResponse(sketchStepLoaderResult, sketchStepIdentifier,
		 * buildIdentifierBeforeAliasResolution, addFallbackInfo);
		 */
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json", "application/xml" })
	public Response storeSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final SketchStep sketchStep) {

		File originalScreenshot = null;
		if (sketchStep.getContextInDocu() != null) {
			// TODO: Extract method "find original screenshot"
			final HashMap<String, String> stepInfo = parseContextInDocu(sketchStep.getContextInDocu());
			final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
					stepInfo.get("branch"),
					stepInfo.get("build"));
			originalScreenshot = docuFiles.getScreenshotFile(buildIdentifier.getBranchName(),
					buildIdentifier.getBuildName(),
					stepInfo.get("usecase"),
					stepInfo.get("scenario"), Integer.parseInt(stepInfo.get("stepIndex")));
		}
		LOGGER.info("Now storing a sketchStep.");
		LOGGER.info(sketchStep);
		LOGGER.info("-----------------------------------");
		files.writeSketchStepToFile(branchName, issueId, scenarioSketchId, sketchStep);
		files.writeSVGToFile(branchName, issueId, scenarioSketchId, sketchStep);
		if (originalScreenshot != null) {
			files.copyOriginalScreenshot(originalScreenshot, branchName, issueId, scenarioSketchId);
		}
		return Response.ok(sketchStep, MediaType.APPLICATION_JSON).build();
	}

	private HashMap<String, String> parseContextInDocu(final String toParse) {
		final String stripped = toParse.substring(toParse.indexOf("branch"));
		final String[] data = stripped.split("/");
		final HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < data.length; i = i + 2) {
			result.put(data[i], data[i + 1]);
		}
		result.put("stepIndex", result.get("image").split(Pattern.quote("."))[0]);
		return result;
	}
}
