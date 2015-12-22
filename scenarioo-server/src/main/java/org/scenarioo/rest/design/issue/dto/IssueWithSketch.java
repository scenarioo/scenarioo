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

import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.StepSketch;

/**
 * An issue with its scenario sketch and the issue sketch. Later we might change this so that an issue
 * can have multiple scenario sketches and a scenario sketch can have multiple step sketches.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueWithSketch {

	private Issue issue;
	private ScenarioSketch scenarioSketch;
	private StepSketch stepSketch;

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(final Issue issue) {
		this.issue = issue;
	}

	public ScenarioSketch getScenarioSketch() {
		return scenarioSketch;
	}

	public void setScenarioSketch(final ScenarioSketch scenarioSketch) {
		this.scenarioSketch = scenarioSketch;
	}

	public StepSketch getStepSketch() {
		return stepSketch;
	}

	public void setStepSketch(final StepSketch stepSketch) {
		this.stepSketch = stepSketch;
	}

}