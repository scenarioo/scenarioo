package org.scenarioo.rest.application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.usecase.UseCasesResource;

@Path("/rest/version/")
public class VersionResource {
	
	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	AggregatedDataReader dao = new ScenarioDocuAggregationDAO(configurationRepository.getDocuDataDirectoryPath());
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public ApplicationVersion getVersionInformation() {
		LOGGER.info("REQUEST: getVersionInformation()");
		return ApplicationVersionHolder.INSTANCE.getApplicationVersion();
	}
	
}
