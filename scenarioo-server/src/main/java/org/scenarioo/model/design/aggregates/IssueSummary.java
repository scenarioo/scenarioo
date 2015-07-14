package org.scenarioo.model.design.aggregates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Labels;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueSummary {
	private String status;
	private String name;
	private String id;
	private String description;
	private String author;
	private int numberOfScenarioSketches;
	private String firstScenarioSketchId;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public int getNumberOfScenarioSketches() {
		return numberOfScenarioSketches;
	}

	public void setNumberOfScenarioSketches(final int numberOfScenarioSketches) {
		this.numberOfScenarioSketches = numberOfScenarioSketches;
	}

	public String getFirstScenarioSketchId() {
		return firstScenarioSketchId;
	}

	public void setFirstScenarioSketchId(final String firstScenarioSketchId) {
		this.firstScenarioSketchId = firstScenarioSketchId;
	}

	public Labels getLabels() {
		return labels;
	}

	public void setLabels(final Labels labels) {
		this.labels = labels;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
