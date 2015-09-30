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

package org.scenarioo.rest.design.scenarioSketch.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.StepSketch;

/**
 * Represents a proposal of an issue with all its steps.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioSketchSteps {

	private ScenarioSketch scenarioSketch;
	private Issue issue;

	@XmlElementWrapper(name = "SketchSteps")
	@XmlElement(name = "SketchSteps")
	private List<StepSketch> sketchSteps;

	public ScenarioSketch getScenarioSketch() {
		return scenarioSketch;
	}

	public void setScenarioSketch(final ScenarioSketch scenarioSketch) {
		this.scenarioSketch = scenarioSketch;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(final Issue issue) {
		this.issue = issue;
	}

	public List<StepSketch> getSketchSteps() {
		return sketchSteps;
	}

	public void setSketchSteps(final List<StepSketch> steps) {
		this.sketchSteps = steps;
	}

}
