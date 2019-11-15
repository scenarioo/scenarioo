package org.scenarioo.rest.step.logic;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.FileResponseCreator;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ScreenshotResponseFactory {

	private static final Logger LOGGER = Logger.getLogger(ScreenshotResponseFactory.class);

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

	public ResponseEntity createFoundSmallImageResponse(final ScenarioIdentifier scenarioIdentifier, final String imageFileName) {

		final BuildIdentifier buildIdentifier = scenarioIdentifier.getBuildIdentifier();
		final String usecaseName = scenarioIdentifier.getUsecaseName();
		final String scenarioName = scenarioIdentifier.getScenarioName();

		File screenshot = scenarioDocuReader.getScreenshotFile(buildIdentifier.getBranchName(),
			buildIdentifier.getBuildName(), usecaseName, scenarioName, imageFileName);

		if (screenshot == null || !screenshot.exists()) {
			return notFoundResponse();
		}

		try {
			BufferedImage originalScreenshot = ImageIO.read(screenshot);

			int type = originalScreenshot.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalScreenshot.getType();
			BufferedImage smallScreenshot = resizeScreenshot(originalScreenshot, 300, type);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(smallScreenshot, imageFileName.endsWith(".png") ? "png" : "jpeg", baos);
			return createOkResponse(baos.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException("Image could not be loaded unexpectedly", e);
		}

	}

	private static BufferedImage resizeScreenshot(BufferedImage screenshot, int targetWidth, int type) {
		int imageWidth = screenshot.getWidth(null);
		int imageHeight = screenshot.getHeight(null);
		double aspectRatio = (double) imageWidth / (double) imageHeight;

		/**
		 * If downscaling in one step, the BILINEAR and BICUBIC algorithms tend to lose information due to the way pixels are sampled from the source image
		 * To combat this issue, we are using a multi-step approach when downscaling by more than two times; this helps prevent the information loss issue and produces a much higher quality result.
		 */
		do {

			if (imageWidth > targetWidth) {
				imageWidth /= 10;
				if (imageWidth < targetWidth) {
					imageWidth = targetWidth;
				}
			}

			imageHeight = (int) (imageWidth / aspectRatio);

			BufferedImage resizedImage = new BufferedImage(imageWidth, imageHeight, type);
			Graphics2D g = resizedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(screenshot.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, imageWidth, imageHeight, null);
			g.dispose();

			screenshot = resizedImage;

		} while (imageWidth != targetWidth);

		return screenshot;
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
			LOGGER.error("Failed to load image.", e);
			return notFoundResponse();
		}
	}

	private ResponseEntity createOkResponse(final File screenshot) {
		return FileResponseCreator.createImageFileResponse(screenshot);
	}

	private ResponseEntity createOkResponse(final byte[] screenshot) {
		return FileResponseCreator.createSmallImageFileResponse(screenshot);
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
