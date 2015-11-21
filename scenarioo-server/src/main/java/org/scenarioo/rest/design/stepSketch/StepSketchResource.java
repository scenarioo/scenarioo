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

package org.scenarioo.rest.design.stepSketch;

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
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.design.DesignFiles;
import org.scenarioo.dao.design.DesignReader;
import org.scenarioo.model.design.entities.StepSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/sketchstep")
public class StepSketchResource {

	private static final Logger LOGGER = Logger.getLogger(StepSketchResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

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
	public StepSketch loadSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("sketchStepName") final int sketchStepName,
			@QueryParam("fallback") final boolean addFallbackInfo, @QueryParam("labels") final String labels) {

		LOGGER.info("Now loading a SketchStep");
		return reader.loadSketchStep(branchName, issueId, scenarioSketchId, sketchStepName);
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json", "application/xml" })
	public Response storeSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final StepSketch sketchStep) {

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
		sketchStep.setSketchStepName(1);
		sketchStep.setDateCreated(System.currentTimeMillis());
		sketchStep.setDateModified(System.currentTimeMillis());
		files.writeSketchStepToFile(branchName, issueId, scenarioSketchId, sketchStep);
		sketchStep.setSketch(SVGSanitizerIE.sanitize(sketchStep.getSketch()));
		files.writeSVGToFile(branchName, issueId, scenarioSketchId, sketchStep);
		if (originalScreenshot != null) {
			files.copyOriginalScreenshot(originalScreenshot, branchName, issueId, scenarioSketchId);
		}
		return Response.ok(sketchStep, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json", "application/xml" })
	@Path("/{sketchStepName}")
	public Response updateSketchStep(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("sketchStepName") final int sketchStepName, final StepSketch updatedSketchStep) {

		LOGGER.info("Now updating a sketchStep.");
		LOGGER.info(updatedSketchStep);
		LOGGER.info("-----------------------");
		if (updatedSketchStep.getSketchStepName() == 0) {
			LOGGER.error("There was no sketchStepName set on the sketchStep object!");
			updatedSketchStep.setSketchStepName(sketchStepName);
		}
		final StepSketch existingSketchStep = reader.loadSketchStep(branchName, issueId, scenarioSketchId,
				sketchStepName);
		existingSketchStep.update(updatedSketchStep);
		existingSketchStep.setDateModified(System.currentTimeMillis());
		// files.updateSketchStep(branchName, existingSketchStep);

		files.writeSVGToFile(branchName, issueId, scenarioSketchId, existingSketchStep);

		return Response.ok(updatedSketchStep, MediaType.APPLICATION_JSON).build();
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
