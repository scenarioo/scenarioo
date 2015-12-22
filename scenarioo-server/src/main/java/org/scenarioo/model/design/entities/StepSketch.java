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
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepSketch implements Serializable {

	private String sketchFileName; // todo
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private String stepContextLink;
	private String contextInDocu; // todo
	private int stepIndex;

	// below: ok
	private String stepSketchId;
	private Date dateCreated;
	private Date dateModified;
	private String svgXmlString;

	@XmlTransient
	private String branchName;
	@XmlTransient
	private String issueName;
	@XmlTransient
	private String issueId;
	@XmlTransient
	private String scenarioSketchId;


	public String getSketchFileName() {
		return sketchFileName;
	}

	public void setSketchFileName(final String sketchFileName) {
		this.sketchFileName = sketchFileName;
	}

	public String getContextInDocu() {
		return contextInDocu;
	}

	public void setContextInDocu(final String contextInDocu) {
		this.contextInDocu = contextInDocu;
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

	// below: ok

	public String getStepSketchId() {
		return stepSketchId;
	}

	public void setStepSketchId(final String stepSketchName) {
		this.stepSketchId = stepSketchName;
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

	public String getSvgXmlString() {
		return svgXmlString;
	}

	public void setSvgXmlString(final String svgXmlString) {
		this.svgXmlString = svgXmlString;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(final int stepIndex) {
		this.stepIndex = stepIndex;
	}

}
