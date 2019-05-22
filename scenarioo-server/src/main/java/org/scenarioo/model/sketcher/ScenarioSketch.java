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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A {@link ScenarioSketch} is a sketch of an entire scenario. It always belongs to an {@link Issue}.
 * It currently always contains one {@link StepSketch}. In the future it might also support several {@link StepSketch}es
 * so that it is actually a scenario sketch.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioSketch implements Serializable {

	private String scenarioSketchId;
	private String issueId;
	private String author;
	private Date dateCreated;
	private Date dateModified;

	public String getScenarioSketchId() {
		return scenarioSketchId;
	}

	public void setScenarioSketchId(final String scenarioSketchId) {
		this.scenarioSketchId = scenarioSketchId;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(final String issueId) {
		this.issueId = issueId;
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

}
