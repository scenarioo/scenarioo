package ngusd.api;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ngusd.api.configuration.ScenarioDocuGeneratorConfiguration;
import ngusd.api.exception.ScenarioDocuSaveException;
import ngusd.api.exception.ScenarioDocuTimeoutException;
import ngusd.api.files.ScenarioDocuFiles;
import ngusd.api.util.files.XMLFileUtil;
import ngusd.model.docu.entities.Branch;
import ngusd.model.docu.entities.Build;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.UseCase;

import org.apache.commons.codec.binary.Base64;

/**
 * Generator to produce documentation files for a specific build.
 * 
 * The writer performs all save operations as asynchronous writes, to not block the webtests tht are typically calling
 * the save operations to save docu content.
 */
public class ScenarioDocuWriter {
	
	private ScenarioDocuFiles docuFiles;
	
	private String branchName;
	
	private String buildName;
	
	private ExecutorService asyncWriteExecutor = newAsyncWriteExecutor();
	
	private List<RuntimeException> caughtExceptions = new ArrayList<RuntimeException>();
	
	/**
	 * Initialize with directory inside which to generate the documentation contents.
	 * 
	 * @param destinationDirectory
	 *            the directory where the content should be generated (this directory must be precreated by you!).
	 * @param branchName
	 *            name of the branch we are generating content for
	 * @param buildName
	 *            name of the build (concrete identifier like revision and date) for which we are generating content.
	 */
	public ScenarioDocuWriter(final File destinationRootDirectory, final String branchName, final String buildName) {
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
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destBranchFile = docuFiles.getBranchFile(branchName);
				XMLFileUtil.marshal(branch, destBranchFile);
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
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destBuildFile = docuFiles.getBuildFile(branchName, buildName);
				XMLFileUtil.marshal(build, destBuildFile);
			}
		});
	}
	
	/**
	 * Save the use case description to appropriate directory and file
	 * 
	 * @param useCase
	 *            the use case description to write
	 */
	public void saveUseCase(final UseCase useCase) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destCaseDir = getUseCaseDirectory(useCase.getName());
				createDirectoryIfNotYetExists(destCaseDir);
				File destCaseFile = docuFiles.getUseCaseFile(branchName, buildName, useCase.getName());
				XMLFileUtil.marshal(useCase, destCaseFile);
			}
		});
	}
	
	public void saveScenario(final UseCase useCase, final Scenario scenario) {
		saveScenario(useCase.getName(), scenario);
	}
	
	public void saveScenario(final String useCaseName, final Scenario scenario) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destScenarioDir = getScenarioDirectory(useCaseName, scenario.getName());
				createDirectoryIfNotYetExists(destScenarioDir);
				File destScenarioFile = docuFiles.getScenarioFile(branchName, buildName, useCaseName,
						scenario.getName());
				XMLFileUtil.marshal(scenario, destScenarioFile);
			}
		});
	}
	
	public void saveStep(final UseCase useCase, final Scenario scenario, final Step step) {
		saveStep(useCase.getName(), scenario.getName(), step);
	}
	
	public void saveStep(final String useCaseName, final String scenarioName, final Step step) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				File destStepsDir = getScenarioStepsDirectory(useCaseName, scenarioName);
				createDirectoryIfNotYetExists(destStepsDir);
				File destStepFile = docuFiles.getStepFile(branchName, buildName, useCaseName, scenarioName, step
						.getStepDescription().getIndex());
				XMLFileUtil.marshal(step, destStepFile);
			}
		});
	}
	
	/**
	 * In case you want to define your screenshot names differently than by step name, you can save it on your own, into
	 * the following directory for a scenario.
	 */
	public File getScreenshotsDirectory(final String usecaseName, final String scenarioName) {
		return docuFiles.getScreenshotsDirectory(branchName, buildName, usecaseName, scenarioName);
	}
	
	/**
	 * Get the file name of the file where the screenshot of a step is stored.
	 */
	public File getScreenshotFile(final String usecaseName, final String scenarioName, final int stepIndex) {
		return docuFiles.getScreenshotFile(branchName, buildName, usecaseName, scenarioName, stepIndex);
	}
	
	/**
	 * Save Screenshot as a PNG file in usual file for step.
	 */
	public void saveScreenshot(final String usecaseName, final String scenarioName, final int stepIndex,
			final byte[] imageBase64Encoded) {
		executeAsyncWrite(new Runnable() {
			@Override
			public void run() {
				final File screenshotFile = docuFiles.getScreenshotFile(branchName, buildName, usecaseName,
						scenarioName, stepIndex);
				File screenshotsDir = getScreenshotsDirectory(usecaseName, scenarioName);
				createDirectoryIfNotYetExists(screenshotsDir);
				try {
					final byte[] decodedScreenshot = Base64.decodeBase64(imageBase64Encoded);
					final FileOutputStream fos = new FileOutputStream(screenshotFile);
					fos.write(decodedScreenshot);
					fos.close();
				} catch (Exception e) {
					throw new RuntimeException("Could not write image: " + screenshotFile.getAbsolutePath(), e);
				}
			}
		});
	}
	
	/**
	 * Save Screenshot as a PNG file in usual file for step.
	 */
	public void saveScreenshot(final String usecaseName, final String scenarioName, final int stepIndex,
			final String imageBase64Encoded) {
		saveScreenshot(usecaseName, scenarioName, stepIndex, imageBase64Encoded.getBytes());
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
			boolean temrinated = asyncWriteExecutor.awaitTermination(timeoutInSeconds, TimeUnit.SECONDS);
			if (!temrinated) {
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
	
	private File getUseCaseDirectory(final String useCaseName) {
		return docuFiles.getUseCaseDirectory(branchName, buildName, useCaseName);
	}
	
	private File getScenarioDirectory(final String useCaseName, final String scenarioName) {
		return docuFiles.getScenarioDirectory(branchName, buildName, useCaseName, scenarioName);
	}
	
	private File getScenarioStepsDirectory(final String useCaseName, final String scenarioName) {
		return docuFiles.getStepsDirectory(branchName, buildName, useCaseName, scenarioName);
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
				}
				catch (RuntimeException e) {
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
		return new ThreadPoolExecutor(
				1,
				1,
				60L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(ScenarioDocuGeneratorConfiguration.INSTANCE.getAsyncWriteBufferSize()));
	}
	
}
