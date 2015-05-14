package org.scenarioo.rest.proposal.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;

/**
 * Information used for displaying the overview page of a scenario.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProposalDetails {

	private ScenarioSketch proposal;
	// private ScenarioStatistics scenarioStatistics;
	private Issue issue;

	// private List<PageWithSteps> pagesAndSteps;

	public ScenarioSketch getProposal() {
		return proposal;
	}

	public void setProposal(final ScenarioSketch scenario) {
		this.proposal = scenario;
	}

	// public ScenarioStatistics getScenarioStatistics() {
	// return scenarioStatistics;
	// }
	//
	// public void setScenarioStatistics(final ScenarioStatistics scenarioStatistics) {
	// this.scenarioStatistics = scenarioStatistics;
	// }

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(final Issue issue) {
		this.issue = issue;
	}

}
