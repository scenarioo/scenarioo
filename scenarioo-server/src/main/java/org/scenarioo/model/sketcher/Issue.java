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

package org.scenarioo.model.sketcher;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.scenarioo.rest.base.StepIdentifier;

/**
 * An issue is the top level element of the design domain. It can be referenced by
 * zero, one or several ScenarioSketches.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Issue implements Serializable {

	private String name;
	private String issueId;
	private String description;

	private String trackingURL;
	private String author;

	private StepIdentifier relatedStep;

	private Date dateCreated;
	private Date dateModified;

	@XmlTransient
	private String branchName;

	public Issue() {
	}

	public Issue(final String name, final String description) {
		this();
		this.name = name;
		this.description = description;
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(final Date dateModified) {
		this.dateModified = dateModified;
	}

	public void setBranchName(final String branchName) {
		this.branchName = branchName;
	}

	public String getBranchName() {
		return branchName;
	}

	public StepIdentifier getRelatedStep() {
		return relatedStep;
	}

	public void setRelatedStep(final StepIdentifier relatedStep) {
		this.relatedStep = relatedStep;
	}

}
