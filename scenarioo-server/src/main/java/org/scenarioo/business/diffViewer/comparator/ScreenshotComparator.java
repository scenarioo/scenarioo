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

package org.scenarioo.business.diffViewer.comparator;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.NumberFormatter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

public class ScreenshotComparator {

	private static final Logger LOGGER = Logger.getLogger(ScreenshotComparator.class);
	private static final int SCREENSHOT_DEFAULT_CHANGE_RATE = 0;
	protected static final ConfigurationRepository configurationRepository =
		RepositoryLocator.INSTANCE.getConfigurationRepository();

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	private ScenarioDocuReader scenarioDocuReader =
		new ScenarioDocuReader(configurationRepository.getDocumentationDataDirectory());

	public double compare(ComparisonParameters parameters, final String baseUseCaseName, final String baseScenarioName,
						  final StepLink baseStepLink, final String comparisonScreenshotName) {

		final String baseScreenshotName = NumberFormatter.formatMinimumThreeDigits(baseStepLink.getStepIndex()) + ".png";

		final File baseScreenshot = scenarioDocuReader.getScreenshotFile(parameters.getBaseBranchName(),
			parameters.getBaseBuildName(), baseUseCaseName, baseScenarioName, baseScreenshotName);

		final File comparisonScreenshot = scenarioDocuReader.getScreenshotFile(
			parameters.getComparisonConfiguration().getComparisonBranchName(),
			parameters.getComparisonConfiguration().getComparisonBuildName(), baseUseCaseName, baseScenarioName,
			comparisonScreenshotName);

		final File diffScreenshot = diffViewerDao.getScreenshotFile(parameters.getBaseBranchName(), parameters.getBaseBuildName(),
			parameters.getComparisonConfiguration().getName(),
			baseUseCaseName, baseScenarioName, baseScreenshotName);

		if (!baseScreenshot.exists()) {
			LOGGER.warn("Base screenshot does not exist: " + baseScreenshot.getAbsolutePath());
			return 0.0;
		}
		if (!comparisonScreenshot.exists()) {
			LOGGER.warn("Comparison screenshot does not exist: " + baseScreenshot.getAbsolutePath());
			return 0.0;
		}

		return compareScreenshots(parameters, baseScreenshot, comparisonScreenshot, diffScreenshot);
	}

	/**
	 * A Diff Screenshot will be created and stored in the directory of the diffScreenshot path.
	 * This directory, and all the parent directories, will be created if they do not exist.
	 */
	double compareScreenshots(ComparisonParameters parameters, final File baseScreenshot,
							  final File comparisonScreenshot,
							  final File diffScreenshot) {

		if (diffScreenshot.getParentFile() != null) {
			diffScreenshot.getParentFile().mkdirs();
		}

		try {
			int diffColor = parameters.getDiffImageColor().getRGB();

			BufferedImage oldImage = ImageIO.read(baseScreenshot);
			BufferedImage newImage = ImageIO.read(comparisonScreenshot);

			// create images with the maximum required size to ensure that both images fit
			int unifiedWidth = Math.max(oldImage.getWidth(), newImage.getWidth());
			int unifiedHeight = Math.max(oldImage.getHeight(), newImage.getHeight());

			// convert images to ARGB format to ensure that the data buffers store one pixel as ARGB
			BufferedImage oldImageArgb = new BufferedImage(unifiedWidth, unifiedHeight, BufferedImage.TYPE_INT_ARGB);
			BufferedImage newImageArgb = new BufferedImage(unifiedWidth, unifiedHeight, BufferedImage.TYPE_INT_ARGB);

			// for simplicity paint both images on the upper left corner of the compare image since searching the sub-image is too expensive
			// areas not covered by images are transparent which will lead to a maximum difference on all 3 measured color channels
			oldImageArgb.getGraphics().drawImage(oldImage, 0, 0, null);
			newImageArgb.getGraphics().drawImage(newImage, 0, 0, null);

			BufferedImage diffImage = new BufferedImage(Math.max(oldImageArgb.getWidth(), newImageArgb.getWidth()), Math.max(oldImageArgb.getHeight(), oldImageArgb.getHeight()), BufferedImage.TYPE_INT_ARGB);

			boolean diffAvailable = false;
			double absoluteErrorTotal = 0;

			// improve performance by accessing data buffers directly since methods to access pixels through BufferedImage are passed through the ColorModel which slows things down
			DataBuffer oldImageBuffer = oldImageArgb.getRaster().getDataBuffer();
			DataBuffer newImageBuffer = newImageArgb.getRaster().getDataBuffer();
			DataBuffer diffImageBuffer = diffImage.getRaster().getDataBuffer();

			int totalPixels = oldImageBuffer.getSize();

			for (int n = 0; n < oldImageBuffer.getSize(); n++) {
				int oldPixel = oldImageBuffer.getElem(n);
				int newPixel = newImageBuffer.getElem(n);

				if (oldPixel != newPixel) {
					diffAvailable = true;
					diffImageBuffer.setElem(n, diffColor);

					absoluteErrorTotal += calculateAbsoluteError(oldPixel, newPixel);
				}
			}

			double mae = absoluteErrorTotal / totalPixels;

			if (diffAvailable) {
				ImageIO.write(diffImage, "PNG", diffScreenshot);
			}

			return Math.sqrt(mae) * 100;
		} catch (IOException ex) {
			LOGGER.warn("Failed to compare images (base: " + baseScreenshot + ", comparison: " + comparisonScreenshot + ")", ex);
			return SCREENSHOT_DEFAULT_CHANGE_RATE;
		}
	}

	private double calculateAbsoluteError(int oldPixel, int newPixel) {

		double oldRed = ((oldPixel >> 16) & 0x000000FF) / 255.;
		double oldGreen = ((oldPixel >> 8) & 0x000000FF) / 255.;
		double oldBlue = ((oldPixel) & 0x000000FF) / 255.;

		double newRed = ((newPixel >> 16) & 0x000000FF) / 255.;
		double newGreen = ((newPixel >> 8) & 0x000000FF) / 255.;
		double newBlue = ((newPixel) & 0x000000FF) / 255.;

		double diffRed = Math.abs(oldRed - newRed);
		double diffGreen = Math.abs(oldGreen - newGreen);
		double diffBlue = Math.abs(oldBlue - newBlue);

		return (diffRed + diffGreen + diffBlue) / 3;
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}
