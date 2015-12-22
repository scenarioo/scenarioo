/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
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

package org.scenarioo.rest.sketcher.issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.dao.sketcher.SketcherFiles;
import org.scenarioo.dao.sketcher.SketcherReader;
import org.scenarioo.model.sketcher.Issue;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.sketcher.issue.dto.IssueSummary;
import org.scenarioo.rest.sketcher.issue.dto.IssueWithSketch;
import org.scenarioo.rest.sketcher.issue.dto.SketchIds;
import org.scenarioo.utils.IdGenerator;

@Path("/rest/branch/{branchName}/issue")
public class IssueResource {

	private static final Logger LOGGER = Logger.getLogger(IssueResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final SketcherReader reader = new SketcherReader(configurationRepository.getDesignDataDirectory());
	private final SketcherFiles files = new SketcherFiles(configurationRepository.getDesignDataDirectory());

	/**
	 * Lists all issues for the given branch. Used to display the branches in the "Sketches" tab on branch level.
	 */
	@GET
	@Produces({ "application/json" })
	public Response loadIssueSummaries(@PathParam("branchName") final String branchName) {
		LOGGER.info("REQUEST: loadIssueSummaries(" + branchName + ")");

		try {
			final List<Issue> issues = reader.loadIssues(branchName);
			return Response.ok(createIssueSummaries(branchName, issues), MediaType.APPLICATION_JSON).build();
		} catch (ResourceNotFoundException e) {
			return Response.noContent().build();
		}
	}

	@GET
	@Produces({ "application/json" })
	@Path("/{issueId}/ids")
	public Response loadSketchIds(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId) {
		LOGGER.info("REQUEST: loadSketchIds(" + branchName + ", " + issueId + ")");

		final IssueWithSketch issueWitchSketch = loadIssueAndSketch(branchName, issueId);

		SketchIds sketchIds = SketchIds.fromIssueWithSketch(issueWitchSketch);

		return Response.ok(sketchIds, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/{issueId}")
	public Response loadIssueWithSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId) {
		LOGGER.info("REQUEST: loadIssueWithSketch(" + branchName + ", " + issueId + ")");

		final IssueWithSketch result = loadIssueAndSketch(branchName, issueId);

		result.getStepSketch().setSvgXmlString(null); // we don't need it here, so we can save some data

		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}



	@POST
	@Consumes("application/json")
	public Response storeNewIssue(@PathParam("branchName") final String branchName,
			final Issue newIssue) {
		LOGGER.info("REQUEST: storeNewIssue(" + branchName + ")");

		Date now = new Date();

		newIssue.setIssueId(IdGenerator.generateRandomId());
		newIssue.setDateCreated(now);
		newIssue.setDateModified(now);

		files.persistIssue(branchName, newIssue);

		return Response.ok(newIssue, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/{issueId}")
	public Response updateIssue(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId,
			final Issue updatedIssue) {
		LOGGER.info("REQUEST: updateIssue(" + branchName + ", " + issueId + ", " + updatedIssue + ")");

		final Issue existingIssue = reader.loadIssue(branchName, issueId);
		existingIssue.setDateModified(new Date());
		existingIssue.setName(updatedIssue.getName());
		existingIssue.setDescription(updatedIssue.getDescription());
		existingIssue.setAuthor(updatedIssue.getAuthor());

		files.persistIssue(branchName, existingIssue);

		return Response.ok(existingIssue, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces("application/json")
	@Path("/related")
	public Response relatedIssues(@PathParam("branchName") final String branchName,
			@QueryParam("objectName") final String objectName, @QueryParam("type") final String type) {
		LOGGER.info("REQUEST: relatedIssues(" + branchName + ", " + objectName + ", " + type + ")");

		try {
			final List<Issue> issues = reader.loadIssues(branchName);
			List<IssueSummary> relatedIssues = selectOnlyRelatedIssues(objectName, type, issues);
			return Response.ok(relatedIssues, MediaType.APPLICATION_JSON).build();
		}
		catch (ResourceNotFoundException e) {
			return Response.noContent().build();
		}
	}

	private List<IssueSummary> selectOnlyRelatedIssues(final String objectName, final String type,
			final List<Issue> issues) {
		final List<IssueSummary> result = new ArrayList<IssueSummary>();
		for (final Issue issue : issues) {
			if (isRelatedIssue(issue, objectName, type)) {
				result.add(IssueSummary.createFromIssue(issue));
			}
		}

		return result;
	}

	private Boolean isRelatedIssue(final Issue issue, final String objectName, final String type) {
		if (type.equals("step")) {
			// Example:
			// /rest/branch/wikipedia-docu-example/issue/related?objectName=%2Fstep%2FDonate%2Ffind_donate_page%2FsearchResults.jsp%2F0%2F0&type=step
			return (issue.getStepContextLink() != null && issue.getStepContextLink().equals(objectName));
		} else if (type.equals("scenario")) {
			// Example: /rest/branch/wikipedia-docu-example/issue/related?objectName=find_donate_page&type=scenario
			// TODO: also match Use Case, not only the scenario name
			return (issue.getScenarioContextName() != null && issue.getScenarioContextName().equals(objectName)) ||
					(issue.getScenarioContextLink() != null && issue.getScenarioContextLink().equals(objectName));
		} else {
			// Example: /rest/branch/wikipedia-docu-example/issue/related?objectName=Donate&type=usecase
			return (issue.getUsecaseContextName() != null && issue.getUsecaseContextName().equals(objectName)) ||
					(issue.getUsecaseContextLink() != null && issue.getUsecaseContextLink().equals(objectName));
		}
	}

	private List<IssueSummary> createIssueSummaries(final String branchName, final List<Issue> issues) {
		final List<IssueSummary> issueSummaries = new LinkedList<IssueSummary>();
		for (final Issue issue : issues) {
			issueSummaries.add(IssueSummary.createFromIssue(issue));
		}
		return issueSummaries;
	}

	private IssueWithSketch loadIssueAndSketch(final String branchName, final String issueId) {
		final IssueWithSketch result = new IssueWithSketch();
		result.setIssue(reader.loadIssue(branchName, issueId));
		result.setScenarioSketch(reader.loadScenarioSketches(branchName, issueId).get(0));
		result.setStepSketch(reader.loadStepSketches(branchName, issueId, result.getScenarioSketch()
				.getScenarioSketchId()).get(0));
		return result;
	}

}