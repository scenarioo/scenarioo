package org.scenarioo.dao.lastSuccessfulScenario;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationFiles;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;

public class LastSuccessfulScenariosDao {
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenariosDao.class);
	
	public void copyUseCaseFoldersWithSuccessfulScenarios(final File documentationDataDirectory,
			final BuildIdentifier sourceBuildIdentifier, final BuildIdentifier destinationBuildIdentifier,
			final Date buildDate) {
		File sourceBuildFolder = getBuildFolder(documentationDataDirectory, sourceBuildIdentifier);
		File destinationBuildFolder = getBuildFolder(documentationDataDirectory, destinationBuildIdentifier);
		
		LastSuccessfulScenariosIndex index = getLastSuccessfulScenariosIndex(destinationBuildFolder);
		
		String[] sourceDirectoryContents = sourceBuildFolder.list();
		
		if (sourceDirectoryContents == null) {
			return;
		}
		
		for (String sourceDirectoryEntry : sourceDirectoryContents) {
			if (sourceDirectoryEntry.endsWith(".derived")) {
				continue;
			}
			File sourceFile = new File(sourceBuildFolder, sourceDirectoryEntry);
			
			if (sourceFile.isDirectory()) {
				File destinationUsecaseFolder = new File(destinationBuildFolder, sourceDirectoryEntry);
				destinationUsecaseFolder.mkdirs();
				LOGGER.info("Created " + destinationUsecaseFolder);
				copySuccessfulNewerScenarios(documentationDataDirectory, sourceBuildIdentifier,
						decode(sourceDirectoryEntry), sourceFile, destinationUsecaseFolder, buildDate, index);
			}
		}
		
		saveLastSuccessfulScenariosIndex(index, destinationBuildFolder);
	}
	
	private void copySuccessfulNewerScenarios(final File documentationDataDirectory,
			final BuildIdentifier sourceBuildIdentifier, final String useCaseName, final File sourceFile,
			final File destinationUsecaseFolder, final Date buildDate, final LastSuccessfulScenariosIndex index) {
		
		File[] scenarioFolders = sourceFile.listFiles();
		
		for (File scenarioFolder : scenarioFolders) {
			if (!scenarioFolder.isDirectory() || scenarioFolder.getPath().endsWith(".derived")) {
				continue;
			}
			
			String scenarioName = decode(scenarioFolder.getName());
			
			ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(sourceBuildIdentifier, useCaseName,
					decode(scenarioFolder.getName()));
			
			Date existingScenarioBuildDate = index.getScenarioBuildDate(useCaseName, scenarioName);
			
			if (isScenarioSuccessful(documentationDataDirectory, scenarioIdentifier)
					&& isScenarioNewerThanExisting(existingScenarioBuildDate, buildDate)) {
				try {
					FileUtils.copyDirectoryToDirectory(scenarioFolder, destinationUsecaseFolder);
					index.setScenarioBuildDate(useCaseName, scenarioName, buildDate);
					LOGGER.info("Copied successful scenario: " + scenarioFolder + " to " + destinationUsecaseFolder);
				} catch (IOException e) {
					LOGGER.warn("Can't copy scenario " + scenarioFolder + " to " + destinationUsecaseFolder);
				}
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
			return URLDecoder.decode(string, "URT-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isScenarioSuccessful(final File documentationDataDirectory,
			final ScenarioIdentifier scenarioIdentifier) {
		ScenarioDocuReader reader = new ScenarioDocuReader(documentationDataDirectory);
		Scenario scenario = reader.loadScenario(scenarioIdentifier.getBranchName(), scenarioIdentifier.getBuildName(),
				scenarioIdentifier.getUsecaseName(), scenarioIdentifier.getScenarioName());
		
		return scenario != null && Status.SUCCESS.getKeyword().equals(scenario.getStatus());
	}
	
	private File getBuildFolder(final File documentationDataDirectory, final BuildIdentifier buildIdentifier) {
		ScenarioDocuAggregationFiles files = new ScenarioDocuAggregationFiles(documentationDataDirectory);
		return files.getBuildDirectory(buildIdentifier);
	}
	
	private LastSuccessfulScenariosIndex getLastSuccessfulScenariosIndex(final File lastSuccessfulScenariosBuildFolder) {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile(lastSuccessfulScenariosBuildFolder);
		if (lastSuccessfulScenariosIndexFile.exists()) {
			return ScenarioDocuXMLFileUtil.unmarshal(LastSuccessfulScenariosIndex.class,
					lastSuccessfulScenariosIndexFile);
		}
		return new LastSuccessfulScenariosIndex();
	}
	
	private File getLastSuccessfulScenariosIndexFile(final File lastSuccessfulScenariosBuildFolder) {
		return new File(lastSuccessfulScenariosBuildFolder, "lastSuccessfulScenariosIndex.derived");
	}
	
	private void saveLastSuccessfulScenariosIndex(final LastSuccessfulScenariosIndex index,
			final File lastSuccessfulScenariosBuildFolder) {
		File lastSuccessfulScenariosIndexFile = getLastSuccessfulScenariosIndexFile(lastSuccessfulScenariosBuildFolder);
		ScenarioDocuXMLFileUtil.marshal(index, lastSuccessfulScenariosIndexFile);
	}
	
}
