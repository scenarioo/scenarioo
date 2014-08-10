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

package org.scenarioo.rest;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.rest.request.BuildIdentifier;
import org.scenarioo.rest.request.ScenarioIdentifier;
import org.scenarioo.rest.request.StepIdentifier;
import org.scenarioo.rest.util.FallbackImageMarker;
import org.scenarioo.rest.util.ScenarioLoader;
import org.scenarioo.rest.util.StepImageInfo;
import org.scenarioo.rest.util.StepImageInfoLoader;
import org.scenarioo.rest.util.StepIndexResolver;

@Path("/rest/branch/{branchName}/build/{buildName}/usecase/{usecaseName}/scenario/{scenarioName}/")
public class ScreenshotResource {
	
	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			ConfigurationDAO.getDocuDataDirectoryPath());
	
	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();
	private final AggregatedDataReader aggregatedDataReader = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath(), longObjectNamesResolver);
	private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	private final StepImageInfoLoader stepImageInfoLoader = new StepImageInfoLoader(scenarioLoader, stepIndexResolver);
	private final FallbackImageMarker fallbackImageMarker = new FallbackImageMarker();
	
	private final String PNG_FILE_EXTENSION = ".png";
	
	/**
	 * This method is used internally for loading the image of a step. It is the faster method, because it already knows
	 * the filename of the image.
	 */
	@GET
	@Produces("image/jpeg")
	@Path("image/{imageFileName}")
	public Response getScreenshot(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("imageFileName") final String imageFileName) {
		
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName);
		
		return createFoundImageResponse(scenarioIdentifier, imageFileName, false);
	}
	
	/**
	 * This method is used for sharing screenshot images. It is a bit slower, because the image filename has to be
	 * resolved first. But it is also more stable, because it uses the new "stable" URL pattern.
	 */
	@GET
	@Produces("image/jpeg")
	@Path("pageName/{pageName}/pageOccurrence/{pageOccurrence}/stepInPageOccurrence/{stepInPageOccurrence}"
			+ PNG_FILE_EXTENSION)
	public Response getScreenshotStable(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("pageName") final String pageName,
			@PathParam("pageOccurrence") final int pageOccurrence,
			@PathParam("stepInPageOccurrence") final int stepInPageOccurrence,
			@QueryParam("fallback") final boolean showFallbackStamp) {
		
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName, scenarioName, pageName,
				pageOccurrence, stepInPageOccurrence);
		
		StepImageInfo stepImageInfo = stepImageInfoLoader.loadStepImageInfo(stepIdentifier);
		
		// TODO [fallback / aliases] Use alias for branch / build in case one was used
		return createResponse(stepImageInfo, showFallbackStamp);
	}
	
	private Response createResponse(final StepImageInfo stepImage, final boolean showFallbackStamp) {
		if (stepImage.isRequestedStepFound()) {
			String imageFileName = getFileName(stepImage.getStepIndex());
			return createFoundImageResponse(stepImage.getStepIdentifier().getScenarioIdentifier(), imageFileName,
					showFallbackStamp);
		} else if (stepImage.isRedirect()) {
			return createRedirectResponse(stepImage.getStepIdentifier());
		} else {
			return createImageNotFoundResponse();
		}
	}
	
	private Response createRedirectResponse(final StepIdentifier redirect) {
		return Response.temporaryRedirect(redirect.getScreenshotUriForRedirect()).build();
	}
	
	private Response createImageNotFoundResponse() {
		return Response.status(Status.NOT_FOUND).build();
	}
	
	private String getFileName(final int stepIndex) {
		DecimalFormat decimalFormat = new DecimalFormat("000");
		return decimalFormat.format(stepIndex) + PNG_FILE_EXTENSION;
	}
	
	private Response createFoundImageResponse(final ScenarioIdentifier scenarioIdentifier, final String imageFileName,
			final boolean showFallbackStamp) {
		final BuildIdentifier buildIdentifier = scenarioIdentifier.getBuildIdentifier();
		final String usecaseName = scenarioIdentifier.getUsecaseName();
		final String scenarioName = scenarioIdentifier.getScenarioName();
		
		File screenshot = scenarioDocuReader.getScreenshotFile(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), usecaseName, scenarioName, imageFileName);
		
		return createResponse(imageFileName, screenshot, showFallbackStamp);
	}
	
	private Response createResponse(final String imgName, final File screenshot, final boolean showFallbackStamp) {
		if (screenshot == null || !screenshot.exists()) {
			return createImageNotFoundResponse();
		}
		
		if (showFallbackStamp) {
			return createOkResponseWithFallbackStamp(imgName, screenshot);
		} else {
			return createOkResponse(imgName, screenshot);
		}
	}
	
	private Response createOkResponseWithFallbackStamp(final String imgName, final File screenshot) {
		try {
			byte[] stampedScreenshot = fallbackImageMarker.getMarkedImage(screenshot);
			return Response.ok(stampedScreenshot).build();
		} catch (IOException e) {
			e.printStackTrace();
			return createImageNotFoundResponse();
		}
	}
	
	private Response createOkResponse(final String imgName, final File screenshot) {
		return Response.ok(screenshot).build();
	}
	
}
