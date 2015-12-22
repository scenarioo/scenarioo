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

package org.scenarioo.rest.sketcher.stepSketch;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.sketcher.SketcherFiles;
import org.scenarioo.dao.sketcher.SketcherReader;
import org.scenarioo.model.sketcher.StepSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/stepsketch")
public class StepSketchResource {

	private static final Logger LOGGER = Logger.getLogger(StepSketchResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final SketcherFiles files = new SketcherFiles(configurationRepository.getDesignDataDirectory());
	private final SketcherReader reader = new SketcherReader(configurationRepository.getDesignDataDirectory());

	private final ScenarioDocuFiles docuFiles = new ScenarioDocuFiles(
			configurationRepository.getDocumentationDataDirectory());

	@GET
	@Produces({ "application/json" })
	@Path("/{stepSketchId}")
	public StepSketch loadStepSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("stepSketchId") final String stepSketchId) {

		LOGGER.info("REQUEST: loadStepSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketchId + ")");

		return reader.loadStepSketch(branchName, issueId, scenarioSketchId, stepSketchId);
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response storeStepSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final StepSketch stepSketch) {

		LOGGER.info("REQUEST: storeStepSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketch + ")");

		stepSketch.setStepSketchId("1");
		Date now = new Date();
		stepSketch.setDateCreated(now);
		stepSketch.setDateModified(now);

		files.createStepSketchDirectory(branchName, issueId, scenarioSketchId, stepSketch.getStepSketchId());
		files.persistStepSketch(branchName, issueId, scenarioSketchId, stepSketch);

		stepSketch.setSvgXmlString(SvgSanitizer.sanitize(stepSketch.getSvgXmlString()));
		files.persistSketchAsSvgAndPng(branchName, issueId, scenarioSketchId, stepSketch);

		copyOriginalScreenshot(branchName, issueId, scenarioSketchId, stepSketch);

		return Response.ok(stepSketch, MediaType.APPLICATION_JSON).build();
	}

	private void copyOriginalScreenshot(final String branchName, final String issueId, final String scenarioSketchId,
			final StepSketch stepSketch) {
		File originalScreenshot = null;
		if (stepSketch.getContextInDocu() == null) {
			return;
		}

		// TODO: contextInDocu should not be transfered as string but as an object
		final HashMap<String, String> stepInfo = parseContextInDocu(stepSketch.getContextInDocu());
		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				stepInfo.get("branch"),
				stepInfo.get("build"));
		originalScreenshot = docuFiles.getScreenshotFile(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(),
				stepInfo.get("usecase"),
				stepInfo.get("scenario"), Integer.parseInt(stepInfo.get("stepIndex")));

		if (originalScreenshot == null) {
			return;
		}

		files.copyOriginalScreenshot(originalScreenshot, branchName, issueId, scenarioSketchId,
				stepSketch.getStepSketchId());
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

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@Path("/{stepSketchId}")
	public Response updateStepSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("stepSketchId") final String stepSketchId, final StepSketch updatedStepSketch) {

		LOGGER.info("REQUEST: updateStepSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketchId + ")");

		final StepSketch stepSketch = reader.loadStepSketch(branchName, issueId, scenarioSketchId,
				stepSketchId);
		
		stepSketch.setSvgXmlString(SvgSanitizer.sanitize(updatedStepSketch.getSvgXmlString()));
		stepSketch.setDateModified(new Date());

		files.persistSketchAsSvgAndPng(branchName, issueId, scenarioSketchId, stepSketch);

		return Response.ok(stepSketch, MediaType.APPLICATION_JSON).build();
	}

}
