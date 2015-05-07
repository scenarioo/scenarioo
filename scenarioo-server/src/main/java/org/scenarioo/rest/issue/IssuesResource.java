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
import org.scenarioo.model.design.aggregates.IssueProposals;
import org.scenarioo.model.design.aggregates.IssueSummary;
import org.scenarioo.model.design.aggregates.ProposalSummary;
import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.Proposal;
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
			List<Proposal> proposals = reader.loadProposals(branchName, i.getName());
			IssueSummary summary = new IssueSummary();
			summary.setName(i.getName());
			summary.setDescription(i.getDescription());
			summary.setStatus(i.getIssueStatus());
			summary.setNumberOfProposals(proposals.size());
			summary.setLabels(i.getLabels());
			result.add(summary);
		}

		return result;
	}

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/{issueName}")
	public IssueProposals loadIssueProposals(@PathParam("branchName") final String branchName,
			@PathParam("issueName") final String issueName) {
		// return dao.loadIssueProposals(new BuildIdentifier(branchName, ""), issueName);

		// This does not use pre-aggregated data
		// Temporary Solution, probably does not scale
		Issue issue = reader.loadIssue(branchName, issueName);
		List<Proposal> proposals = reader.loadProposals(branchName, issueName);
		List<ProposalSummary> summaries = new ArrayList<ProposalSummary>();
		for (Proposal p : proposals) {
			ProposalSummary summary = new ProposalSummary();
			summary.setProposal(p);
			summary.setNumberOfSteps(0); // Wrong, but works until pre-calculating data is done
			summaries.add(summary);
		}
		IssueProposals result = new IssueProposals();
		result.setIssue(issue);
		result.setProposals(summaries);
		return result;

	}

	@POST
	@Consumes({ "application/xml", "application/json" })
	public void storeIssue(@PathParam("branchName") final String branchName,
			final Issue newIssue) {
		files.writeIssueToFile(branchName, newIssue);

	}

	private IssueSummary mapSummary(final IssueProposals issueProposals) {
		IssueSummary summary = new IssueSummary();
		Issue issue = issueProposals.getIssue();
		summary.setName(issue.getName());
		summary.setDescription(issue.getDescription());
		summary.setStatus(issue.getIssueStatus());
		summary.setNumberOfProposals(issueProposals.getProposals().size());
		summary.setLabels(issue.getLabels());
		return summary;
	}
}