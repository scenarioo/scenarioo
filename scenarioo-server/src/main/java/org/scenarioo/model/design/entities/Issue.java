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

package org.scenarioo.model.design.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.Detailable;
import org.scenarioo.model.docu.entities.Labelable;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.generic.Details;

/**
 * An issue is the top level element of the design domain. It can be referenced by
 * zero, one or several ScenarioSketches.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Issue implements Serializable, Labelable, Detailable {

	private String name;
	private String issueId;
	private String description;

	// TODO #185 Can this be removed? Seems we don't use it
	private String issueStatus;

	private String trackingURL;
	private String author;

	// TODO #185 Create component with all context information
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private String stepContextLink;

	private long dateCreated;
	private long dateModified;

	@XmlTransient
	private String branchName;

	private Details details = new Details();
	private Labels labels = new Labels();

	public Issue() {
	}

	public Issue(final String name, final String description) {
		this();
		this.name = name;
		this.description = description;
		this.issueStatus = "Open";
	}

	public void update(final Issue update) {
		this.name = update.getName() != null ? update.getName() : this.name;
		this.issueId = update.getIssueId() != null ? update.getIssueId() : this.issueId;
		this.description = update.getDescription() != null ? update.getDescription() : this.description;
		this.issueStatus = update.getIssueStatus() != null ? update.getIssueStatus() : this.issueStatus;
		this.trackingURL = update.getTrackingURL() != null ? update.getTrackingURL() : this.trackingURL;
		this.author = update.getAuthor() != null ? update.getAuthor() : this.author;
		this.usecaseContextLink = update.getUsecaseContextLink() != null ? update.getUsecaseContextLink()
				: this.usecaseContextLink;
		this.dateCreated = update.getDateCreated() != 0 ? update.getDateCreated() : this.dateCreated;
		this.dateModified = update.getDateModified() != 0 ? update.getDateModified() : this.dateModified;

		this.details = update.getDetails() != null ? update.getDetails() : this.details;
		this.labels = update.getLabels() != null ? update.getLabels() : this.labels;
	}

	public String getName() {
		return name;
	}

	/**
	 * A unique name for this issue.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(final String id) {
		this.issueId = id;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional but recommended) More detailed description for current scenario (additionally to descriptive name).
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getIssueStatus() {
		return issueStatus;
	}

	/**
	 * Set status of current issue. Scenarioo supports open and closed by default.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	public void setStatus(final IssueStatus status) {
		setIssueStatus(IssueStatus.toKeywordNullSafe(status));
	}

	public void setIssueStatus(final String status) {
		this.issueStatus = status;
	}

	public String getTrackingURL() {
		return trackingURL;
	}

	public void setTrackingURL(final String trackingURL) {
		this.trackingURL = trackingURL;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getUsecaseContextLink() {
		return usecaseContextLink;
	}

	public void setUsecaseContextLink(final String usecaseContextLink) {
		this.usecaseContextLink = usecaseContextLink;
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

	@Override
	public Details getDetails() {
		return details;
	}

	@Override
	public Details addDetail(final String key, final Object value) {
		return details.addDetail(key, value);
	}

	@Override
	public void setDetails(final Details details) {
		Preconditions.checkNotNull(details, "Details not allowed to set to null");
		this.details = details;
	}

	@Override
	public Labels getLabels() {
		return labels;
	}

	@Override
	public Labels addLabel(final String label) {
		return labels.addLabel(label);
	}

	@Override
	public void setLabels(final Labels labels) {
		Preconditions.checkNotNull(labels, "Labels not allowed to set to null");
		this.labels = labels;
	}

	public void setBranchName(final String branchName) {
		this.branchName = branchName;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getScenarioContextLink() {
		return scenarioContextLink;
	}

	public void setScenarioContextLink(final String scenarioContextLink) {
		this.scenarioContextLink = scenarioContextLink;
	}

	public String getStepContextLink() {
		return stepContextLink;
	}

	public void setStepContextLink(final String stepContextLink) {
		this.stepContextLink = stepContextLink;
	}

	public String getUsecaseContextName() {
		return usecaseContextName;
	}

	public void setUsecaseContextName(final String usecaseContextName) {
		this.usecaseContextName = usecaseContextName;
	}

	public String getScenarioContextName() {
		return scenarioContextName;
	}

	public void setScenarioContextName(final String scenarioContextName) {
		this.scenarioContextName = scenarioContextName;
	}

}
