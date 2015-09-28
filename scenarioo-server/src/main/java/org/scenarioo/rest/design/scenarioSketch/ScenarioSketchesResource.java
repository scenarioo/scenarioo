package org.scenarioo.rest.design.scenarioSketch;

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
import org.scenarioo.dao.design.DesignFiles;
import org.scenarioo.dao.design.DesignReader;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.StepSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.design.scenarioSketch.dto.ScenarioSketchSteps;

@Path("/rest/branch/{branchName}/issue/{issueId}/scenariosketch")
public class ScenarioSketchesResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private static final Logger LOGGER = Logger.getLogger(ScenarioSketchesResource.class);

	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());
	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/{scenarioSketchId}")
	public ScenarioSketchSteps loadScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId) {
		LOGGER.info("Loading scenario sketch (" + branchName + "/" + issueId + "/" + scenarioSketchId + ")");

		final ScenarioSketch scenarioSketch = reader.loadScenarioSketch(branchName, issueId, scenarioSketchId);
		final List<StepSketch> steps = reader.loadSketchSteps(branchName, issueId, scenarioSketchId);
		final ScenarioSketchSteps scenarioSketchSteps = new ScenarioSketchSteps();
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
		LOGGER.info("Storing new scenario sketch (branch " + branchName + ")");

		MessageDigest converter;
		try {
			converter = MessageDigest.getInstance("SHA1");
			String id = new String(Hex.encodeHex(converter.digest(scenarioSketch.toString().getBytes())));
			id = id.substring(0, 8); // limit to first 8 characters, to keep URLs short and collision likelihood small
			scenarioSketch.setScenarioSketchId(id.toString());
		} catch (final NoSuchAlgorithmException e) {
			LOGGER.info("Couldn't generate SHA1 message digest.");
			return Response.serverError().build();
		}
		scenarioSketch.setDateCreated(System.currentTimeMillis());
		scenarioSketch.setDateModified(System.currentTimeMillis());
		files.writeScenarioSketchToFile(branchName, scenarioSketch.getIssueId(), scenarioSketch);
		return Response.ok(scenarioSketch, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/{scenarioSketchId}")
	public Response updateScenarioSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			@PathParam("scenarioSketchId") final String scenarioSketchId, final ScenarioSketch updatedScenarioSketch) {
		LOGGER.info("Updating scenario sketch (" + branchName + "/" + issueId + "/" + scenarioSketchId + ")");

		if (updatedScenarioSketch.getScenarioSketchId() == null) {
			LOGGER.error("There was no scenarioSketchId set on the scenarioSketch object!");
			updatedScenarioSketch.setScenarioSketchId(scenarioSketchId);
		}
		final ScenarioSketch existingScenarioSketch = reader.loadScenarioSketch(branchName, issueId, scenarioSketchId);
		existingScenarioSketch.update(updatedScenarioSketch);
		existingScenarioSketch.setDateModified(System.currentTimeMillis());
		files.updateScenarioSketch(branchName, existingScenarioSketch);

		return Response.ok(updatedScenarioSketch, MediaType.APPLICATION_JSON).build();
	}

}
