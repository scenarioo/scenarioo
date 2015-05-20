package org.scenarioo.rest.proposal;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.dao.design.aggregates.IssueAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.aggregates.ScenarioSketchSteps;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.SketchStep;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.scenario.mapper.ScenarioDetailsMapper;
import org.scenarioo.utils.design.readers.DesignReader;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch/{scenarioSketchName}")
public class ScenarioSketchesResource {


	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private static final Logger LOGGER = Logger.getLogger(ScenarioSketchesResource.class);

	private final IssueAggregationDAO dao = new IssueAggregationDAO(
			configurationRepository.getDesignDataDirectory());

	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());
	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());

	private final ScenarioDetailsMapper scenarioDetailsMapper = new ScenarioDetailsMapper();

	@GET
	@Produces({ "application/xml", "application/json" })
	public ScenarioSketchSteps loadScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchName") final String scenarioSketchName) {
		LOGGER.info("REQUEST: Loading scenarioSketch " + scenarioSketchName);

		// ScenarioSketchIdentifier scenarioSketchIdentifier = new ScenarioSketchIdentifier(branchName, issueId,
		// scenarioSketchName);

		// ScenarioSketchSteps scenarioSketchSteps = dao.loadScenarioSketchSteps(scenarioSketchIdentifier);

		ScenarioSketch scenarioSketch = reader.loadScenarioSketch(branchName, issueId, scenarioSketchName);
		List<SketchStep> steps = reader.loadSketchSteps(branchName, issueId, scenarioSketchName);
		ScenarioSketchSteps scenarioSketchSteps = new ScenarioSketchSteps();
		scenarioSketchSteps.setIssue(reader.loadIssue(branchName, issueId));
		scenarioSketchSteps.setScenarioSketch(scenarioSketch);
		scenarioSketchSteps.setSketchSteps(steps);

		// TODO: Investigate whether ProposalDetails and ProposalMapper are necessary
		return scenarioSketchSteps;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response storeScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			final ScenarioSketch scenarioSketch) {
		LOGGER.info("Now storing a scenario sketch.");
		files.writeScenarioSketchToFile(branchName, issueId, scenarioSketch);
		return Response.ok().build();
	}

}
