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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
import org.scenarioo.rest.base.BuildIdentifier;
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
	 * Lightweight call, which does not send all proposal information.
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<IssueSummary> loadIssueSummaries(@PathParam("branchName") final String branchName) {
		LOGGER.info("REQUEST: loadIssueSummaryList(" + branchName + ")");
		BuildIdentifier ident = new BuildIdentifier(branchName, "");
		List<IssueSummary> result = new LinkedList<IssueSummary>();

		// for (IssueProposals ip : dao.loadIssueProposalsList(ident)) {
		// result.add(mapSummary(ip));
		// }
		// return result;

		// This does not use pre-aggregated data
		// Temporary Solution, probably does not scale
		List<Issue> issues = reader.loadIssues(branchName);
		for (Issue i : issues) {
			List<ScenarioSketch> proposals = reader.loadProposals(branchName, i.getName());
			IssueSummary summary = new IssueSummary();
			summary.setName(i.getName());
			summary.setDescription(i.getDescription());
			summary.setStatus(i.getIssueStatus());
			summary.setNumberOfScenarioSketches(proposals.size());
			summary.setLabels(i.getLabels());
			result.add(summary);
		}

		return result;
	}

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/{issueName}")
	public IssueScenarioSketches loadIssueProposals(@PathParam("branchName") final String branchName,
			@PathParam("issueName") final String issueName) {
		LOGGER.info("REQUEST: loadIssueProposals(" + issueName + ")");
		// return dao.loadIssueProposals(new BuildIdentifier(branchName, ""), issueName);

		// This does not use pre-aggregated data
		// Temporary Solution, probably does not scale
		Issue issue = reader.loadIssue(branchName, issueName);
		List<ScenarioSketch> proposals = reader.loadProposals(branchName, issueName);
		List<ScenarioSketchSummary> summaries = new ArrayList<ScenarioSketchSummary>();
		for (ScenarioSketch p : proposals) {
			ScenarioSketchSummary summary = new ScenarioSketchSummary();
			summary.setScenarioSketch(p);
			summary.setNumberOfSteps(0); // Wrong, but works until pre-calculating data is done
			summaries.add(summary);
		}
		IssueScenarioSketches result = new IssueScenarioSketches();
		result.setIssue(issue);
		result.setScenarioSketches(summaries);
		return result;

	}

	@POST
	@Consumes({ "application/xml", "application/json" })
	public void storeIssue(@PathParam("branchName") final String branchName,
			final Issue newIssue) {
		LOGGER.info("Now storing a new issue.");
		LOGGER.info(newIssue);
		LOGGER.info("-----------------------");
		files.writeIssueToFile(branchName, newIssue);

	}

	private IssueSummary mapSummary(final IssueScenarioSketches issueProposals) {
		IssueSummary summary = new IssueSummary();
		Issue issue = issueProposals.getIssue();
		summary.setName(issue.getName());
		summary.setDescription(issue.getDescription());
		summary.setStatus(issue.getIssueStatus());
		summary.setNumberOfScenarioSketches(issueProposals.getScenarioSketches().size());
		summary.setLabels(issue.getLabels());
		return summary;
	}
}