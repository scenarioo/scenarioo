package org.scenarioo.dao.design.aggregates.issues;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.aggregates.IssueProposals;

/**
 * List of all issues with their proposals
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueProposalsList {

		private String version;
		@XmlElementWrapper(name = "list")
		@XmlElement(name = "issueScenarios")
		private List<IssueProposals> issueProposal;

		public String getVersion() {
			return version;
		}

		public void setVersion(final String version) {
			this.version = version;
		}

		public List<IssueProposals> getIssueProposals() {
			return issueProposal;
		}

		public void setUseCaseScenarios(final List<IssueProposals> issueProposals) {
			this.issueProposal = issueProposals;
		}

}
