package org.scenarioo.rest.step.logic;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;

public class ScreenshotResponseFactory {
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());
	private final FallbackImageMarker fallbackImageMarker = new FallbackImageMarker();
	
	public Response createResponse(final StepLoaderResult stepLoaderResult, final boolean showFallbackStamp,
			final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		if (stepLoaderResult.isRequestedStepFound()) {
			return foundImageResponse(stepLoaderResult, showFallbackStamp);
		} else if (stepLoaderResult.isRedirect()) {
			return redirectResponse(stepLoaderResult, buildIdentifierBeforeAliasResolution);
		} else {
			return notFoundResponse();
		}
	}
	
	private Response foundImageResponse(final StepLoaderResult steLoaderResult, final boolean showFallbackStamp) {
		return createFoundImageResponse(steLoaderResult.getStepIdentifier().getScenarioIdentifier(),
				steLoaderResult.getScreenshotFileName(), showFallbackStamp);
	}
	
	public Response createFoundImageResponse(final ScenarioIdentifier scenarioIdentifier, final String imageFileName,
			final boolean showFallbackStamp) {
		final BuildIdentifier buildIdentifier = scenarioIdentifier.getBuildIdentifier();
		final String usecaseName = scenarioIdentifier.getUsecaseName();
		final String scenarioName = scenarioIdentifier.getScenarioName();
		
		File screenshot = scenarioDocuReader.getScreenshotFile(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), usecaseName, scenarioName, imageFileName);
		
		return createFoundImageResponse(imageFileName, screenshot, showFallbackStamp);
	}
	
	private Response createFoundImageResponse(final String imgName, final File screenshot,
			final boolean showFallbackStamp) {
		if (screenshot == null || !screenshot.exists()) {
			return notFoundResponse();
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
			return notFoundResponse();
		}
	}
	
	private Response createOkResponse(final String imgName, final File screenshot) {
		return Response.ok(screenshot).build();
	}
	
	private Response redirectResponse(final StepLoaderResult stepImage,
			final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		StepIdentifier stepIdentifier = stepImage.getStepIdentifier();
		StepIdentifier stepIdentifierWithPotentialAlias = stepIdentifier
				.withDifferentBuildIdentifier(buildIdentifierBeforeAliasResolution);
		String screenshotFileNameExtension = stepImage.getScreenshotFileNameExtension();
		return Response.temporaryRedirect(
				stepIdentifierWithPotentialAlias.getScreenshotUriForRedirect(screenshotFileNameExtension)).build();
	}
	
	private Response notFoundResponse() {
		return Response.status(Status.NOT_FOUND).build();
	}
	
}
