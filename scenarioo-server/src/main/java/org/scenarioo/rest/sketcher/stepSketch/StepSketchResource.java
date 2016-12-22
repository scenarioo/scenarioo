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
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.sketcher.StepSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.step.logic.ResolveStepIndexResult;
import org.scenarioo.rest.step.logic.StepIndexResolver;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchId}/stepsketch")
public class StepSketchResource {

	private static final Logger LOGGER = Logger.getLogger(StepSketchResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final SketcherDao sketcherDao = new SketcherDao();

	private final ScenarioDocuFiles docuFiles = new ScenarioDocuFiles(
			configurationRepository.getDocumentationDataDirectory());
	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();
	private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory(), longObjectNamesResolver);
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();

	@GET
	@Produces({ "application/json" })
	@Path("/{stepSketchId}")
	public StepSketch loadStepSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId,
			@PathParam("stepSketchId") final String stepSketchId) {

		LOGGER.info("REQUEST: loadStepSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketchId + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		return sketcherDao.loadStepSketch(resolvedBranchName, issueId, scenarioSketchId, stepSketchId);
	}

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response storeStepSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final StepSketch stepSketch) {

		LOGGER.info("REQUEST: storeStepSketch(" + branchName + ", " + issueId + ", " + scenarioSketchId + ", "
				+ stepSketch + ")");

		BuildIdentifier resolvedBranchAndBuildAlias = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				stepSketch.getRelatedStep().getBranchName(), stepSketch.getRelatedStep().getBuildName());
		stepSketch.getRelatedStep().setBranchName(resolvedBranchAndBuildAlias.getBranchName());
		stepSketch.getRelatedStep().setBuildName(resolvedBranchAndBuildAlias.getBuildName());
		String resolvedBranchName = resolvedBranchAndBuildAlias.getBranchName();

		stepSketch.setStepSketchId("1");
		Date now = new Date();
		stepSketch.setDateCreated(now);
		stepSketch.setDateModified(now);

		sketcherDao.persistStepSketch(resolvedBranchName, issueId, scenarioSketchId, stepSketch);

		stepSketch.setSvgXmlString(SvgSanitizer.sanitize(stepSketch.getSvgXmlString()));
		sketcherDao.persistSketchAsSvgAndPng(resolvedBranchName, issueId, scenarioSketchId, stepSketch);

		copyOriginalScreenshot(resolvedBranchName, issueId, scenarioSketchId, stepSketch);

		return Response.ok(stepSketch, MediaType.APPLICATION_JSON).build();
	}

	private void copyOriginalScreenshot(final String branchName, final String issueId, final String scenarioSketchId,
			final StepSketch stepSketch) {
		File originalScreenshot = null;
		if (stepSketch.getRelatedStep() == null) {
			return;
		}
		
		StepIdentifier relatedStep = stepSketch.getRelatedStep();
		ResolveStepIndexResult stepIndex = resolveStepIndex(relatedStep);
		File screenshotsDirectory = docuFiles.getScreenshotsDirectory(relatedStep.getBranchName(),
				relatedStep.getBuildName(), relatedStep.getUsecaseName(), relatedStep.getScenarioName());
		originalScreenshot = new File(screenshotsDirectory, stepIndex.getScreenshotFileName());

		sketcherDao.copyOriginalScreenshot(originalScreenshot, branchName, issueId, scenarioSketchId,
				stepSketch.getStepSketchId());
	}

	private ResolveStepIndexResult resolveStepIndex(final StepIdentifier relatedStep) {
		ScenarioPageSteps scenario = aggregatedDataReader.loadScenarioPageSteps(relatedStep.getScenarioIdentifier());
		ResolveStepIndexResult stepIndex = stepIndexResolver.resolveStepIndex(scenario, relatedStep);
		return stepIndex;
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

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		final StepSketch stepSketch = sketcherDao.loadStepSketch(resolvedBranchName, issueId, scenarioSketchId,
				stepSketchId);
		
		stepSketch.setSvgXmlString(SvgSanitizer.sanitize(updatedStepSketch.getSvgXmlString()));
		stepSketch.setDateModified(new Date());

		sketcherDao.persistSketchAsSvgAndPng(resolvedBranchName, issueId, scenarioSketchId, stepSketch);

		return Response.ok(stepSketch, MediaType.APPLICATION_JSON).build();
	}

}
