package org.scenarioo.repository;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationFiles;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;

import com.google.common.base.Preconditions;

/**
 * This class has to be instantiated once for each build that is added to the "last successful scenarios" build.
 */
public class LastSuccessfulScenariosBuildRepository {
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenariosBuildRepository.class);
	
	public static final String LAST_SUCCESSFUL_SCENARIO_BUILD_NAME = "last successful scenarios.derived";
	public static final String LAST_SUCCESSFUL_SCENARIOS_INDEX_FILENAME = "lastSuccessfulScenariosIndex.derived";
	
	private final FileSystemOperationsDao fileSystemOperations = new FileSystemOperationsDao();
	
	private final File documentationDataDirectory;
	private final BuildImportSummary buildImportSummary;
	
	private final BuildIdentifier lastSuccessfulScenariosBuildIdentifier;
	
	private final File lastSuccessfulScenariosBuildFolder;
	
	private LastSuccessfulScenariosIndex index;
	
	private final File importedBuildFolder;
	
	public LastSuccessfulScenariosBuildRepository(final File documentationDataDirectory,
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
		
		index = getLastSuccessfulScenariosIndex(lastSuccessfulScenariosBuildFolder);
		
		createLastSuccessfulBuildDirectoryIfItDoesNotExist(branchName);
		createOrUpdateBuildXmlFile(branchName);
		
		removeUseCasesAndScenariosThatDoNotExistAnymoreIfThisIsTheLatestBuild();
		
		copySuccessfulScenariosToSuccessfulScenariosBuild();
		index.setLatestImportedBuildDate(buildImportSummary.getBuildDescription().getDate());
		
		saveLastSuccessfulScenariosIndex(index);
	}
	
	private void removeUseCasesAndScenariosThatDoNotExistAnymoreIfThisIsTheLatestBuild() {
		Date buildDate = buildImportSummary.getBuildDescription().getDate();
		Date latestImportedBuildDate = index.getLatestImportedBuildDate();
		
		if (latestImportedBuildDate == null) {
			// This just means that no build is imported yet to the "last successful scenarios" build
			// Therefore there's nothing to delete
			return;
		}
		
		if (buildDate.after(latestImportedBuildDate)) {
			removeUseCasesAndScenariosThatDoNotExistAnymore();
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
	
	private void removeUseCasesAndScenariosThatDoNotExistAnymore() {
		String[] useCaseDirectories = getAllDirectoriesThatAreNotDerived(lastSuccessfulScenariosBuildFolder);
		
		for (String useCaseDirectory : useCaseDirectories) {
			if (subdirectoryDoesNotExist(importedBuildFolder, useCaseDirectory)) {
				removeUseCaseFromLastSuccessfulScenarios(useCaseDirectory);
			} else {
				removeScenariosThatDoNotExistAnymore(useCaseDirectory);
			}
		}
	}
	
	private void removeScenariosThatDoNotExistAnymore(final String useCaseDirectory) {
		File useCaseDirectoryFile = new File(lastSuccessfulScenariosBuildFolder, useCaseDirectory);
		File useCaseDirectoryFileInImportedBuilld = new File(importedBuildFolder, useCaseDirectory);
		String[] scenarioDirectories = getAllDirectoriesThatAreNotDerived(useCaseDirectoryFile);
		
		for (String scenarioDirectory : scenarioDirectories) {
			if (subdirectoryDoesNotExist(useCaseDirectoryFileInImportedBuilld, scenarioDirectory)) {
				removeScenarioFromLastSuccessfulScenarios(useCaseDirectoryFile, scenarioDirectory);
			}
		}
	}
	
	private boolean subdirectoryDoesNotExist(final File importedBuildFolder, final String useCase) {
		File useCaseDirectory = new File(importedBuildFolder, useCase);
		return !useCaseDirectory.exists();
	}
	
	private void removeUseCaseFromLastSuccessfulScenarios(final String useCaseDirectory) {
		File useCaseDirectoryFile = new File(lastSuccessfulScenariosBuildFolder, useCaseDirectory);
		try {
			FileUtils.deleteDirectory(useCaseDirectoryFile);
		} catch (IOException e) {
			LOGGER.warn("Could not delete use case that does not exist anymore.", e);
		}
		
		index.removeUseCase(decode(useCaseDirectory));
	}
	
	private void removeScenarioFromLastSuccessfulScenarios(final File useCaseDirectoryFile,
			final String scenarioDirectory) {
		File scenarioDirectoryFile = new File(useCaseDirectoryFile, scenarioDirectory);
		
		try {
			FileUtils.deleteDirectory(scenarioDirectoryFile);
		} catch (IOException e) {
			LOGGER.warn("Could not delete scenario that does not exist anymore.", e);
		}
		
		index.removeScenario(decode(useCaseDirectoryFile.getName()), decode(scenarioDirectory));
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
				copySuccessfulNewerScenarios(decode(sourceDirectoryEntry), sourceFile, destinationUsecaseFolder);
			}
		}
	}
	
	private void copySuccessfulNewerScenarios(final String useCaseName, final File sourceFile,
			final File destinationUsecaseFolder) {
		
		Date buildDate = buildImportSummary.getBuildDescription().getDate();
		File[] scenarioFolders = sourceFile.listFiles();
		
		for (File scenarioFolder : scenarioFolders) {
			if (!scenarioFolder.isDirectory() || scenarioFolder.getPath().contains(".derived")) {
				continue;
			}
			
			String scenarioName = decode(scenarioFolder.getName());
			
			ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildImportSummary.getIdentifier(),
					useCaseName, decode(scenarioFolder.getName()));
			
			Date existingScenarioBuildDate = index.getScenarioBuildDate(useCaseName, scenarioName);
			
			if (isScenarioSuccessful(documentationDataDirectory, scenarioIdentifier)
					&& isScenarioNewerThanExisting(existingScenarioBuildDate, buildDate)) {
				copyDirectoryWithoutDerivedFilesAndDirectories(scenarioFolder, destinationUsecaseFolder);
				index.setScenarioBuildDate(useCaseName, scenarioName, buildDate);
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
	
	private String decode(final String string) {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isScenarioSuccessful(final File documentationDataDirectory,
			final ScenarioIdentifier scenarioIdentifier) {
		ScenarioDocuReader reader = new ScenarioDocuReader(documentationDataDirectory);
		Scenario scenario = null;
		try {
			scenario = reader.loadScenario(scenarioIdentifier.getBranchName(), scenarioIdentifier.getBuildName(),
					scenarioIdentifier.getUsecaseName(), scenarioIdentifier.getScenarioName());
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
	
	public LastSuccessfulScenariosIndex getLastSuccessfulScenariosIndex(final File lastSuccessfulScenariosBuildFolder) {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile(lastSuccessfulScenariosBuildFolder);
		if (lastSuccessfulScenariosIndexFile.exists()) {
			return ScenarioDocuXMLFileUtil.unmarshal(LastSuccessfulScenariosIndex.class,
					lastSuccessfulScenariosIndexFile);
		}
		return new LastSuccessfulScenariosIndex();
	}
	
	private File getLastSuccessfulScenariosIndexFile(final File lastSuccessfulScenariosBuildFolder) {
		return new File(lastSuccessfulScenariosBuildFolder, LAST_SUCCESSFUL_SCENARIOS_INDEX_FILENAME);
	}
	
	private void saveLastSuccessfulScenariosIndex(final LastSuccessfulScenariosIndex index) {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile(lastSuccessfulScenariosBuildFolder);
		ScenarioDocuXMLFileUtil.marshal(index, lastSuccessfulScenariosIndexFile);
	}
	
}
