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
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private int numberOfScenarioSketches;
	private long dateCreated, dateModified;
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

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateModified() {
		return dateModified;
	}

	public void setDateModified(final long dateModified) {
		this.dateModified = dateModified;
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

	public String getUsecaseContextName() {
		return usecaseContextName;
	}

	public void setUsecaseContextName(final String usecaseContextName) {
		this.usecaseContextName = usecaseContextName;
	}

	public String getUsecaseContextLink() {
		return usecaseContextLink;
	}

	public void setUsecaseContextLink(final String usecaseContextLink) {
		this.usecaseContextLink = usecaseContextLink;
	}

	public String getScenarioContextName() {
		return scenarioContextName;
	}

	public void setScenarioContextName(final String scenarioContextName) {
		this.scenarioContextName = scenarioContextName;
	}

	public String getScenarioContextLink() {
		return scenarioContextLink;
	}

	public void setScenarioContextLink(final String scenarioContextLink) {
		this.scenarioContextLink = scenarioContextLink;
	}

}
