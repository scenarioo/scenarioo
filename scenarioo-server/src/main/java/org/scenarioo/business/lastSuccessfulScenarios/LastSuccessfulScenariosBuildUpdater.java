package org.scenarioo.business.lastSuccessfulScenarios;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.dao.aggregates.LastSuccessfulScenariosIndexDao;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationFiles;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.utils.UrlEncoding;

import com.google.common.base.Preconditions;

/**
 * This class has to be instantiated once for each build that is added to the "last successful scenarios" build.
 */
public class LastSuccessfulScenariosBuildUpdater {

	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenariosBuildUpdater.class);

	/**
	 * Name of the build folder.
	 */
	public static final String LAST_SUCCESSFUL_SCENARIO_BUILD_NAME = "last successful scenarios.derived";

	/**
	 * Name used for display in the UI.
	 */
	public static final String LAST_SUCCESSFUL_SCENARIO_BUILD_DISPLAY_NAME = "last successful scenarios";

	private static final String FILE_NAME_FEATURE = "feature.xml";

	private final FileSystemOperationsDao fileSystemOperations = new FileSystemOperationsDao();

	private final File documentationDataDirectory;
	private final BuildImportSummary buildImportSummary;

	private final BuildIdentifier lastSuccessfulScenariosBuildIdentifier;

	private final File lastSuccessfulScenariosBuildFolder;

	private LastSuccessfulScenariosIndex index;

	private final File importedBuildFolder;

	public LastSuccessfulScenariosBuildUpdater(final File documentationDataDirectory,
			final BuildImportSummary buildImportSummary) {
		Preconditions.checkNotNull(buildImportSummary, "buildImportSummary must not be null");
		Preconditions.checkNotNull(buildImportSummary.getIdentifier(), "buildIdentifier must not be null");

		this.documentationDataDirectory = documentationDataDirectory;
		this.buildImportSummary = buildImportSummary;

		lastSuccessfulScenariosBuildIdentifier = getLastSuccessfulScenariosBuildIdentifierForBranch(buildImportSummary
				.getIdentifier().getBranchName());
		lastSuccessfulScenariosBuildFolder = getBuildFolder(documentationDataDirectory,
				lastSuccessfulScenariosBuildIdentifier);
		importedBuildFolder = getBuildFolder(documentationDataDirectory, buildImportSummary.getIdentifier());
	}

	public void deleteLastSuccessfulScenarioBuild() {
		fileSystemOperations.deleteBuild(documentationDataDirectory, lastSuccessfulScenariosBuildIdentifier);
	}

	public void enrichLastSuccessfulScenariosWithBuild() {
		String branchName = buildImportSummary.getIdentifier().getBranchName();

		LastSuccessfulScenariosIndexDao dao = new LastSuccessfulScenariosIndexDao(documentationDataDirectory,
				branchName);
		index = dao.loadLastSuccessfulScenariosIndex();

		createLastSuccessfulBuildDirectoryIfItDoesNotExist(branchName);
		createOrUpdateBuildXmlFile(branchName);

		removeFeaturesAndScenariosThatDoNotExistAnymoreIfThisIsTheLatestBuild();

		copySuccessfulScenariosToSuccessfulScenariosBuild();

		copyFeatureXmlFilesWhereverNecessary();

		index.setLatestImportedBuildDate(buildImportSummary.getBuildDescription().getDate());

		dao.saveLastSuccessfulScenariosIndex(index);
	}

	private void removeFeaturesAndScenariosThatDoNotExistAnymoreIfThisIsTheLatestBuild() {
		Date buildDate = buildImportSummary.getBuildDescription().getDate();
		Date latestImportedBuildDate = index.getLatestImportedBuildDate();

		if (latestImportedBuildDate == null) {
			// This just means that no build is imported yet to the "last successful scenarios" build
			// Therefore there's nothing to delete
			return;
		}

		if (buildDate.after(latestImportedBuildDate)) {
			removeFeaturesAndScenariosThatDoNotExistAnymore();
		}
	}

	public void createLastSuccessfulBuildDirectoryIfItDoesNotExist(final String branchName) {
		BuildIdentifier lSSBuildIdentifier = getLastSuccessfulScenariosBuildIdentifierForBranch(branchName);
		fileSystemOperations.createBuildFolderIfItDoesNotExist(documentationDataDirectory, lSSBuildIdentifier);
	}

	private BuildIdentifier getLastSuccessfulScenariosBuildIdentifierForBranch(final String branchName) {
		return new BuildIdentifier(branchName, LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
	}

	public void createOrUpdateBuildXmlFile(final String branchName) {
		Build build = new Build();
		build.setDate(new Date());
		build.setName(LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		build.setRevision("various");
		build.setStatus(Status.SUCCESS);

		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(documentationDataDirectory, branchName,
				LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		scenarioDocuWriter.saveBuildDescription(build);
		scenarioDocuWriter.flush();
	}

	private void removeFeaturesAndScenariosThatDoNotExistAnymore() {
		String[] featureDirectories = getAllDirectoriesThatAreNotDerived(lastSuccessfulScenariosBuildFolder);

		for (String featureDirectory : featureDirectories) {
			if (subdirectoryDoesNotExist(importedBuildFolder, featureDirectory)) {
				removeFeatureFromLastSuccessfulScenarios(featureDirectory);
			} else {
				removeScenariosThatDoNotExistAnymore(featureDirectory);
			}
		}
	}

	private void removeScenariosThatDoNotExistAnymore(final String featureDirectory) {
		File featureDirectoryFile = new File(lastSuccessfulScenariosBuildFolder, featureDirectory);
		File featureDirectoryFileInImportedBuilld = new File(importedBuildFolder, featureDirectory);
		String[] scenarioDirectories = getAllDirectoriesThatAreNotDerived(featureDirectoryFile);

		for (String scenarioDirectory : scenarioDirectories) {
			if (subdirectoryDoesNotExist(featureDirectoryFileInImportedBuilld, scenarioDirectory)) {
				removeScenarioFromLastSuccessfulScenarios(featureDirectoryFile, scenarioDirectory);
			}
		}
	}

	private boolean subdirectoryDoesNotExist(final File importedBuildFolder, final String feature) {
		File featureDirectory = new File(importedBuildFolder, feature);
		return !featureDirectory.exists();
	}

	private void removeFeatureFromLastSuccessfulScenarios(final String featureDirectory) {
		File featureDirectoryFile = new File(lastSuccessfulScenariosBuildFolder, featureDirectory);
		try {
			FileUtils.deleteDirectory(featureDirectoryFile);
		} catch (IOException e) {
			LOGGER.warn("Could not delete feature that does not exist anymore.", e);
		}

		index.removeFeature(UrlEncoding.decode(featureDirectory));
	}

	private void removeScenarioFromLastSuccessfulScenarios(final File featureDirectoryFile,
			final String scenarioDirectory) {
		File scenarioDirectoryFile = new File(featureDirectoryFile, scenarioDirectory);

		try {
			FileUtils.deleteDirectory(scenarioDirectoryFile);
		} catch (IOException e) {
			LOGGER.warn("Could not delete scenario that does not exist anymore.", e);
		}

		index.removeScenario(UrlEncoding.decode(featureDirectoryFile.getName()), UrlEncoding.decode(scenarioDirectory));
	}

	private String[] getAllDirectoriesThatAreNotDerived(final File parentDirectory) {
		return parentDirectory.list(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				File file = new File(dir, name);
				return file.isDirectory() && !name.contains(".derived");
			}
		});
	}

	private String[] getFilesAndDirectoriesThatAreNotDerived(final File parentDirectory) {
		return parentDirectory.list(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return !name.contains(".derived");
			}
		});
	}

	public void copySuccessfulScenariosToSuccessfulScenariosBuild() {
		File sourceBuildFolder = getBuildFolder(documentationDataDirectory, buildImportSummary.getIdentifier());
		File destinationBuildFolder = lastSuccessfulScenariosBuildFolder;

		String[] sourceDirectoryContents = sourceBuildFolder.list();

		if (sourceDirectoryContents == null) {
			return;
		}

		for (String sourceDirectoryEntry : sourceDirectoryContents) {
			if (sourceDirectoryEntry.contains(".derived")) {
				continue;
			}
			File sourceFile = new File(sourceBuildFolder, sourceDirectoryEntry);

			if (sourceFile.isDirectory()) {
				File destinationUsecaseFolder = new File(destinationBuildFolder, sourceDirectoryEntry);
				if (!destinationUsecaseFolder.exists()) {
					destinationUsecaseFolder.mkdirs();
					LOGGER.info("Created " + destinationUsecaseFolder);
				}
				copySuccessfulNewerScenarios(UrlEncoding.decode(sourceDirectoryEntry), sourceFile,
						destinationUsecaseFolder);
			}
		}
	}

	private void copyFeatureXmlFilesWhereverNecessary() {
		File sourceBuildFolder = getBuildFolder(documentationDataDirectory, buildImportSummary.getIdentifier());
		File destinationBuildFolder = lastSuccessfulScenariosBuildFolder;

		String[] featureDirectories = getAllDirectoriesThatAreNotDerived(destinationBuildFolder);

		for (String featureDirectory : featureDirectories) {
			copyFeatureXmlIfNecessary(sourceBuildFolder, destinationBuildFolder, featureDirectory);
		}
	}

	private void copyFeatureXmlIfNecessary(final File sourceBuildFolder, final File destinationBuildFolder,
			final String featureDirectory) {
		File destinationFeatureXmlFile = new File(destinationBuildFolder, featureDirectory + "/" + FILE_NAME_FEATURE);
		String featureName = UrlEncoding.decode(featureDirectory);
		if (!destinationFeatureXmlFile.exists() || destinationFeatureXmlFileIsNotNewerThanCurrentBuild(featureName)) {
			copyFeatureXmlFile(destinationFeatureXmlFile, sourceBuildFolder, featureDirectory);
		}
	}

	private boolean destinationFeatureXmlFileIsNotNewerThanCurrentBuild(final String featureName) {
		Date buildDate = buildImportSummary.getBuildDescription().getDate();
		Date latestBuildDateOfFeature = index.getLatestBuildDateOfFeature(featureName);
		return latestBuildDateOfFeature == null || !latestBuildDateOfFeature.after(buildDate);
	}

	private void copyFeatureXmlFile(final File destinationFeatureXmlFile, final File sourceBuildFolder,
			final String featureDirectory) {
		File sourceFeatureXmlFile = new File(sourceBuildFolder, featureDirectory + "/" + FILE_NAME_FEATURE);
		if (sourceFeatureXmlFile.exists()) {
			copyFeatureXmlFileAndSetStatusToSuccess(featureDirectory);
		} else {
			LOGGER.warn("File " + sourceFeatureXmlFile + " does not exist.");
		}
	}

	private void copyFeatureXmlFileAndSetStatusToSuccess(final String featureDirectory) {
		Feature feature = readFeature(buildImportSummary.getIdentifier(), UrlEncoding.decode(featureDirectory));
		feature.setStatus(Status.SUCCESS);
		saveFeatureXmlInLastSuccessfulScenariosBuild(UrlEncoding.decode(featureDirectory), feature);
	}

	private void saveFeatureXmlInLastSuccessfulScenariosBuild(final String featureName, final Feature feature) {
		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(documentationDataDirectory, buildImportSummary
				.getIdentifier().getBranchName(),
				LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		scenarioDocuWriter.saveFeature(feature);
		scenarioDocuWriter.flush();
	}

	private Feature readFeature(final BuildIdentifier buildIdentifier, final String featureName) {
		ScenarioDocuReader scenarioDocuReader = new ScenarioDocuReader(documentationDataDirectory);
		return scenarioDocuReader.loadUsecase(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
				featureName);
	}

	private void copySuccessfulNewerScenarios(final String featureName, final File sourceFile,
			final File destinationUsecaseFolder) {

		Date buildDate = buildImportSummary.getBuildDescription().getDate();
		File[] scenarioFolders = sourceFile.listFiles();

		for (File scenarioFolder : scenarioFolders) {
			if (!scenarioFolder.isDirectory() || scenarioFolder.getPath().contains(".derived")) {
				continue;
			}

			String scenarioName = UrlEncoding.decode(scenarioFolder.getName());

			ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildImportSummary.getIdentifier(),
					featureName, scenarioName);

			Date existingScenarioBuildDate = index.getScenarioBuildDate(featureName, scenarioName);

			if (isScenarioSuccessful(documentationDataDirectory, scenarioIdentifier)
					&& isScenarioNewerThanExisting(existingScenarioBuildDate, buildDate)) {
				copyDirectoryWithoutDerivedFilesAndDirectories(scenarioFolder, destinationUsecaseFolder);
				index.setScenarioBuildDate(featureName, scenarioName, buildDate);
				LOGGER.info("Copied successful scenario: " + scenarioFolder + " to " + destinationUsecaseFolder);
			}
		}
	}

	private void copyDirectoryWithoutDerivedFilesAndDirectories(final File scenarioFolder,
			final File destinationUsecaseFolder) {
		File destinationScenarioFolder = new File(destinationUsecaseFolder, scenarioFolder.getName());
		destinationScenarioFolder.mkdirs();

		String[] filesAndDirectories = getFilesAndDirectoriesThatAreNotDerived(scenarioFolder);

		copyListOfFiles(filesAndDirectories, scenarioFolder, destinationScenarioFolder);
	}

	private void copyListOfFiles(final String[] filesAndDirectories, final File sourceDirectory,
			final File destinationDirectory) {
		for (String fileOrDirectoryString : filesAndDirectories) {
			File fileOrDirectory = new File(sourceDirectory, fileOrDirectoryString);
			try {
				if (fileOrDirectory.isDirectory()) {
					FileUtils.copyDirectoryToDirectory(fileOrDirectory, destinationDirectory);
				} else {
					FileUtils.copyFileToDirectory(fileOrDirectory, destinationDirectory);
				}
			} catch (IOException e) {
				LOGGER.warn("Copying " + fileOrDirectory + " to " + destinationDirectory + " failed.");
			}
		}
	}

	private boolean isScenarioNewerThanExisting(final Date existingScenarioBuildDate, final Date buildDate) {
		if (existingScenarioBuildDate == null) {
			return true;
		}
		return buildDate.after(existingScenarioBuildDate);
	}

	public boolean isScenarioSuccessful(final File documentationDataDirectory,
			final ScenarioIdentifier scenarioIdentifier) {
		ScenarioDocuReader reader = new ScenarioDocuReader(documentationDataDirectory);
		Scenario scenario = null;
		try {
			scenario = reader.loadScenario(scenarioIdentifier.getBranchName(), scenarioIdentifier.getBuildName(),
					scenarioIdentifier.getFeatureName(), scenarioIdentifier.getScenarioName());
		} catch (ResourceNotFoundException e) {
			LOGGER.warn("Can't read scenario.xml file for scenario " + scenarioIdentifier + " in directory "
					+ documentationDataDirectory);
		}

		return scenario != null && Status.SUCCESS.getKeyword().equals(scenario.getStatus());
	}

	public File getBuildFolder(final File documentationDataDirectory, final BuildIdentifier buildIdentifier) {
		ScenarioDocuAggregationFiles files = new ScenarioDocuAggregationFiles(documentationDataDirectory);
		return files.getBuildDirectory(buildIdentifier);
	}

}
