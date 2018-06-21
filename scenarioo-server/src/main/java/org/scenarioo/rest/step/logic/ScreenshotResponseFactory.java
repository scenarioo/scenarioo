package org.scenarioo.rest.step.logic;

import java.io.File;
import java.io.IOException;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ScreenshotResponseFactory {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());
	private final FallbackImageMarker fallbackImageMarker = new FallbackImageMarker();

	public ResponseEntity createResponse(final StepLoaderResult stepLoaderResult, final boolean showFallbackStamp,
										 final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		if (stepLoaderResult.isRequestedStepFound()) {
			return foundImageResponse(stepLoaderResult, showFallbackStamp);
		} else if (stepLoaderResult.isRedirect()) {
			return redirectResponse(stepLoaderResult, buildIdentifierBeforeAliasResolution);
		} else {
			return notFoundResponse();
		}
	}

	private ResponseEntity foundImageResponse(final StepLoaderResult steLoaderResult, final boolean showFallbackStamp) {
		return createFoundImageResponse(steLoaderResult.getStepIdentifier().getScenarioIdentifier(),
				steLoaderResult.getScreenshotFileName(), showFallbackStamp);
	}

	public ResponseEntity createFoundImageResponse(final ScenarioIdentifier scenarioIdentifier, final String imageFileName,
			final boolean showFallbackStamp) {
		final BuildIdentifier buildIdentifier = scenarioIdentifier.getBuildIdentifier();
		final String usecaseName = scenarioIdentifier.getUsecaseName();
		final String scenarioName = scenarioIdentifier.getScenarioName();

		File screenshot = scenarioDocuReader.getScreenshotFile(buildIdentifier.getBranchName(),
				buildIdentifier.getBuildName(), usecaseName, scenarioName, imageFileName);

		return createFoundImageResponse(screenshot, showFallbackStamp);
	}

	private ResponseEntity createFoundImageResponse(final File screenshot,
													final boolean showFallbackStamp) {
		if (screenshot == null || !screenshot.exists()) {
			return notFoundResponse();
		}

		if (showFallbackStamp) {
			return createOkResponseWithFallbackStamp(screenshot);
		} else {
			return createOkResponse(screenshot);
		}
	}

	private ResponseEntity createOkResponseWithFallbackStamp(final File screenshot) {
		try {
			byte[] stampedScreenshot = fallbackImageMarker.getMarkedImage(screenshot);
			return ResponseEntity.ok(stampedScreenshot);
		} catch (IOException e) {
			e.printStackTrace();
			return notFoundResponse();
		}
	}

	private ResponseEntity createOkResponse(final File screenshot) {
		return ResponseEntity.ok(screenshot);
	}

	private ResponseEntity redirectResponse(final StepLoaderResult stepImage,
			final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		StepIdentifier stepIdentifier = stepImage.getStepIdentifier();
		StepIdentifier stepIdentifierWithPotentialAlias = stepIdentifier
				.withDifferentBuildIdentifier(buildIdentifierBeforeAliasResolution);
		String screenshotFileNameExtension = stepImage.getScreenshotFileNameExtension();
		return ResponseEntity.status(HttpStatus.FOUND)
			.header(HttpHeaders.LOCATION, stepIdentifierWithPotentialAlias.getScreenshotUriForRedirect(screenshotFileNameExtension).toString())
			.build();
	}

	private ResponseEntity notFoundResponse() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

}
