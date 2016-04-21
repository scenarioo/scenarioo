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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.model.sketcher.Issue;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.sketcher.issue.dto.IssueSummary;
import org.scenarioo.rest.sketcher.issue.dto.IssueWithSketch;
import org.scenarioo.rest.sketcher.issue.dto.SketchIds;
import org.scenarioo.utils.IdGenerator;

@Path("/rest/branch/{branchName}/issue")
public class IssueResource {

	private static final Logger LOGGER = Logger.getLogger(IssueResource.class);

	private final SketcherDao sketcherDao = new SketcherDao();

	/**
	 * Lists all issues for the given branch. Used to display the branches in the "Sketches" tab on branch level.
	 */
	@GET
	@Produces({ "application/json" })
	public Response loadIssueSummaries(@PathParam("branchName") final String branchName) {
		LOGGER.info("REQUEST: loadIssueSummaries(" + branchName + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		try {
			final List<Issue> issues = sketcherDao.loadIssues(resolvedBranchName);
			return Response.ok(createIssueSummaries(resolvedBranchName, issues), MediaType.APPLICATION_JSON).build();
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

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		final IssueWithSketch issueWitchSketch = loadIssueAndSketch(resolvedBranchName, issueId);

		SketchIds sketchIds = SketchIds.fromIssueWithSketch(issueWitchSketch);

		return Response.ok(sketchIds, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/{issueId}")
	public Response loadIssueWithSketch(@PathParam("branchName") final String branchName,
			@PathParam("issueId") final String issueId) {
		LOGGER.info("REQUEST: loadIssueWithSketch(" + branchName + ", " + issueId + ")");

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		IssueWithSketch result = null;

		try {
			result = loadIssueAndSketch(resolvedBranchName, issueId);
		} catch (ResourceNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}

		result.getStepSketch().setSvgXmlString(null); // we don't need it here, so we can save some data

		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	public Response storeNewIssue(final Issue newIssue) {
		LOGGER.info("REQUEST: storeNewIssue(" + newIssue.getRelatedStep().getBranchName() + ")");

		BuildIdentifier resolvedBranchAndBuildAlias = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				newIssue.getRelatedStep().getBranchName(), newIssue.getRelatedStep().getBuildName());
		newIssue.getRelatedStep().setBranchName(resolvedBranchAndBuildAlias.getBranchName());
		newIssue.getRelatedStep().setBuildName(resolvedBranchAndBuildAlias.getBuildName());

		Date now = new Date();

		newIssue.setIssueId(IdGenerator.generateRandomId());
		newIssue.setDateCreated(now);
		newIssue.setDateModified(now);

		sketcherDao.persistIssue(resolvedBranchAndBuildAlias.getBranchName(), newIssue);

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

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);

		final Issue existingIssue = sketcherDao.loadIssue(resolvedBranchName, issueId);
		existingIssue.setDateModified(new Date());
		existingIssue.setName(updatedIssue.getName());
		existingIssue.setDescription(updatedIssue.getDescription());
		existingIssue.setAuthor(updatedIssue.getAuthor());

		sketcherDao.persistIssue(resolvedBranchName, existingIssue);

		return Response.ok(existingIssue, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces("application/json")
	@Path("/related/{buildName}/{usecaseName}")
	public Response relatedIssuesForUsecase(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName) {
		LOGGER.info("REQUEST: relatedIssuesUsecase(" + branchName + ", " + buildName + ", " + usecaseName + ")");

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				"", "", 0, 0);

		return loadRelatedIssues(stepIdentifier);
	}

	@GET
	@Produces("application/json")
	@Path("/related/{buildName}/{usecaseName}/{scenarioName}")
	public Response relatedIssuesForScenario(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName) {
		LOGGER.info("REQUEST: relatedIssuesScenario(" + branchName + ", " + buildName + ", " + usecaseName + ", "
				+ scenarioName + ")");

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				scenarioName, "", 0, 0);

		return loadRelatedIssues(stepIdentifier);
	}

	@GET
	@Produces("application/json")
	@Path("/related/{buildName}/{usecaseName}/{scenarioName}/{pageName}/{pageOccurrence}/{stepInPageOccurrence}")
	public Response relatedIssuesForStep(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("pageName") final String pageName,
			@PathParam("pageOccurrence") final int pageOccurrence,
			@PathParam("stepInPageOccurrence") final int stepInPageOccurrence) {
		LOGGER.info("REQUEST: relatedIssuesStep(" + branchName + ", " + buildName + ", " + usecaseName + ", "
				+ scenarioName + ", " + pageName + ", " + pageOccurrence + ", " + stepInPageOccurrence + ")");

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				scenarioName, pageName, pageOccurrence, stepInPageOccurrence);

		return loadRelatedIssues(stepIdentifier);
	}

	private Response loadRelatedIssues(final StepIdentifier stepIdentifier) {
		try {
			final List<Issue> issues = sketcherDao.loadIssues(stepIdentifier.getBranchName());
			List<IssueSummary> relatedIssues = selectOnlyRelatedIssues(stepIdentifier, issues);
			return Response.ok(relatedIssues, MediaType.APPLICATION_JSON).build();
		}
		catch (ResourceNotFoundException e) {
			return Response.noContent().build();
		}
	}

	private List<IssueSummary> selectOnlyRelatedIssues(final StepIdentifier stepIdentifier,
			final List<Issue> issues) {
		final List<IssueSummary> result = new ArrayList<IssueSummary>();
		for (final Issue issue : issues) {
			if (isRelatedIssue(issue, stepIdentifier)) {
				result.add(IssueSummary.createFromIssue(issue));
			}
		}

		return result;
	}

	private Boolean isRelatedIssue(final Issue issue, final StepIdentifier stepIdentifier) {
		if (issue.getRelatedStep() == null) {
			return false;
		}

		StepIdentifier relatedStep = issue.getRelatedStep();

		return (useCaseIsEqual(stepIdentifier, relatedStep)
				&& scenarioNameNotSetOrEqual(stepIdentifier, relatedStep)
				&& pageAndStepIndexNotSetOrEqual(stepIdentifier, relatedStep));
	}

	private boolean useCaseIsEqual(final StepIdentifier stepIdentifier, final StepIdentifier relatedStep) {
		return relatedStep.getUsecaseName().equals(stepIdentifier.getUsecaseName());
	}

	private boolean scenarioNameNotSetOrEqual(final StepIdentifier stepIdentifier, final StepIdentifier relatedStep) {
		return StringUtils.isBlank(stepIdentifier.getScenarioName()) || stepIdentifier.getScenarioName().equals(
				relatedStep.getScenarioName());
	}

	private boolean pageAndStepIndexNotSetOrEqual(final StepIdentifier stepIdentifier, final StepIdentifier relatedStep) {
		return StringUtils.isBlank(stepIdentifier.getPageName()) || (stepIdentifier.getPageName().equals(
				relatedStep.getPageName())
				&& stepIdentifier.getPageOccurrence() == relatedStep.getPageOccurrence()
				&& stepIdentifier.getStepInPageOccurrence() == relatedStep.getStepInPageOccurrence());
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
		result.setIssue(sketcherDao.loadIssue(branchName, issueId));
		result.setScenarioSketch(sketcherDao.loadScenarioSketches(branchName, issueId).get(0));
		result.setStepSketch(sketcherDao.loadStepSketches(branchName, issueId, result.getScenarioSketch()
				.getScenarioSketchId()).get(0));
		return result;
	}

}