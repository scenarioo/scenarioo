package org.scenarioo.rest.base.design;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ScenarioSketchIdentifier {

	private final String branchName;
	private final String issueName;
	private final String scenarioSketchName;

	public ScenarioSketchIdentifier(final String branchName, final String issueName, final String scenarioSketchName) {
		this.branchName = branchName;
		this.issueName = issueName;
		this.scenarioSketchName = scenarioSketchName;
	}

	/*
	 * public ScenarioSketchIdentifier withDifferentBuildIdentifier(final BuildIdentifier
	 * buildIdentifierBeforeAliasResolution) {
	 * return new ScenarioSketchIdentifier(buildIdentifierBeforeAliasResolution, issueName, scenarioSketchName);
	 * }
	 * 
	 * public static ScenarioSketchIdentifier fromStepLink(final BuildIdentifier buildIdentifier, final StepLink
	 * stepLink) {
	 * return new ScenarioSketchIdentifier(buildIdentifier, stepLink.getUseCaseName(), stepLink.getScenarioName());
	 * }
	 */

	public String getBranchName() {
		return branchName;
	}

	public String getIssueName() {
		return issueName;
	}

	public String getScenarioSketchName() {
		return scenarioSketchName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(branchName).append(issueName).append(scenarioSketchName).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(branchName).append(issueName).append(scenarioSketchName).toHashCode();
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
		ScenarioSketchIdentifier other = (ScenarioSketchIdentifier) obj;
		return new EqualsBuilder().append(branchName, other.getBranchName())
				.append(issueName, other.getIssueName()).append(scenarioSketchName, other.getScenarioSketchName()).isEquals();
	}

}
