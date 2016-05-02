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

package org.scenarioo.business.diffViewer;

import java.io.File;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListErrorConsumer;
import org.im4java.process.ArrayListOutputConsumer;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.NumberFormatCreator;

/**
 * Comparator to compare screenshots of two steps. Results are persisted in a xml file.
 */
public class ScreenshotComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(ScreenshotComparator.class);
	private static final int CHANGERATE_SCREENSHOT_DEFAULT = 0;
	private static final String SCREENSHOT_FILE_EXTENSION = ".png";
	private static NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
			.createNumberFormatWithMinimumIntegerDigits(3);
	private final DiffReader diffReader;
	private final ArrayListErrorConsumer gmConsoleErrorConsumer;
	private ArrayListOutputConsumer gmConsoleOutputConsumer;
	private CompareCmd gmConsole;

	public ScreenshotComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
		gmConsole = new CompareCmd(true);
		gmConsoleErrorConsumer = new ArrayListErrorConsumer();
		gmConsoleOutputConsumer = new ArrayListOutputConsumer();
		gmConsole.setErrorConsumer(gmConsoleErrorConsumer);
		gmConsole.setOutputConsumer(gmConsoleOutputConsumer);
		diffReader = new DiffReader(configurationRepository.getDiffViewerDirectory());
	}

	/**
	 * Compares the screenshots of the given step.
	 * 
	 * @param baseUseCaseName
	 *            the use case of the screenshots.
	 * @param baseScenarioName
	 *            the scenario of the screenshots.
	 * @param baseStepLink
	 *            the step to compare the screenshots.
	 * @return {@link StepDiffInfo} with the summarized diff information.
	 */
	public double compare(final String baseUseCaseName, final String baseScenarioName, final StepLink baseStepLink,
			final StepLink comparisonStepLink) {
		final BuildIdentifier comparisonBuildIdentifier = getComparisonBuildIdentifier(comparisonName);

		final String baseScreenshotName = THREE_DIGIT_NUM_FORMAT.format(baseStepLink.getStepIndex())
				+ SCREENSHOT_FILE_EXTENSION;
		final String comparisonScreenshotName = THREE_DIGIT_NUM_FORMAT.format(comparisonStepLink.getStepIndex())
				+ SCREENSHOT_FILE_EXTENSION;
		final String diffScreenshotName = baseScreenshotName;

		final File baseScreenshot = docuReader.getScreenshotFile(baseBranchName,
				baseBuildName, baseUseCaseName, baseScenarioName, baseScreenshotName);

		final File comparisonScreenshot = docuReader.getScreenshotFile(comparisonBuildIdentifier.getBranchName(),
				comparisonBuildIdentifier.getBuildName(), baseUseCaseName, baseScenarioName, comparisonScreenshotName);

		final File diffScreenshot = diffReader.getScreenshotFile(baseBranchName, baseBuildName, comparisonName,
				baseUseCaseName, baseScenarioName, diffScreenshotName);

		return compareScreenshots(baseScreenshot, comparisonScreenshot, diffScreenshot);
	}

	/**
	 * Compares the given screenshots
	 * 
	 * @param baseScreenshot
	 *            The original screenshot
	 * @param comparisonScreenshot
	 *            The comparison screenshot
	 * @param diffScreenshot
	 *            The generated screenshot that shows differences
	 * @return
	 */
	public double compareScreenshots(final File baseScreenshot, final File comparisonScreenshot,
			final File diffScreenshot) {
		diffScreenshot.getParentFile().mkdir();
		final IMOperation gmOperation = new IMOperation();
		gmOperation.metric("RMSE");
		gmOperation.addImage(baseScreenshot.getPath());
		gmOperation.addImage(comparisonScreenshot.getPath());
		gmOperation.addRawArgs("-highlight-style", "Tint");
		gmOperation.addRawArgs("-file", diffScreenshot.getPath());
		final double difference = runGraphicsMagickOperation(gmOperation);
		if (difference == 0.0) {
			diffScreenshot.delete();
		}
		return difference;
	}

	private double runGraphicsMagickOperation(final IMOperation gmOperation) {
		try {
			gmConsole.run(gmOperation);
			return getRmseValueFromOutput();
		} catch (final Exception e) {
			LOGGER.warn("Graphics Magick operation failed. Default screenshot changerate '"
					+ CHANGERATE_SCREENSHOT_DEFAULT + "' gets returned.");
			if (gmConsoleErrorConsumer.getOutput().size() > 0) {
				final String errorMessage = gmConsoleErrorConsumer.getOutput().get(0);
				LOGGER.warn(errorMessage);
			}
			return CHANGERATE_SCREENSHOT_DEFAULT;

		} finally {
			gmConsoleOutputConsumer.clear();
		}
	}

	double getRmseValueFromOutput() {
		if (gmConsoleOutputConsumer.getOutput().size() == 8) {
			final String cmdOutput = gmConsoleOutputConsumer.getOutput().get(7);
			final Pattern p = Pattern.compile("\\d+\\.\\d+");
			final Matcher m = p.matcher(cmdOutput);
			if (m.find()) {
				return Double.parseDouble(m.group(0)) * 100;
			}
		}
		throw new RuntimeException("Cannot parse Graphics Magick console output");
	}

	public ArrayListOutputConsumer getCmdOutputConsumer() {
		return gmConsoleOutputConsumer;
	}

	public void setCmdOutputConsumer(final ArrayListOutputConsumer cmdOutputConsumer) {
		this.gmConsoleOutputConsumer = cmdOutputConsumer;
	}

	public CompareCmd getCmd() {
		return gmConsole;
	}

	public void setCmd(final CompareCmd cmd) {
		this.gmConsole = cmd;
	}

	public Logger getLogger() {
		return LOGGER;
	}

}
