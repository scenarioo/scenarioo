package org.scenarioo.rest.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.scenarioo.model.docu.aggregates.steps.StepLink;

public class ScenarioIdentifier {
	
	private BuildIdentifier buildIdentifier;
	private String usecaseName;
	private String scenarioName;
	
	/**
	 * Just for XML processing.
	 */
	public ScenarioIdentifier() {
		buildIdentifier = new BuildIdentifier();
	}

	public ScenarioIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName) {
		this.buildIdentifier = buildIdentifier;
		this.usecaseName = usecaseName;
		this.scenarioName = scenarioName;
	}
	
	public ScenarioIdentifier withDifferentBuildIdentifier(final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		return new ScenarioIdentifier(buildIdentifierBeforeAliasResolution, usecaseName, scenarioName);
	}
	
	public static ScenarioIdentifier fromStepLink(final BuildIdentifier buildIdentifier, final StepLink stepLink) {
		return new ScenarioIdentifier(buildIdentifier, stepLink.getUseCaseName(), stepLink.getScenarioName());
	}
	
	public BuildIdentifier getBuildIdentifier() {
		return buildIdentifier;
	}
	
	public void setBuildIdentifier(final BuildIdentifier buildIdentifier) {
		this.buildIdentifier = buildIdentifier;
	}

	public String getBranchName() {
		return buildIdentifier.getBranchName();
	}
	
	public void setBranchName(final String branchName) {
		buildIdentifier.setBranchName(branchName);
	}

	public String getBuildName() {
		return buildIdentifier.getBuildName();
	}
	
	public void setBuildName(final String buildName) {
		buildIdentifier.setBuildName(buildName);
	}

	public String getUsecaseName() {
		return usecaseName;
	}
	
	public void setUsecaseName(final String usecaseName) {
		this.usecaseName = usecaseName;
	}

	public String getScenarioName() {
		return scenarioName;
	}
	
	public void setScenarioName(final String scenarioName) {
		this.scenarioName = scenarioName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(buildIdentifier).append(usecaseName).append(scenarioName).toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(buildIdentifier).append(usecaseName).append(scenarioName).toHashCode();
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
		ScenarioIdentifier other = (ScenarioIdentifier) obj;
		return new EqualsBuilder().append(buildIdentifier, other.getBuildIdentifier())
				.append(usecaseName, other.getUsecaseName()).append(scenarioName, other.getScenarioName()).isEquals();
	}
	
}
