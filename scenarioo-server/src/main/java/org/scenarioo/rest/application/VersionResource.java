package org.scenarioo.rest.application;

import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.dao.version.ApplicationVersion;
import org.scenarioo.dao.version.ApplicationVersionHolder;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.usecase.UseCasesResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/version")
public class VersionResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	AggregatedDocuDataReader dao = new ScenarioDocuAggregationDao(configurationRepository.getDocumentationDataDirectory());

	@GetMapping
	public ApplicationVersion getVersionInformation() {
		return ApplicationVersionHolder.INSTANCE.getApplicationVersion();
	}

}
