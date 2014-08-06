package org.scenarioo.rest.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ScenarioIdentifier {
	
	private final BuildIdentifier buildIdentifier;
	private final String usecaseName;
	private final String scenarioName;
	
	public ScenarioIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName) {
		this.buildIdentifier = buildIdentifier;
		this.usecaseName = usecaseName;
		this.scenarioName = scenarioName;
	}
	
	public static ScenarioIdentifier clone(final ScenarioIdentifier scenarioIdentifier) {
		return new ScenarioIdentifier(BuildIdentifier.clone(scenarioIdentifier.getBuildIdentifier()),
				scenarioIdentifier.getUsecaseName(), scenarioIdentifier.getScenarioName());
	}
	
	public BuildIdentifier getBuildIdentifier() {
		return buildIdentifier;
	}
	
	public String getUsecaseName() {
		return usecaseName;
	}
	
	public String getScenarioName() {
		return scenarioName;
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
