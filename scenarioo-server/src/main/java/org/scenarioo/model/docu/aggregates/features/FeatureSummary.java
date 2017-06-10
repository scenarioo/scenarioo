package org.scenarioo.model.docu.aggregates.features;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a feature and some important aggregated data to display in use
 * cases list.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureSummary extends Feature {
	private int numberOfScenarios;

	public List<ScenarioSummary> scenarios = new ArrayList<>();

	public FeatureSummary(){}

	public FeatureSummary(Feature feature) {
		super(feature);
	}

	public int getNumberOfScenarios() {
		return numberOfScenarios;
	}

	public void setNumberOfScenarios(final int numberOfScenarios) {
		this.numberOfScenarios = numberOfScenarios;
	}


}
