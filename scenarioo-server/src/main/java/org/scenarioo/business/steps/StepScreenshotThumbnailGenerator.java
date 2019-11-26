package org.scenarioo.business.steps;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StepScreenshotThumbnailGenerator {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private final ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(
		configurationRepository.getDocumentationDataDirectory());

	public void calculateThumbnail(String branchName, String buildName, UseCase usecase, Scenario scenario, Step step) {

		final String usecaseName = usecase.getName();
		final String scenarioName = scenario.getName();
		final String imageFileName = step.getStepDescription().getScreenshotFileName();

		File screenshot = scenarioDocuReader.getScreenshotFile(branchName, buildName, usecaseName, scenarioName, imageFileName);

		try {
			BufferedImage originalScreenshot = ImageIO.read(screenshot);

			int type = originalScreenshot.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalScreenshot.getType();
			BufferedImage smallScreenshot = resizeScreenshot(originalScreenshot, 300, type);

			String path = screenshot.getParentFile().getAbsolutePath();
			File outputfile = new File(path, "thumbnail_" + imageFileName);
			// ImageOutputStream ios = ImageIO.createImageOutputStream(outputfile);

			ImageIO.write(smallScreenshot, imageFileName.endsWith(".png") ? "png" : "jpeg", outputfile);

		} catch (IOException ex) {
			throw new RuntimeException("Image could not be loaded unexpectedly", ex);
		}
	}

	private static BufferedImage resizeScreenshot(BufferedImage screenshot, int targetWidth, int type) {
		int imageWidth = screenshot.getWidth(null);
		int imageHeight = screenshot.getHeight(null);
		double aspectRatio = (double) imageWidth / (double) imageHeight;

		/*
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
}
