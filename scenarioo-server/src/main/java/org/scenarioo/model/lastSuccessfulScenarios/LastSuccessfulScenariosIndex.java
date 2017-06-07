package org.scenarioo.model.lastSuccessfulScenarios;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Keeps track of which scenario comes from which build by storing the build date for each scenario.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LastSuccessfulScenariosIndex {

	private final Map<String, FeatureWithLastSuccessfulScenarios> features = new HashMap<String, FeatureWithLastSuccessfulScenarios>();
	private Date latestImportedBuildDate;

	public FeatureWithLastSuccessfulScenarios getFeatureCreateIfNotNull(final String featureName) {
		FeatureWithLastSuccessfulScenarios feature = features.get(featureName);
		if (feature == null) {
			feature = new FeatureWithLastSuccessfulScenarios();
			features.put(featureName, feature);
		}
		return feature;
	}

	public FeatureWithLastSuccessfulScenarios getFeature(final String featureName) {
		return features.get(featureName);
	}

	public void setScenarioBuildDate(final String featureName, final String scenarioName, final Date time) {
		getScenarioCreateIfNotNull(featureName, scenarioName).setBuildDate(time);
	}

	public Date getScenarioBuildDate(final String featureName, final String scenarioName) {
		FeatureWithLastSuccessfulScenarios feature = getFeature(featureName);
		if (feature == null) {
			return null;
		}

		LastSuccessfulScenario scenario = feature.getScenario(scenarioName);
		if (scenario == null) {
			return null;
		}

		return scenario.getBuildDate();
	}

	private LastSuccessfulScenario getScenarioCreateIfNotNull(final String featureName, final String scenarioName) {
		return getFeatureCreateIfNotNull(featureName).getScenarioCreateIfNotNull(scenarioName);
	}

	private LastSuccessfulScenario getScenario(final String featureName, final String scenarioName) {
		FeatureWithLastSuccessfulScenarios feature = getFeature(featureName);
		if (feature == null) {
			return null;
		}
		return feature.getScenario(scenarioName);
	}

	public void setLatestImportedBuildDate(final Date latestImportedBuildDate) {
		this.latestImportedBuildDate = latestImportedBuildDate;
	}

	public Date getLatestImportedBuildDate() {
		return latestImportedBuildDate;
	}

	public void removeFeature(final String featureName) {
		features.remove(featureName);
	}

	public void removeScenario(final String featureName, final String scenarioName) {
		FeatureWithLastSuccessfulScenarios featureWithLastSuccessfulScenarios = features.get(featureName);
		if (featureWithLastSuccessfulScenarios != null) {
			featureWithLastSuccessfulScenarios.removeScenario(scenarioName);
		}
	}

	public Date getLatestBuildDateOfFeature(final String featureName) {
		FeatureWithLastSuccessfulScenarios feature = getFeature(featureName);
		if (feature == null) {
			return null;
		}

		return feature.getLatestBuildDateOfAllScenarios();
	}

	public Date getBuildDateForScenario(final String featureName, final String scenarioName) {
		LastSuccessfulScenario scenario = getScenario(featureName, scenarioName);
		if (scenario == null) {
			return null;
		}
		return scenario.getBuildDate();
	}

}
