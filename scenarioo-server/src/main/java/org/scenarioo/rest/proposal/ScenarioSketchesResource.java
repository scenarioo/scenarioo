package org.scenarioo.rest.proposal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Hex;
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

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch")
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
	@Path("/{scenarioSketchId}")
	public ScenarioSketchSteps loadScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId) {
		LOGGER.info("REQUEST: Loading scenarioSketch " + scenarioSketchId);

		// ScenarioSketchIdentifier scenarioSketchIdentifier = new ScenarioSketchIdentifier(branchName, issueId,
		// scenarioSketchName);

		// ScenarioSketchSteps scenarioSketchSteps = dao.loadScenarioSketchSteps(scenarioSketchIdentifier);

		ScenarioSketch scenarioSketch = reader.loadScenarioSketch(branchName, issueId, scenarioSketchId);
		List<SketchStep> steps = reader.loadSketchSteps(branchName, issueId, scenarioSketchId);
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
			final ScenarioSketch scenarioSketch) {
		LOGGER.info("Now storing a new scenario sketch.");
		MessageDigest converter;
		try {
			converter = MessageDigest.getInstance("SHA1");
			String id = new String(Hex.encodeHex(converter.digest(scenarioSketch.toString().getBytes())));
			id = id.substring(0, 7); // limit to first 7 characters, like in git, to keep URLs short
			scenarioSketch.setScenarioSketchId(id.toString());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.info("Couldn't generate SHA1 message digest.");
			return Response.serverError().build();
		}
		files.writeScenarioSketchToFile(branchName, scenarioSketch.getIssueId(), scenarioSketch);
		return Response.ok(scenarioSketch, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/{scenarioSketchId}")
	public Response updateScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final ScenarioSketch updatedScenarioSketch){
		LOGGER.info("Now updating a scenarioSketch.");
		LOGGER.info(scenarioSketchId);
		LOGGER.info("-----------------------");
		if (updatedScenarioSketch.getScenarioSketchId() == null) {
			LOGGER.error("There was no scenarioSketchId set on the scenarioSketch object!");
			updatedScenarioSketch.setScenarioSketchId(scenarioSketchId);
		}
		ScenarioSketch existingScenarioSketch = reader.loadScenarioSketch(branchName, issueId, scenarioSketchId);
		existingScenarioSketch.update(updatedScenarioSketch);
		files.updateScenarioSketch(branchName, updatedScenarioSketch);

		return Response.ok(updatedScenarioSketch, MediaType.APPLICATION_JSON).build();
	}

}
