package org.scenarioo.business.builds;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.LastSuccessfulScenarioBuildRepository;
import org.scenarioo.repository.RepositoryLocator;

import com.google.common.base.Preconditions;

/**
 * The "last successful scenario" build is an artificial build that contains the last successful version of each
 * scenario of a branch.
 * 
 * This build is updated whenever a build is successfully imported or re-imported. After the build is updated, it is
 * re-imported.
 */
public class LastSuccessfulScenarioBuild {
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenarioBuild.class);
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	private final LastSuccessfulScenarioBuildRepository lastSuccessfulScenarioBuildRepository = RepositoryLocator.INSTANCE
			.getLastSuccessfulScenarioBuildRepository();
	
	// private final ScenarioDocuReader reader = new ScenarioDocuReader(
	// configurationRepository.getDocumentationDataDirectory());
	
	public void updateLastSuccessfulScenarioBuild(final BuildImportSummary summary) {
		Preconditions.checkNotNull(summary, "summary must not be null");
		Preconditions.checkNotNull(summary.getIdentifier(), "build identifier must not be null");
		
		if (!BuildImportStatus.SUCCESS.equals(summary.getStatus())) {
			LOGGER.warn("Build "
					+ summary.getIdentifier()
					+ " is not successfully imported. Do not call updateWithBuild for builds that are not successfully imported.");
			return;
		}
		
		if (!configurationRepository.getConfiguration().isCreateLastSuccessfulScenarioBuild()) {
			LOGGER.info("Config value createLastSuccessfulScenarioBuild = false");
			deleteLastSuccessfulScenarioBuild(summary);
			return;
		}
		
		update(summary);
	}
	
	private void update(final BuildImportSummary summary) {
		LOGGER.info("Config value createLastSuccessfulScenarioBuild = true, starting update of build \"last successful scenario\".");
		
		createLastSuccessfulBuildDirectoryIfItDoesNotExist(summary.getIdentifier().getBranchName());
		// BuildIdentifier importedBuildIdentifier = summary.getIdentifier();
		// BuildIdentifier lSSBuildIdentifier = new BuildIdentifier(importedBuildIdentifier.getBranchName(),
		// LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		//
		// List<UseCase> importedUseCases = reader.loadUsecases(importedBuildIdentifier.getBranchName(),
		// importedBuildIdentifier.getBuildName());
		//
		// List<UseCase> lSSUseCases = reader.loadUsecases(importedBuildIdentifier.getBranchName(),
		// importedBuildIdentifier.getBuildName());
		LOGGER.info("Done updating build \"last successful scenario\".");
	}
	
	private void createLastSuccessfulBuildDirectoryIfItDoesNotExist(final String branchName) {
		lastSuccessfulScenarioBuildRepository.createLastSuccessfulBuildDirectoryIfItDoesNotExist(branchName);
	}
	
	private void deleteLastSuccessfulScenarioBuild(final BuildImportSummary summary) {
		lastSuccessfulScenarioBuildRepository
				.deleteLastSuccessfulScenarioBuild(summary.getIdentifier().getBranchName());
	}
	
}
