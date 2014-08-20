package org.scenarioo.business.builds;

import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;

/**
 * The "last successful scenario" build is an artificial build that contains the last successful version of each
 * scenario of a branch.
 * 
 * This build is updated whenever a build is successfully imported or re-imported. After the build is updated, it is
 * re-imported.
 */
public class LastSuccessfulScenarioBuild {
	
	// private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
	// .getConfigurationRepository();
	
	private static final Logger LOGGER = Logger.getLogger(LastSuccessfulScenarioBuild.class);
	
	// private final ScenarioDocuReader reader = new
	// ScenarioDocuReader(configurationRepository.getDocuDataDirectoryPath());
	
	public void updateWithBuild(final BuildImportSummary summary, final boolean lastSuccessfulScenarioBuildEnabled) {
		
		if (!lastSuccessfulScenarioBuildEnabled) {
			LOGGER.info("Config value createLastSuccessfulScenarioBuild = false");
			deleteLastSuccessfulScenarioBuild();
			return;
		}
		
		updateWithBuild(summary);
	}
	
	private void deleteLastSuccessfulScenarioBuild() {
		// TODO Auto-generated method stub
	}
	
	private void updateWithBuild(final BuildImportSummary summary) {
		// TODO Auto-generated method stub
		
	}
	
}
