package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.rest.application.ApplicationVersion;
import org.scenarioo.rest.application.ApplicationVersionHolder;

@Path("/rest/version/")
public class VersionResource {

	private static final Logger LOGGER = Logger
			.getLogger(UseCasesResource.class);

	AggregatedDataReader dao = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());

	@GET
	@Produces({ "application/xml", "application/json" })
	public ApplicationVersion getVersionInformation() {
		LOGGER.info("REQUEST: getVersionInformation()");
		return ApplicationVersionHolder.INSTANCE.getApplicationVersion();
	}

}
