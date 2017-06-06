package org.scenarioo.model.docu.aggregates.features;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.ImportFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a feature and some important aggregated data to display in use
 * cases list.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureSummary extends ImportFeature{
	private int numberOfScenarios;

	public List<ScenarioSummary> scenarios = new ArrayList<>();

	public FeatureSummary(){}

	public FeatureSummary(ImportFeature importFeature) {
		super(importFeature);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getNumberOfScenarios() {
		return numberOfScenarios;
	}

	public void setNumberOfScenarios(final int numberOfScenarios) {
		this.numberOfScenarios = numberOfScenarios;
	}


}
