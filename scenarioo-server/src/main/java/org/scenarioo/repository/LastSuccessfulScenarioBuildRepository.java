package org.scenarioo.repository;

import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenarioBuildRepository {
	
	public static final String LAST_SUCCESSFUL_SCENARIO_BUILD_NAME = "last_successful_scenario.derived";
	
	private final FileSystemOperationsDao fileSystemOperations = new FileSystemOperationsDao();
	
	private final ConfigurationRepository configurationRepository;
	
	public LastSuccessfulScenarioBuildRepository(final ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}
	
	public void deleteLastSuccessfulScenarioBuild(final String branchName) {
		BuildIdentifier lSSBuildIdentifier = getLSSBuildIdentifierForBranch(branchName);
		fileSystemOperations.deleteBuild(configurationRepository.getDocumentationDataDirectory(), lSSBuildIdentifier);
	}
	
	public void createLastSuccessfulBuildDirectoryIfItDoesNotExist(final String branchName) {
		BuildIdentifier lSSBuildIdentifier = getLSSBuildIdentifierForBranch(branchName);
		fileSystemOperations.createBuildFolderIfItDoesNotExist(configurationRepository.getDocumentationDataDirectory(),
				lSSBuildIdentifier);
	}
	
	private BuildIdentifier getLSSBuildIdentifierForBranch(final String branchName) {
		return new BuildIdentifier(branchName, LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
	}
	
}
