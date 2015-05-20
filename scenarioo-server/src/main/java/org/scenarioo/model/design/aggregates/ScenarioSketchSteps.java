/*
 * scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.design.aggregates;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.ScenarioSketch;
import org.scenarioo.model.design.entities.SketchStep;

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
	private List<SketchStep> sketchSteps;

	public ScenarioSketch getScenarioSketch() {
		return scenarioSketch;
	}

	public void setScenarioSketch(final ScenarioSketch scenarioSketch) {
		this.scenarioSketch = scenarioSketch;
	}

	// public ScenarioStatistics getScenarioStatistics() {
	// ScenarioStatistics scenarioStatistics = new ScenarioStatistics();
	// scenarioStatistics.setNumberOfPages(getTotalNumberOfPagesInScenario());
	// scenarioStatistics.setNumberOfSteps(getTotalNumberOfStepsInScenario());
	// return scenarioStatistics;
	// }

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(final Issue issue) {
		this.issue = issue;
	}

	public List<SketchStep> getSketchSteps() {
		return sketchSteps;
	}

	public void setSketchSteps(final List<SketchStep> steps) {
		this.sketchSteps = steps;
	}

	public int getTotalNumberOfStepsInScenario() {
		if (sketchSteps == null) {
			return 0;
		}

		return sketchSteps.size();
	}

	// public StepStatistics getStepStatistics(final String pageName, final int pageOccurrence) {
	// StepStatistics statistics = new StepStatistics();
	// statistics.setTotalNumberOfStepsInScenario(getTotalNumberOfStepsInScenario());
	// statistics
	// .setTotalNumberOfStepsInPageOccurrence(getTotalNumberOfStepsInPageOccurrence(pageName, pageOccurrence));
	// statistics.setTotalNumberOfPagesInScenario(getTotalNumberOfPagesInScenario());
	// return statistics;
	// }

	// public int getTotalNumberOfStepsInPageOccurrence(final String pageName, final int pageOccurrence) {
	// PageSteps pageWithSteps = getOccurrence(pageName, pageOccurrence);
	//
	// if (pageWithSteps == null) {
	// return 0;
	// }
	//
	// return pageWithSteps.getSteps().size();
	// }

//	private PageSteps getOccurrence(final String pageName, final int pageOccurrence) {
//		int currentOccurrence = 0;
//		for (PageSteps pageWithSteps : pagesAndSteps) {
//			if (pageWithSteps.getPage().getName().equals(pageName)) {
//				if (currentOccurrence == pageOccurrence) {
//					return pageWithSteps;
//				}
//				currentOccurrence++;
//			}
//		}
//		return null;
//	}

//	private int getTotalNumberOfPagesInScenario() {
//		return pagesAndSteps.size();
//	}

//	public StepDescription getStepDescription(final String pageName, final int pageOccurrence,
//			final int stepInPageOccurrence) {
//		PageSteps pageWithSteps = getOccurrence(pageName, pageOccurrence);
//		return pageWithSteps.getSteps().get(stepInPageOccurrence);
	// }

}
