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

package org.scenarioo.rest.design.issue.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.entities.Issue;

/**
 * Represents an issue with all its proposals
 */
// TODO #185 Rename this class
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueScenarioSketches {

	private Issue issue;

	@XmlElementWrapper(name = "scenarioSketches")
	@XmlElement(name = "scenarioSketchSummary")
	private List<ScenarioSketchSummary> scenarioSketches = new ArrayList<ScenarioSketchSummary>();

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(final Issue issue) {
		this.issue = issue;
	}

	public List<ScenarioSketchSummary> getScenarioSketches() {
		return scenarioSketches;
	}

	public void setScenarioSketches(final List<ScenarioSketchSummary> proposals) {
		this.scenarioSketches = proposals;
	}

}