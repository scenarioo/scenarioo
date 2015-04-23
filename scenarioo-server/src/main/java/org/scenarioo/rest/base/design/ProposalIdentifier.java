package org.scenarioo.rest.base.design;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.rest.base.BuildIdentifier;

public class ProposalIdentifier {

	private final BuildIdentifier buildIdentifier;
	private final String issueName;
	private final String proposalName;

	public ProposalIdentifier(final BuildIdentifier buildIdentifier, final String issueName, final String proposalName) {
		this.buildIdentifier = buildIdentifier;
		this.issueName = issueName;
		this.proposalName = proposalName;
	}

	public ProposalIdentifier withDifferentBuildIdentifier(final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		return new ProposalIdentifier(buildIdentifierBeforeAliasResolution, issueName, proposalName);
	}

	public static ProposalIdentifier fromStepLink(final BuildIdentifier buildIdentifier, final StepLink stepLink) {
		return new ProposalIdentifier(buildIdentifier, stepLink.getUseCaseName(), stepLink.getScenarioName());
	}

	public BuildIdentifier getBuildIdentifier() {
		return buildIdentifier;
	}

	public String getBranchName() {
		return buildIdentifier.getBranchName();
	}

	public String getIssueName() {
		return issueName;
	}

	public String getProposalName() {
		return proposalName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(buildIdentifier).append(issueName).append(proposalName).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(buildIdentifier).append(issueName).append(proposalName).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProposalIdentifier other = (ProposalIdentifier) obj;
		return new EqualsBuilder().append(buildIdentifier, other.getBuildIdentifier())
				.append(issueName, other.getIssueName()).append(proposalName, other.getProposalName()).isEquals();
	}

}
