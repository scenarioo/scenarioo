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

/**
 * Information to store and display for one design sketchStep.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepSketch implements Serializable {

	// private Page page;
	private int sketchStepName;
	private String sketchFileName; // todo
	private String sketch;
	private int nextSketchStepRef; // (index) todo
	private String usecaseContextName;
	private String usecaseContextLink;
	private String scenarioContextName;
	private String scenarioContextLink;
	private String stepContextLink;
	private String contextInDocu; // todo
	private long dateCreated; // todo
	private long dateModified; // todo

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

	/*
	 * public void setPage(final Page page) {
	 * this.page = page;
	 * }
	 */

	public String getSketch() {
		return sketch;
	}

	public void setSketch(final String sketch) {
		this.sketch = sketch;
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

	public void update(final StepSketch update) {
		this.sketchStepName = update.getSketchStepName() != 0 ? update.getSketchStepName() : this.sketchStepName;
		this.sketchFileName = update.getSketchFileName() != null ? update.getSketchFileName() : this.sketchFileName;
		this.nextSketchStepRef = update.getNextSketchStepRef() != 0 ? update.getNextSketchStepRef()
				: this.nextSketchStepRef;
		this.dateModified = update.getDateModified() != 0 ? update.getDateModified() : this.dateModified;
		this.stepContextLink = update.getStepContextLink() != null ? update.getStepContextLink() : this.stepContextLink;
		this.scenarioContextLink = update.getScenarioContextLink() != null ? update.getScenarioContextLink()
				: this.scenarioContextLink;
		this.scenarioContextName = update.getScenarioContextName() != null ? update.getScenarioContextName()
				: this.scenarioContextName;
		this.usecaseContextLink = update.getUsecaseContextLink() != null ? update.getUsecaseContextLink()
				: this.usecaseContextLink;
		this.usecaseContextName = update.getUsecaseContextName() != null ? update.getUsecaseContextName()
				: this.usecaseContextName;
		this.issueId = update.getIssueId() != null ? update.getIssueId() : this.issueId;
		this.scenarioSketchId = update.getScenarioSketchId() != null ? update.getScenarioSketchId()
				: this.scenarioSketchId;
	}

}
