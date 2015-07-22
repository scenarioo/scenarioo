/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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

/**
 * Information to store and display for one design sketchStep.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SketchStep implements Serializable {

	// private Page page;
	private int sketchStepName;
	private String sketchFileName; // todo
	private int nextSketchStepRef; // (index) todo
	private SketchStepDescription sketchStepDescription;
	private SketchStepHtml html;
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private String stepContextLink;
	private String contextInDocu; // todo
	private String dateCreated; // todo
	private String dateModified; // todo

	@XmlTransient
	private String branchName;
	@XmlTransient
	private String issueName;
	@XmlTransient
	private String issueId;
	@XmlTransient
	private String scenarioSketchId;

	/*
	 * public Page getPage() {
	 * return page;
	 * }
	 */

	/**
	 * Information about the page this sketchStep belongs to (usually there are several sketchSteps that show the same
	 * UI page).
	 *
	 * This information is optional in case you do not have a page concept in your application.
	 */
	/*
	 * public void setPage(final Page page) {
	 * this.page = page;
	 * }
	 */

	public String getSketch() {
		return sketchFileName;
	}

	public void setSketch(final String sketch) {
		this.sketchFileName = sketch;
	}

	public String getSketchFileName() {
		return sketchFileName;
	}

	public void setSketchFileName(final String sketchFileName) {
		this.sketchFileName = sketchFileName;
	}

	public int getNextSketchStepRef() {
		return nextSketchStepRef;
	}

	public void setNextSketchStepRef(final int nextSketchStepRef) {
		this.nextSketchStepRef = nextSketchStepRef;
	}

	public String getContextInDocu() {
		return contextInDocu;
	}

	public void setContextInDocu(final String contextInDocu) {
		this.contextInDocu = contextInDocu;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(final String dateModified) {
		this.dateModified = dateModified;
	}

	public SketchStepDescription getSketchStepDescription() {
		return sketchStepDescription;
	}

	/**
	 * Most important description information about this sketchStep. Only put the most important values and informations
	 * about
	 * a sketchStep into this object.
	 */
	public void setSketchStepDescription(final SketchStepDescription sketchStepDescription) {
		this.sketchStepDescription = sketchStepDescription;
	}

	public SketchStepHtml getHtml() {
		return html;
	}

	/**
	 * Optional information for webapplications about the html output of current sketchStep.
	 */
	public void setHtml(final SketchStepHtml html) {
		this.html = html;
	}

	public int getSketchStepName() {
		return sketchStepName;
	}

	public void setSketchStepName(final int sketchStepName) {
		this.sketchStepName = sketchStepName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(final String branchName) {
		this.branchName = branchName;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(final String issueName) {
		this.issueName = issueName;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(final String issueId) {
		this.issueId = issueId;
	}

	public String getScenarioSketchId() {
		return scenarioSketchId;
	}

	public void setScenarioSketchId(final String scenarioSketchId) {
		this.scenarioSketchId = scenarioSketchId;
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

	public String getStepContextLink() {
		return stepContextLink;
	}

	public void setStepContextLink(final String stepContextLink) {
		this.stepContextLink = stepContextLink;
	}

}
