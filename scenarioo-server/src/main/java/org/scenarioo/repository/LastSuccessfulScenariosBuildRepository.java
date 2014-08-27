package org.scenarioo.repository;

import java.util.Date;

import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.dao.lastSuccessfulScenario.LastSuccessfulScenariosDao;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenariosBuildRepository {
	
	public static final String LAST_SUCCESSFUL_SCENARIO_BUILD_NAME = "latest successful scenarios.derived";
	
	private final FileSystemOperationsDao fileSystemOperations = new FileSystemOperationsDao();
	private final LastSuccessfulScenariosDao lastSuccessfulScenarioDao = new LastSuccessfulScenariosDao();
	
	private final ConfigurationRepository configurationRepository;
	
	public LastSuccessfulScenariosBuildRepository(final ConfigurationRepository configurationRepository) {
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
	
	public void copyAllUseCasesToLastSuccessfulScenarioBuild(final BuildIdentifier buildIdentifier, final Date buildDate) {
		BuildIdentifier lSSBuildIdentifier = getLSSBuildIdentifierForBranch(buildIdentifier.getBranchName());
		lastSuccessfulScenarioDao
				.copyUseCaseFoldersWithSuccessfulScenarios(configurationRepository.getDocumentationDataDirectory(),
						buildIdentifier, lSSBuildIdentifier, buildDate);
	}
	
	private BuildIdentifier getLSSBuildIdentifierForBranch(final String branchName) {
		return new BuildIdentifier(branchName, LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
	}
	
	public void createOrUpdateBuildXmlFile(final String branchName) {
		Build build = new Build();
		build.setDate(new Date());
		build.setName(LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		build.setRevision("various");
		build.setStatus(Status.SUCCESS);
		
		ScenarioDocuWriter scenarioDocuWriter = new ScenarioDocuWriter(
				configurationRepository.getDocumentationDataDirectory(), branchName,
				LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		scenarioDocuWriter.saveBuildDescription(build);
		scenarioDocuWriter.flush();
	}
	
}
