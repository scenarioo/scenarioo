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

package org.scenarioo.rest.step;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.step.logic.*;

@Path("/rest/branch/{branchName}/build/{buildName}/usecase/{usecaseName}/scenario")
public class StepResource {

	private static final Logger LOGGER = Logger.getLogger(StepResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();
	private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory(), longObjectNamesResolver);

	private final LabelsQueryParamParser labelsQueryParamParser = new LabelsQueryParamParser();
	private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	private final StepLoader stepLoader = new StepLoader(scenarioLoader, stepIndexResolver);

	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());
	private final StepResponseFactory stepResponseFactory = new StepResponseFactory(aggregatedDataReader,
			scenarioDocuReader);
	private final ScreenshotResponseFactory screenshotResponseFactory = new ScreenshotResponseFactory();

	/**
	 * Get a step with all its data (meta data, html, ...) together with additional calculated navigation data
	 */
	@GET
	@Produces({ "application/json" })
	@Path("/{scenarioName}/pageName/{pageName}/pageOccurrence/{pageOccurrence}/stepInPageOccurrence/{stepInPageOccurrence}")
	public Response loadStep(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("pageName") final String pageName,
			@PathParam("pageOccurrence") final int pageOccurrence,
			@PathParam("stepInPageOccurrence") final int stepInPageOccurrence,
			@QueryParam("fallback") final boolean addFallbackInfo, @QueryParam("labels") final String labels) {

		BuildIdentifier buildIdentifierBeforeAliasResolution = new BuildIdentifier(branchName, buildName);
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName, scenarioName, pageName,
				pageOccurrence, stepInPageOccurrence, labelsQueryParamParser.parseLabels(labels));

		LOGGER.info("loadStep(" + stepIdentifier + ")");

		StepLoaderResult stepLoaderResult = stepLoader.loadStep(stepIdentifier);

		return stepResponseFactory.createResponse(stepLoaderResult, stepIdentifier,
				buildIdentifierBeforeAliasResolution, addFallbackInfo);
	}

	/**
	 * This method is used for sharing screenshot images. It is a bit slower, because the image filename has to be
	 * resolved first. But it is also more stable, because it uses the new "stable" URL pattern.
	 */
	@GET
	@Produces({"image/jpeg", "image/png"})
	@Path("/{scenarioName}/pageName/{pageName}/pageOccurrence/{pageOccurrence}/stepInPageOccurrence/{stepInPageOccurrence}/image.{extension}")
	public Response getScreenshotStable(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("pageName") final String pageName,
			@PathParam("pageOccurrence") final int pageOccurrence,
			@PathParam("stepInPageOccurrence") final int stepInPageOccurrence,
			@QueryParam("fallback") final boolean fallback, @QueryParam("labels") final String labels) {

		BuildIdentifier buildIdentifierBeforeAliasResolution = new BuildIdentifier(branchName, buildName);
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
			buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName, scenarioName, pageName,
			pageOccurrence, stepInPageOccurrence, labelsQueryParamParser.parseLabels(labels));

		StepLoaderResult stepImageInfo = stepLoader.loadStep(stepIdentifier);

		return screenshotResponseFactory.createResponse(stepImageInfo, fallback, buildIdentifierBeforeAliasResolution);
	}

	/**
	 * This method is used internally for loading the image of a step. It is the faster method, because it already knows
	 * the filename of the image.
	 */
	@GET
	@Produces({"image/jpeg", "image/png"})
	@Path("/{scenarioName}/image/{imageFileName}")
	public Response getScreenshot(@PathParam("branchName") final String branchName,
								  @PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
								  @PathParam("scenarioName") final String scenarioName, @PathParam("imageFileName") final String imageFileName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
			buildName);
		ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName);

		return screenshotResponseFactory.createFoundImageResponse(scenarioIdentifier, imageFileName, false);
	}

}
