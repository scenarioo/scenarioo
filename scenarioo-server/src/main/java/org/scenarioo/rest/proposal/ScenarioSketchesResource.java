package org.scenarioo.rest.proposal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.design.aggregates.IssueAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.aggregates.ScenarioSketchSteps;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.design.ScenarioSketchIdentifier;
import org.scenarioo.rest.scenario.mapper.ScenarioDetailsMapper;
import org.scenarioo.utils.design.readers.DesignReader;

@Path("/rest/branch/{branchName}/issue/{issueName}/scenariosketch/{scenarioSketchName}")
public class ScenarioSketchesResource {


	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final IssueAggregationDAO dao = new IssueAggregationDAO(
			configurationRepository.getDesignDataDirectory());

	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());
	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());

	private final ScenarioDetailsMapper scenarioDetailsMapper = new ScenarioDetailsMapper();

	@GET
	@Produces({ "application/xml", "application/json" })
	public ScenarioSketchSteps loadScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("proposalName") final String scenarioSketchName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				"");
		ScenarioSketchIdentifier proposalIdentifier = new ScenarioSketchIdentifier(buildIdentifier, issueId,
				scenarioSketchName);

		ScenarioSketchSteps scenarioSketchSteps = dao.loadScenarioSketchSteps(proposalIdentifier);

		// TODO: Investigate whether ProposalDetails and ProposalMapper are necessary
		return scenarioSketchSteps;
	}

	@POST
	@Consumes({ "application/xml", "application/json" })
	public Response storeScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			final ScenarioSketch scenarioSketch) {
		files.writeScenarioSketchToFile(branchName, issueId, scenarioSketch);
		return Response.ok().build();
	}

}
