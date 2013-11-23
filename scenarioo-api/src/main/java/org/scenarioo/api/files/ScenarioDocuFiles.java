package org.scenarioo.api.files;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.api.util.files.AlphanumericFileComparator;

/**
 * Represents the file structure of the documentation.
 */
public class ScenarioDocuFiles {
	
	private static final String DIRECTORY_NAME_SCENARIO_SCREENSHOTS = "screenshots";
	
	private static final String DIRECTORY_NAME_SCENARIO_STEPS = "steps";
	
	private static final String FILE_NAME_SCENARIO = "scenario.xml";
	
	private static final String FILE_NAME_CASE = "usecase.xml";
	
	private static final String FILE_NAME_BUILD = "build.xml";
	
	private static final String FILE_NAME_BRANCH = "branch.xml";
	
	private static NumberFormat THREE_DIGIT_NUM_FORMAT = createNumberFormatWithMinimumIntegerDigits(3);
	
	private File rootDirectory;
	
	public ScenarioDocuFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	
	public void assertRootDirectoryExists() {
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for docu content generation does not exist: "
					+ rootDirectory.getAbsolutePath());
		}
	}
	
	public File getBranchDirectory(final String branchName) {
		File branchDirectory = new File(rootDirectory, branchName);
		return branchDirectory;
	}
	
	public File getBranchFile(final String branchName) {
		return new File(getBranchDirectory(branchName), FILE_NAME_BRANCH);
	}
	
	public List<File> getBranchFiles() {
		return getListOfFilesFromSubdirs(rootDirectory, FILE_NAME_BRANCH);
	}
	
	public File getBuildDirectory(final String branchName, final String buildName) {
		File buildDirectory = new File(getBranchDirectory(branchName), buildName);
		return buildDirectory;
	}
	
	public File getBuildFile(final String branchName, final String buildName) {
		return new File(getBuildDirectory(branchName, buildName), FILE_NAME_BUILD);
	}
	
	public List<File> getBuildFiles(final String branchName) {
		return getListOfFilesFromSubdirs(getBranchDirectory(branchName), FILE_NAME_BUILD);
	}
	
	public File getUseCaseDirectory(final String branchName, final String buildName, final String useCaseName) {
		File branchDirectory = new File(getBuildDirectory(branchName, buildName), useCaseName);
		return branchDirectory;
	}
	
	public File getUseCaseFile(final String branchName, final String buildName, final String useCaseName) {
		return new File(getUseCaseDirectory(branchName, buildName, useCaseName), FILE_NAME_CASE);
	}
	
	public List<File> getUseCaseFiles(final String branchName, final String buildName) {
		return getListOfFilesFromSubdirs(getBuildDirectory(branchName, buildName), FILE_NAME_CASE);
	}
	
	public File getScenarioDirectory(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		File branchDirectory = new File(getUseCaseDirectory(branchName, buildName, useCaseName), scenarioName);
		return branchDirectory;
	}
	
	public File getScenarioFile(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		return new File(getScenarioDirectory(branchName, buildName, useCaseName, scenarioName), FILE_NAME_SCENARIO);
	}
	
	public List<File> getScenarioFiles(final String branchName, final String buildName, final String useCaseName) {
		return getListOfFilesFromSubdirs(getUseCaseDirectory(branchName, buildName, useCaseName), FILE_NAME_SCENARIO);
	}
	
	public File getStepsDirectory(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		File branchDirectory = new File(getScenarioDirectory(branchName, buildName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_STEPS);
		return branchDirectory;
	}
	
	public File getStepFile(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		return new File(getStepsDirectory(branchName, buildName, useCaseName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".xml");
	}
	
	public List<File> getStepFiles(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		return getListOfFiles(getStepsDirectory(branchName, buildName, useCaseName, scenarioName));
	}
	
	public File getScreenshotsDirectory(final String branchName, final String buildName,
			final String useCaseName, final String scenarioName) {
		return new File(getScenarioDirectory(branchName, buildName, useCaseName, scenarioName),
				DIRECTORY_NAME_SCENARIO_SCREENSHOTS);
	}
	
	public File getScreenshotFile(final String branchName, final String buildName,
			final String useCaseName, final String scenarioName, final int stepIndex) {
		return new File(getScreenshotsDirectory(branchName, buildName, useCaseName, scenarioName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".png");
	}
	
	private static NumberFormat createNumberFormatWithMinimumIntegerDigits(
			final int minimumIntegerDigits) {
		final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		return numberFormat;
	}
	
	/**
	 * Read all files with given name from all subdirectories of 'directory'.
	 */
	private List<File> getListOfFilesFromSubdirs(final File directory, final String filename) {
		List<File> result = new ArrayList<File>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File subDir : listFiles(directory)) {
			if (subDir.isDirectory()) {
				File file = new File(subDir, filename);
				if (file.exists()) {
					result.add(file);
				}
			}
		}
		return result;
	}
	
	/**
	 * Read all files from 'directory'.
	 */
	private List<File> getListOfFiles(final File directory) {
		List<File> result = new ArrayList<File>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File file : listFiles(directory)) {
			if (!file.isDirectory()) {
				result.add(file);
			}
		}
		return result;
	}
	
	/**
	 * List all files in the given directory sorted alphanumerically using a collator.
	 */
	private static File[] listFiles(final File directory) {
		File[] files = directory.listFiles();
		Arrays.sort(files, new AlphanumericFileComparator());
		return files;
	}
	
}
