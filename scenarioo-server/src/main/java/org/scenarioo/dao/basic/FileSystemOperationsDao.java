package org.scenarioo.dao.basic;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationFiles;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Operations that do only concern file system operations (delete folder, add folder, move folder / files). This DAO
 * does not do anything that is related to the content of files.
 */
public class FileSystemOperationsDao {
	
	private static final Logger LOGGER = Logger.getLogger(FileSystemOperationsDao.class);
	
	public void deleteBuild(final File documentationDataDirectory, final BuildIdentifier buildIdentifier) {
		File buildFolder = getBuildFolder(documentationDataDirectory, buildIdentifier);
		
		if (!buildFolder.exists()) {
			LOGGER.debug("Build folder " + buildFolder.getAbsolutePath()
					+ " does not exist, therefore it can't be deleted.");
			return;
		}
		
		try {
			FileUtils.deleteDirectory(buildFolder);
			LOGGER.info("Deleted build folder " + buildFolder.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("Can't delete build " + buildIdentifier, e);
		}
	}
	
	public void createBuildFolderIfItDoesNotExist(final File documentationDataDirectory,
			final BuildIdentifier buildIdentifier) {
		File buildFolder = getBuildFolder(documentationDataDirectory, buildIdentifier);
		
		if (buildFolder.exists()) {
			LOGGER.debug("Build folder " + buildFolder.getAbsolutePath() + " already exists.");
			return;
		}
		
		buildFolder.mkdirs();
		
		if (buildFolder.exists()) {
			LOGGER.info("Created build folder " + buildFolder.getAbsolutePath());
		} else {
			LOGGER.error("Could not create build folder " + buildFolder.getAbsolutePath());
		}
	}
	
	private File getBuildFolder(final File documentationDataDirectory, final BuildIdentifier buildIdentifier) {
		ScenarioDocuAggregationFiles files = new ScenarioDocuAggregationFiles(documentationDataDirectory);
		return files.getBuildDirectory(buildIdentifier);
	}
	
	public void copyAllUseCases(final File documentationDataDirectory, final BuildIdentifier sourceBuildIdentifier,
			final BuildIdentifier destinationBuildIdentifier) {
		File sourceBuildFolder = getBuildFolder(documentationDataDirectory, sourceBuildIdentifier);
		File destinationBuildFolder = getBuildFolder(documentationDataDirectory, destinationBuildIdentifier);
		
		String[] sourceDirectoryContents = sourceBuildFolder.list();
		
		for (String sourceDirectoryEntry : sourceDirectoryContents) {
			if (sourceDirectoryEntry.endsWith(".derived")) {
				continue;
			}
			File sourceFile = new File(sourceBuildFolder, sourceDirectoryEntry);
			if (sourceFile.isDirectory()) {
				try {
					FileUtils.copyDirectoryToDirectory(sourceFile, destinationBuildFolder);
				} catch (IOException e) {
					LOGGER.warn("Can't copy use case " + sourceDirectoryEntry, e);
				}
			}
		}
	}
	
}
