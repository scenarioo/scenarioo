package org.scenarioo.rest.base.design;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.rest.base.BuildIdentifier;

public class ScenarioSketchIdentifier {

	private final BuildIdentifier buildIdentifier;
	private final String issueName;
	private final String scenarioSketchName;

	public ScenarioSketchIdentifier(final BuildIdentifier buildIdentifier, final String issueName, final String scenarioSketchName) {
		this.buildIdentifier = buildIdentifier;
		this.issueName = issueName;
		this.scenarioSketchName = scenarioSketchName;
	}

	public ScenarioSketchIdentifier withDifferentBuildIdentifier(final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		return new ScenarioSketchIdentifier(buildIdentifierBeforeAliasResolution, issueName, scenarioSketchName);
	}

	public static ScenarioSketchIdentifier fromStepLink(final BuildIdentifier buildIdentifier, final StepLink stepLink) {
		return new ScenarioSketchIdentifier(buildIdentifier, stepLink.getUseCaseName(), stepLink.getScenarioName());
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

	public String getScenarioSketchName() {
		return scenarioSketchName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(buildIdentifier).append(issueName).append(scenarioSketchName).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(buildIdentifier).append(issueName).append(scenarioSketchName).toHashCode();
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
		return new EqualsBuilder().append(buildIdentifier, other.getBuildIdentifier())
				.append(issueName, other.getIssueName()).append(scenarioSketchName, other.getScenarioSketchName()).isEquals();
	}

}
