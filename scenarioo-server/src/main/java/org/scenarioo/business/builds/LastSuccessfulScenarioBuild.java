package org.scenarioo.business.builds;

import org.apache.log4j.Logger;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
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
	
	// private static final String LAST_SUCCESSFUL_SCENARIO_BUILD_NAME = "last_successful_scenario.derived";
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenarioBuild.class);
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	// private final ScenarioDocuReader reader = new ScenarioDocuReader(
	// configurationRepository.getDocumentationDataDirectory());
	
	public void updateWithBuild(final BuildImportSummary summary) {
		Preconditions.checkNotNull(summary);
		
		if (!BuildImportStatus.SUCCESS.equals(summary.getStatus())) {
			LOGGER.warn("Build "
					+ summary.getIdentifier()
					+ " is not successfully imported. Do not call updateWithBuild for builds that are not successfully imported.");
			return;
		}
		
		Configuration configuration = configurationRepository.getConfiguration();
		
		if (!configuration.isCreateLastSuccessfulScenarioBuild()) {
			LOGGER.info("Config value createLastSuccessfulScenarioBuild = false");
			deleteLastSuccessfulScenarioBuild();
			return;
		}
		
		update(summary);
	}
	
	private void update(final BuildImportSummary summary) {
		// BuildIdentifier importedBuildIdentifier = summary.getIdentifier();
		// BuildIdentifier lSSBuildIdentifier = new BuildIdentifier(importedBuildIdentifier.getBranchName(),
		// LAST_SUCCESSFUL_SCENARIO_BUILD_NAME);
		//
		// List<UseCase> importedUseCases = reader.loadUsecases(importedBuildIdentifier.getBranchName(),
		// importedBuildIdentifier.getBuildName());
		//
		// List<UseCase> lSSUseCases = reader.loadUsecases(importedBuildIdentifier.getBranchName(),
		// importedBuildIdentifier.getBuildName());
	}
	
	private void deleteLastSuccessfulScenarioBuild() {
		// TODO Auto-generated method stub
	}
	
}
