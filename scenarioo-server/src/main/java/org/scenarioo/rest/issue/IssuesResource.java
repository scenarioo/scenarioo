/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.rest.issue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.scenarioo.dao.design.aggregates.IssueAggregationDAO;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.aggregates.IssueScenarioSketches;
import org.scenarioo.model.design.aggregates.IssueSummary;
import org.scenarioo.model.design.aggregates.ScenarioSketchSummary;
import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.design.readers.DesignReader;

@Path("/rest/branch/{branchName}/issue")
public class IssuesResource {

	private static final Logger LOGGER = Logger.getLogger(IssuesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final IssueAggregationDAO dao = new IssueAggregationDAO(configurationRepository.getDesignDataDirectory());

	// TODO: Extract the functionality these provide into separate classes
	private final DesignReader reader = new DesignReader(configurationRepository.getDesignDataDirectory());
	private final DesignFiles files = new DesignFiles(configurationRepository.getDesignDataDirectory());

	/**
	 * Lightweight call, which does not send all scenario sketch information.
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<IssueSummary> loadIssueSummaries(@PathParam("branchName") final String branchName) {
		LOGGER.info("REQUEST: loadIssueSummaryList(" + branchName + ")");
		final List<IssueSummary> result = new LinkedList<IssueSummary>();

		// This does not use pre-aggregated data
		// Temporary Solution, probably does not scale
		final List<Issue> issues = reader.loadIssues(branchName);
		for (final Issue i : issues) {
			final IssueSummary summary = summarizeIssue(branchName, i);

			result.add(summary);
		}
		return result;
	}

	@DELETE
	@Path("/{issueId}")
	public Response deleteIssue(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId) {
		LOGGER.warn("Now deleting issue " + issueId);
		if (files.deleteIssue(branchName, issueId)) {
			return Response.ok().build();
		} else {
			return Response.serverError().build();
		}

	}

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/{issueId}")
	public Response loadIssueScenarioSketches(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId) {
		LOGGER.info("REQUEST: loadIssueScenarioSketches(" + issueId + ")");
		// return dao.loadIssueProposals(new BuildIdentifier(branchName, ""), issueId);

		// This does not use pre-aggregated data
		// Temporary Solution, probably does not scale
		final Issue issue = reader.loadIssue(branchName, issueId);
		final List<ScenarioSketch> scenarioSketches = reader.loadScenarioSketches(branchName, issueId);
		final List<ScenarioSketchSummary> summaries = new ArrayList<ScenarioSketchSummary>();
		for (final ScenarioSketch sketch : scenarioSketches) {
			final ScenarioSketchSummary summary = new ScenarioSketchSummary();
			summary.setScenarioSketch(sketch);
			summary.setNumberOfSteps(reader.loadSketchSteps(branchName, issueId, sketch.getScenarioSketchId()).size());
			summaries.add(summary);
		}
		final IssueScenarioSketches result = new IssueScenarioSketches();
		result.setIssue(issue);
		result.setScenarioSketches(summaries);
		LOGGER.info(result);
		// return result;
		return Response.ok(result, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Consumes("application/json")
	public Response storeNewIssue(@PathParam("branchName") final String branchName,
			final Issue newIssue) {
		// TODO: Make sure we do not overwrite an existing issue without confirmation! If we do, do not overwrite the
		// hash. Maybe have a different URL, using the id for accessing/updating existing issues
		LOGGER.info("Now storing a new issue.");
		LOGGER.info(newIssue);
		LOGGER.info("-----------------------");
		MessageDigest converter;
		try {
			converter = MessageDigest.getInstance("SHA1");
			String id = new String(Hex.encodeHex(converter.digest(newIssue.toString().getBytes())));
			id = id.substring(0, 8); // limit to first 8 characters, to keep URLs short and collision likelihood small
			newIssue.setIssueId(id.toString());
		} catch (final NoSuchAlgorithmException e) {
			LOGGER.info("Couldn't generate SHA1 message digest.");
			return Response.serverError().build();
		}
		files.writeIssueToFile(branchName, newIssue);
		return Response.ok(newIssue, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/{issueId}")
	public Response updateIssue(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			final Issue updatedIssue) {
		LOGGER.info("Now updating an existing issue.");
		LOGGER.info(issueId);
		LOGGER.info("-----------------------");
		if (updatedIssue.getIssueId() == null) {
			LOGGER.error("There was no IssueID set on the issue object!");
			updatedIssue.setIssueId(issueId);
		}
		final Issue existingIssue = reader.loadIssue(branchName, issueId);
		existingIssue.update(updatedIssue);
		files.updateIssue(branchName, existingIssue);

		return Response.ok(existingIssue, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces("application/json")
	@Path("/related")
	public List<IssueSummary> relatedIssues(@PathParam("branchName") final String branchName,
			@QueryParam("objectName") final String objectName, @QueryParam("type") final String type) {
		LOGGER.info("Returning issues related to " + objectName);
		final List<Issue> issues = reader.loadIssues(branchName);
		final List<IssueSummary> result = new ArrayList<IssueSummary>();
		for (final Issue i : issues) {
			if (nameMatches(i, objectName, type)) {
				final IssueSummary summary = summarizeIssue(branchName, i);
				result.add(summary);
			}
		}

		return result;
	}

	private Boolean nameMatches(final Issue issue, final String objectName, final String type) {
		switch (type) {
		case "scenario":
			// The frontend uses the version with underscores internally, which is used to represent the link here
			return issue.getScenarioContextLink() != null && issue.getScenarioContextLink().equals(objectName);
		default:
			return issue.getUsecaseContextName() != null && issue.getUsecaseContextName().equals(objectName);
		}

	}

	private IssueSummary summarizeIssue(final String branchName, final Issue issue) {
		final List<ScenarioSketch> scenarioSketches = reader.loadScenarioSketches(branchName, issue.getIssueId());
		final IssueSummary summary = new IssueSummary();
		summary.setName(issue.getName());
		summary.setId(issue.getIssueId());
		summary.setDescription(issue.getDescription());
		summary.setAuthor(issue.getAuthor());
		summary.setUsecaseContextName(issue.getUsecaseContextName());
		summary.setUsecaseContextLink(issue.getUsecaseContextLink());
		summary.setScenarioContextName(issue.getScenarioContextName());
		summary.setScenarioContextLink(issue.getScenarioContextLink());
		summary.setStatus(issue.getIssueStatus());
		summary.setNumberOfScenarioSketches(scenarioSketches.size());
		summary.setLabels(issue.getLabels());

		if (scenarioSketches.size() > 0) {
			final ScenarioSketch firstScenarioSketch = scenarioSketches.get(0);
			summary.setFirstScenarioSketchId(firstScenarioSketch.getScenarioSketchId());
		}
		return summary;
	}

	/*
	 * private IssueSummary mapSummary(final IssueScenarioSketches issueProposals) {
	 * final IssueSummary summary = new IssueSummary();
	 * final Issue issue = issueProposals.getIssue();
	 * summary.setName(issue.getName());
	 * summary.setDescription(issue.getDescription());
	 * summary.setAuthor(issue.getAuthor());
	 * summary.setUsecaseContext(issue.getUsecaseContextLink());
	 * summary.setStatus(issue.getIssueStatus());
	 * summary.setNumberOfScenarioSketches(issueProposals.getScenarioSketches().size());
	 * summary.setLabels(issue.getLabels());
	 * return summary;
	 * }
	 */
}