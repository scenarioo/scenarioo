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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.builds.BranchAliasResolver;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.sketcher.SketcherDao;
import org.scenarioo.model.sketcher.Issue;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.sketcher.issue.dto.IssueSummary;
import org.scenarioo.rest.sketcher.issue.dto.IssueWithSketch;
import org.scenarioo.rest.sketcher.issue.dto.SketchIds;
import org.scenarioo.utils.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/rest/branch/{branchName}/issue")
public class IssueResource {

	private static final Logger LOGGER = Logger.getLogger(IssueResource.class);

	private final SketcherDao sketcherDao = new SketcherDao();

	/**
	 * Lists all issues for the given branch. Used to display the branches in the "Sketches" tab on branch level.
	 */
	@GetMapping
	public ResponseEntity loadIssueSummaries(@PathVariable("branchName") final String branchName) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		try {
			final List<Issue> issues = sketcherDao.loadIssues(resolvedBranchName);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(createIssueSummaries(issues));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/{issueId}/ids")
	public ResponseEntity loadSketchIds(@PathVariable("branchName") final String branchName,
			@PathVariable("issueId") final String issueId) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		final IssueWithSketch issueWitchSketch = loadIssueAndSketch(resolvedBranchName, issueId);

		SketchIds sketchIds = SketchIds.fromIssueWithSketch(issueWitchSketch);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(sketchIds);
	}

	@GetMapping("/{issueId}")
	public ResponseEntity loadIssueWithSketch(@PathVariable("branchName") final String branchName,
											  @PathVariable("issueId") final String issueId) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		IssueWithSketch result;

		try {
			result = loadIssueAndSketch(resolvedBranchName, issueId);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		result.getStepSketch().setSvgXmlString(null); // we don't need it here, so we can save some data

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
	}

	@PostMapping
	public ResponseEntity storeNewIssue(@RequestBody final Issue newIssue) {
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

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(newIssue);
	}

	@PostMapping("/{issueId}")
	public ResponseEntity updateIssue(@PathVariable("branchName") final String branchName,
			@PathVariable("issueId") final String issueId,
			@RequestBody  final Issue updatedIssue) {
		LOGGER.info("REQUEST: updateIssue(" + branchName + ", " + issueId + ", " + updatedIssue + ")");

		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);

		final Issue existingIssue = sketcherDao.loadIssue(resolvedBranchName, issueId);
		existingIssue.setDateModified(new Date());
		existingIssue.setName(updatedIssue.getName());
		existingIssue.setDescription(updatedIssue.getDescription());
		existingIssue.setAuthor(updatedIssue.getAuthor());

		sketcherDao.persistIssue(resolvedBranchName, existingIssue);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(existingIssue);
	}

	@GetMapping("/related/{buildName}/{usecaseName}")
	public ResponseEntity relatedIssuesForUsecase(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("usecaseName") final String usecaseName) {
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				"", "", 0, 0);

		return loadRelatedIssues(stepIdentifier);
	}

	@GetMapping("/related/{buildName}/{usecaseName}/{scenarioName}")
	public ResponseEntity relatedIssuesForScenario(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("usecaseName") final String usecaseName,
			@PathVariable("scenarioName") final String scenarioName) {
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				scenarioName, "", 0, 0);

		return loadRelatedIssues(stepIdentifier);
	}

	@GetMapping("/related/{buildName}/{usecaseName}/{scenarioName}/{pageName}/{pageOccurrence}/{stepInPageOccurrence}")
	public ResponseEntity relatedIssuesForStep(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("usecaseName") final String usecaseName,
			@PathVariable("scenarioName") final String scenarioName, @PathVariable("pageName") final String pageName,
			@PathVariable("pageOccurrence") final int pageOccurrence,
			@PathVariable("stepInPageOccurrence") final int stepInPageOccurrence) {
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, usecaseName,
				scenarioName, pageName, pageOccurrence, stepInPageOccurrence);

		return loadRelatedIssues(stepIdentifier);
	}

	private ResponseEntity loadRelatedIssues(final StepIdentifier stepIdentifier) {
		try {
			final List<Issue> issues = sketcherDao.loadIssues(stepIdentifier.getBranchName());
			List<IssueSummary> relatedIssues = selectOnlyRelatedIssues(stepIdentifier, issues);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(relatedIssues);
		}
		catch (ResourceNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}

	private List<IssueSummary> selectOnlyRelatedIssues(final StepIdentifier stepIdentifier,
			final List<Issue> issues) {
		final List<IssueSummary> result = new ArrayList<>();
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

	private List<IssueSummary> createIssueSummaries(final List<Issue> issues) {
		final List<IssueSummary> issueSummaries = new LinkedList<>();
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
