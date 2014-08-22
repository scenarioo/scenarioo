package org.scenarioo.repository;

import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.rest.base.BuildIdentifier;

public class LastSuccessfulScenarioBuildRepository {
	
	private final ConfigurationRepository configurationRepository;
	
	public LastSuccessfulScenarioBuildRepository(final ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}
	
	public void deleteLastSuccessfulScenarioBuild(final BuildIdentifier lSSBuildIdentifier) {
		ScenarioDocuAggregationDAO scenarioDocuAggregationDAO = new ScenarioDocuAggregationDAO(
				configurationRepository.getDocumentationDataDirectory());
		scenarioDocuAggregationDAO.deleteBuild(lSSBuildIdentifier);
	}
	
}
