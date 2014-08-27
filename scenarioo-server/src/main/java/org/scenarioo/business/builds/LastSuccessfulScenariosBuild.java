package org.scenarioo.business.builds;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.LastSuccessfulScenariosBuildRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import com.google.common.base.Preconditions;

/**
 * The "last successful scenario" build is an artificial build that contains the last successful version of each
 * scenario of a branch.
 * 
 * This build is updated whenever a build is successfully imported or re-imported. After the build is updated, it is
 * re-imported.
 */
public class LastSuccessfulScenariosBuild {
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenariosBuild.class);
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	private final LastSuccessfulScenariosBuildRepository repository = RepositoryLocator.INSTANCE
			.getLastSuccessfulScenarioBuildRepository();
	
	public void updateLastSuccessfulScenarioBuild(final BuildImportSummary summary, final BuildImporter buildImporter,
			final AvailableBuildsList availableBuilds) {
		Preconditions.checkNotNull(summary, "summary must not be null");
		Preconditions.checkNotNull(summary.getIdentifier(), "build identifier must not be null");
		
		if (!BuildImportStatus.SUCCESS.equals(summary.getStatus())) {
			LOGGER.warn("Build "
					+ summary.getIdentifier()
					+ " is not successfully imported. Do not call updateWithBuild for builds that are not successfully imported.");
			return;
		}
		
		if (LastSuccessfulScenariosBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME.equals(summary.getIdentifier()
				.getBuildName())) {
			LOGGER.info("Not importing myself, just to make sure I don't produce any black holes.");
			return;
		}
		
		if (!configurationRepository.getConfiguration().isCreateLastSuccessfulScenarioBuild()) {
			LOGGER.info("Config value createLastSuccessfulScenarioBuild = false");
			repository.deleteLastSuccessfulScenarioBuild(summary.getIdentifier().getBranchName());
			return;
		}
		
		update(summary);
		
		buildImporter.submitBuildForReimport(availableBuilds, new BuildIdentifier(summary.getIdentifier()
				.getBranchName(), LastSuccessfulScenariosBuildRepository.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME));
	}
	
	private void update(final BuildImportSummary summary) {
		LOGGER.info("Config value createLastSuccessfulScenarioBuild = true, starting update of build \"last successful scenario\".");
		
		String branchName = summary.getIdentifier().getBranchName();
		
		repository.createLastSuccessfulBuildDirectoryIfItDoesNotExist(branchName);
		repository.createOrUpdateBuildXmlFile(branchName);
		repository.copyAllUseCasesToLastSuccessfulScenarioBuild(summary.getIdentifier(), summary.getBuildDescription()
				.getDate());
		
		LOGGER.info("Done updating build \"last successful scenario\".");
	}
	
}
