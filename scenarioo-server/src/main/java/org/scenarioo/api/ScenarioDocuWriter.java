/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.api;

import org.apache.commons.io.FileUtils;
import org.scenarioo.api.configuration.ScenarioDocuGeneratorConfiguration;
import org.scenarioo.api.exception.ScenarioDocuSaveException;
import org.scenarioo.api.exception.ScenarioDocuTimeoutException;
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.rules.CharacterChecker;
import org.scenarioo.api.rules.DetailsChecker;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.docu.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.scenarioo.api.rules.CharacterChecker.checkIdentifier;

/**
 * Generator to produce documentation files for a specific build.
 *
 * The writer performs all save operations as asynchronous writes, to not block the webtests that are typically calling
 * the save operations to save documentation content.
 *
 * An instance of such a writer needs to be closed after last write operation by using the method {@link #flush()}.
 * After calling {@link #flush()} once the writer can not be used anymore.
 */
public class ScenarioDocuWriter {

	private final ScenarioDocuFiles docuFiles;

	private final String branchName;

	private final String buildName;

	private final ExecutorService asyncWriteExecutor = newAsyncWriteExecutor();

	private final List<RuntimeException> caughtExceptions = new ArrayList<RuntimeException>();

	/**
	 * Initialize with directory inside which to generate the documentation contents.
	 *
	 * @param destinationRootDirectory
	 *            the directory where the content should be generated (this directory must be precreated by you!).
	 * @param branchName
	 *            name of the branch we are generating content for
	 * @param buildName
	 *            name of the build (concrete identifier like revision and date) for which we are generating content.
	 */
	public ScenarioDocuWriter(final File destinationRootDirectory, final String branchName, final String buildName) {
		checkIdentifier(branchName);
		checkIdentifier(buildName);
		docuFiles = new ScenarioDocuFiles(destinationRootDirectory);
		this.branchName = branchName;
		this.buildName = buildName;
		createBuildDirectoryIfNotYetExists();
	}

	/**
	 * Save the branch description to appropriate directory
	 *
	 * @param branch
	 *            the branch description to write.
	 */
	public void saveBranchDescription(final Branch branch) {
		checkIdentifier(branch.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destBranchFile = docuFiles.getBranchFile(branchName);
				ScenarioDocuXMLFileUtil.marshal(branch, destBranchFile);
			}
		});
	}

	/**
	 * Save the build description to appropriate directory
	 *
	 * @param build
	 *            the build description to write
	 */
	public void saveBuildDescription(final Build build) {
		checkIdentifier(build.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destBuildFile = docuFiles.getBuildFile(branchName, buildName);
				ScenarioDocuXMLFileUtil.marshal(build, destBuildFile);
			}
		});
	}

	/**
	 * Save the feature description to appropriate directory and file
	 *
	 * @param feature
	 *            the feature description to write
	 */
	public void saveFeature(final Feature feature) {
		checkIdentifier(feature.getId());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destCaseDir = getFeatureDirectory(feature.getId());
				createDirectoryIfNotYetExists(destCaseDir);
				File destCaseFile = docuFiles.getFeatureFile(branchName, buildName, feature.getId());
				ScenarioDocuXMLFileUtil.marshal(feature, destCaseFile);
			}
		});
	}

	public void saveScenario(final Feature feature, final Scenario scenario) {
		saveScenario(feature.getId(), scenario);
	}

	public void saveScenario(final String featureName, final Scenario scenario) {
		checkIdentifier(featureName);
		checkIdentifier(scenario.getName());
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destScenarioDir = getScenarioDirectory(featureName, scenario.getName());
				createDirectoryIfNotYetExists(destScenarioDir);
				File destScenarioFile = docuFiles.getScenarioFile(branchName, buildName, featureName,
						scenario.getName());
				ScenarioDocuXMLFileUtil.marshal(scenario, destScenarioFile);
			}
		});
	}

	public void saveStep(final Feature feature, final Scenario scenario, final Step step) {
		saveStep(feature.getId(), scenario.getName(), step);
	}

	/**
	 * The page property of the step is optional, but it is recommended to use it. Page names are a central part of
	 * Scenarioo.
	 */
	public void saveStep(final String featureName, final String scenarioName, final Step step) {
		checkSaveStepPreconditions(featureName, scenarioName, step);
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destStepsDir = getScenarioStepsDirectory(featureName, scenarioName);
				createDirectoryIfNotYetExists(destStepsDir);
				calculateScreenshotFileNameIfNotSetWorkaround(featureName, scenarioName, step);
				File destStepFile = docuFiles.getStepFile(branchName, buildName, featureName, scenarioName, step
						.getStepDescription().getIndex());
				ScenarioDocuXMLFileUtil.marshal(step, destStepFile);
			}
		});
	}

	private void checkSaveStepPreconditions(final String featureName, final String scenarioName, final Step step) {
		CharacterChecker.checkIdentifier(featureName);
		CharacterChecker.checkIdentifier(scenarioName);
		if (step.getPage() != null) {
			CharacterChecker.checkIdentifier(step.getPage().getName());
		}
		if (step.getMetadata() != null) {
			DetailsChecker.checkIdentifiers(step.getMetadata().getDetails());
		}
		if (step.getStepDescription() != null) {
			DetailsChecker.checkIdentifiers(step.getStepDescription().getDetails());
		}
	}

	private void calculateScreenshotFileNameIfNotSetWorkaround(final String featureName, final String scenarioName,
			final Step step) {
		StepDescription stepDescription = step.getStepDescription();
		if (stepDescription != null && stepDescription.getScreenshotFileName() == null) {
			File imageFile = docuFiles.getScreenshotFile(branchName, buildName, featureName, scenarioName,
					stepDescription.getIndex());
			stepDescription.setScreenshotFileName(imageFile.getName());
		}
	}

	/**
	 * In case you want to define your screenshot names differently than by step name, you can save it on your own, into
	 * the following directory for a scenario.
	 */
	public File getScreenshotsDirectory(final String featureName, final String scenarioName) {
		return docuFiles.getScreenshotsDirectory(branchName, buildName, checkIdentifier(featureName),
				checkIdentifier(scenarioName));
	}

	/**
	 * Get the file name of the file where the screenshot of a step is stored.
	 */
	public File getScreenshotFile(final String featureName, final String scenarioName, final int stepIndex) {
		return docuFiles.getScreenshotFile(branchName, buildName, checkIdentifier(featureName),
				checkIdentifier(scenarioName), stepIndex);
	}

	/**
	 * Save the provided PNG image as a PNG file into the correct default file location for a step.
	 *
	 * In case you want to use another image format (e.g. JPEG) or just want to define the image file names for your
	 * scenarios differently, you can do this by using {@link StepDescription#setScreenshotFileName} and saving the
	 * picture on your own, as explained in the documentation of the mentioned method.
	 *
	 * @param pngScreenshot
	 *            Screenshot in PNG format.
	 */
	public void saveScreenshotAsPng(final String featureName, final String scenarioName, final int stepIndex,
			final byte[] pngScreenshot) {
		checkIdentifier(featureName);
		checkIdentifier(scenarioName);
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File screenshotFile = docuFiles.getScreenshotFile(branchName, buildName, featureName,
						scenarioName, stepIndex);
				try {
					FileUtils.writeByteArrayToFile(screenshotFile, pngScreenshot);
				} catch (IOException e) {
					throw new RuntimeException("Could not write image: " + screenshotFile.getAbsolutePath(), e);
				}
			}
		});
	}

	/**
	 * Finish asynchronous writing of all saved files. This has to be called in the end, to ensure all data saved in
	 * this generator is written to the filesystem.
	 *
	 * Will block until writing has finished or timeout occurs.
	 *
	 * @throws ScenarioDocuSaveException
	 *             if any of the save commands throwed an exception during asynchronous execution.
	 * @throws ScenarioDocuTimeoutException
	 *             if waiting for the saving beeing finished exceeds the configured timeout
	 */
	public void flush() {
		int timeoutInSeconds = ScenarioDocuGeneratorConfiguration.INSTANCE
				.getTimeoutWaitingForWritingFinishedInSeconds();
		asyncWriteExecutor.shutdown();
		try {
			boolean terminated = asyncWriteExecutor.awaitTermination(timeoutInSeconds, TimeUnit.SECONDS);
			if (!terminated) {
				asyncWriteExecutor.shutdownNow();
				throw new ScenarioDocuTimeoutException(
						"Timeout occured while waiting for docu files to be written. Writing of files took too long.");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("Async writing of scenarioo docu files was interrupted", e);
		}
		if (!caughtExceptions.isEmpty()) {
			throw new ScenarioDocuSaveException(caughtExceptions);
		}
	}

	private File getBuildDirectory() {
		return docuFiles.getBuildDirectory(branchName, buildName);
	}

	private File getFeatureDirectory(final String featureName) {
		return docuFiles.getFeatureDirectory(branchName, buildName, featureName);
	}

	private File getScenarioDirectory(final String featureName, final String scenarioName) {
		return docuFiles.getScenarioDirectory(branchName, buildName, featureName, scenarioName);
	}

	private File getScenarioStepsDirectory(final String featureName, final String scenarioName) {
		return docuFiles.getStepsDirectory(branchName, buildName, featureName, scenarioName);
	}

	private void createBuildDirectoryIfNotYetExists() {
		createDirectoryIfNotYetExists(getBuildDirectory());
	}

	private void createDirectoryIfNotYetExists(final File directory) {
		docuFiles.assertRootDirectoryExists();
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private void executeAsyncWrite(final Runnable writeTask) {
		asyncWriteExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					writeTask.run();
				} catch (RuntimeException e) {
					caughtExceptions.add(e);
				}
			}
		});
	}

	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread. The excutor will
	 * start to block further executions as soon as more than the configured write tasks are waiting for execution.
	 */
	private static ExecutorService newAsyncWriteExecutor() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(ScenarioDocuGeneratorConfiguration.INSTANCE.getAsyncWriteBufferSize()));
		// as soon as the queue is full: start to execute writes synchronously
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	/**
	 * Get the directory to store arbitrary additional documents (like markdown files, gherkin files, etc.)
	 * inside a build.
	 */
	public File getDocsDirectory() {
		return docuFiles.getDocsDirectory(this.branchName, this.buildName);
	}
}
