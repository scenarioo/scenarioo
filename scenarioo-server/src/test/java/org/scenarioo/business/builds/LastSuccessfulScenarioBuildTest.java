package org.scenarioo.business.builds;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

public class LastSuccessfulScenarioBuildTest {
	
	private LastSuccessfulScenarioBuild lastSuccessfulScenarioBuild;
	private ConfigurationRepository configurationRepository;
	
	@Before
	public void setupTest() {
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest();
		configurationRepository = RepositoryLocator.INSTANCE.getConfigurationRepository();
		lastSuccessfulScenarioBuild = new LastSuccessfulScenarioBuild();
	}
	
	@Test(expected = NullPointerException.class)
	public void theBuildSummaryParameterMustNotBeNull() {
		lastSuccessfulScenarioBuild.updateWithBuild(null);
	}
	
	@Test
	public void ifTheImportedBuildDoesNotHaveSuccessfulImportStatusNothingIsDone() {
		lastSuccessfulScenarioBuild.updateWithBuild(getSummaryWith(BuildImportStatus.FAILED));
		
		// TODO Assert this somehow...
	}
	
	@Test
	public void ifTheLastSuccessfulScenarioBuildIsDisabledInTheConfigAnExistingSuchBuildIsDeleted() {
		Configuration configuration = configurationRepository.getConfiguration();
		configuration.setCreateLastSuccessfulScenarioBuild(false);
		configurationRepository.updateConfiguration(configuration);
		
		// TODO Create existing "lastSuccessfulScenarioBuild" in current branch
		
		lastSuccessfulScenarioBuild.updateWithBuild(getSummaryWith(BuildImportStatus.SUCCESS));
		
		// TODO Assert that existing "lastSuccessfulScenarioBuild" was deleted in current branch
	}
	
	private BuildImportSummary getSummaryWith(final BuildImportStatus buildImportStatus) {
		BuildImportSummary summary = new BuildImportSummary();
		summary.setStatus(buildImportStatus);
		return summary;
	}
	
}
