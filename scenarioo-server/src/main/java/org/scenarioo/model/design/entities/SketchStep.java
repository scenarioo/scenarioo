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
	private SketchStepMetadata metadata = new SketchStepMetadata();
	private String contextInDocu; // todo
	private String dateCreated; // todo
	private String dateModified; // todo

	@XmlTransient
	private String branchName;
	@XmlTransient
	private String issueName;
	@XmlTransient
	private String proposalName;

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

	public SketchStepMetadata getMetadata() {
		return metadata;
	}

	/**
	 * (optional) Additional metadata that will only be displayed on the sketchStep details page. Especially put huge
	 * additional detail data about a sketchStep into this object.
	 */
	public void setMetadata(final SketchStepMetadata metadata) {
		this.metadata = metadata;
	}

	public int getSketchStepName() {
		return sketchStepName;
	}

	public void setSketchStepName(final int sketchStepName) {
		this.sketchStepName = sketchStepName;
	}

	private String getBranchName() {
		return branchName;
	}

	private void setBranchName(final String branchName) {
		this.branchName = branchName;
	}

	private String getIssueName() {
		return issueName;
	}

	private void setIssueName(final String issueName) {
		this.issueName = issueName;
	}

	private String getProposalName() {
		return proposalName;
	}

	private void setProposalName(final String proposalName) {
		this.proposalName = proposalName;
	}

}
