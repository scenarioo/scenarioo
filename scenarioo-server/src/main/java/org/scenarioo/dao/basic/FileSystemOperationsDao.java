package org.scenarioo.dao.basic;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationFiles;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.io.IOException;

/**
 * Operations that do only concern file system operations (delete folder, add folder, move folder / files). This DAO
 * does not do anything that is related to the content of files.
 */
public class FileSystemOperationsDao {

	private static final Logger LOGGER = Logger.getLogger(FileSystemOperationsDao.class);

	private File documentationDataDirectory;

	public FileSystemOperationsDao() {
		ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
		documentationDataDirectory = configurationRepository.getDocumentationDataDirectory();
	}

	public void deleteBuild(final BuildIdentifier buildIdentifier) {
		File buildFolder = getBuildFolder( buildIdentifier);

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

	public void createBuildFolderIfItDoesNotExist(final BuildIdentifier buildIdentifier) {
		File buildFolder = getBuildFolder(buildIdentifier);

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

	public boolean buildFolderExists(final BuildIdentifier buildIdentifier) {
		return getBuildFolder(buildIdentifier).exists();
	}

	private File getBuildFolder(final BuildIdentifier buildIdentifier) {
		ScenarioDocuAggregationFiles files = new ScenarioDocuAggregationFiles(documentationDataDirectory);
		return files.getBuildDirectory(buildIdentifier);
	}

}
