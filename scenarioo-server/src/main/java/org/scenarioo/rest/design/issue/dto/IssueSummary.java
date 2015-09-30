/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.rest.design.issue.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Labels;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueSummary {

	// TODO #185 Can this be removed? Seems we don't use it
	private String status;

	private String name;
	private String id;
	private String description;
	private String author;
	
	// TODO #185 Create component with all context information
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private int numberOfScenarioSketches;
	
	private long dateCreated;
	private long dateModified;
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
