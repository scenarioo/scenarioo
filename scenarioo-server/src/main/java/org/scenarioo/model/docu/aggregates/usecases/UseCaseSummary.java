package org.scenarioo.model.docu.aggregates.usecases;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Labels;

/**
 * Represents a use case and some important aggregated data to display in use
 * cases list.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UseCaseSummary {
	private String status;
	private String name;
	private String description;
	private int numberOfScenarios;
	private Labels labels;
	
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

	public Labels getLabels() {
		return labels;
	}

	public void setLabels(Labels labels) {
		this.labels = labels;
	}

}
