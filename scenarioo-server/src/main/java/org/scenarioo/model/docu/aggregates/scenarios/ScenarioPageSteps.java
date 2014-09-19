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

package org.scenarioo.model.docu.aggregates.scenarios;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.UseCase;

/**
 * Represents a scenario of a usecase with all its pages and steps.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioPageSteps {
	
	private Scenario scenario;
	
	private UseCase useCase;
	
	@XmlElementWrapper(name = "pagesAndSteps")
	@XmlElement(name = "pageSteps")
	private List<PageSteps> pagesAndSteps;
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}
	
	public ScenarioStatistics getScenarioStatistics() {
		ScenarioStatistics scenarioStatistics = new ScenarioStatistics();
		scenarioStatistics.setNumberOfPages(getTotalNumberOfPagesInScenario());
		scenarioStatistics.setNumberOfSteps(getTotalNumberOfStepsInScenario());
		return scenarioStatistics;
	}
	
	public UseCase getUseCase() {
		return useCase;
	}
	
	public void setUseCase(final UseCase useCase) {
		this.useCase = useCase;
	}
	
	public List<PageSteps> getPagesAndSteps() {
		return pagesAndSteps;
	}
	
	public void setPagesAndSteps(final List<PageSteps> pagesAndSteps) {
		this.pagesAndSteps = pagesAndSteps;
	}
	
	public int getTotalNumberOfStepsInScenario() {
		if (pagesAndSteps == null) {
			return 0;
		}
		
		int sum = 0;
		
		for (PageSteps pageWithSteps : pagesAndSteps) {
			sum += pageWithSteps.getSteps().size();
		}
		
		return sum;
	}
	
	public StepStatistics getStepStatistics(final String pageName, final int pageOccurrence) {
		StepStatistics statistics = new StepStatistics();
		statistics.setTotalNumberOfStepsInScenario(getTotalNumberOfStepsInScenario());
		statistics
				.setTotalNumberOfStepsInPageOccurrence(getTotalNumberOfStepsInPageOccurrence(pageName, pageOccurrence));
		statistics.setTotalNumberOfPagesInScenario(getTotalNumberOfPagesInScenario());
		return statistics;
	}
	
	public int getTotalNumberOfStepsInPageOccurrence(final String pageName, final int pageOccurrence) {
		PageSteps pageWithSteps = getOccurrence(pageName, pageOccurrence);
		
		if (pageWithSteps == null) {
			return 0;
		}
		
		return pageWithSteps.getSteps().size();
	}
	
	private PageSteps getOccurrence(final String pageName, final int pageOccurrence) {
		int currentOccurrence = 0;
		for (PageSteps pageWithSteps : pagesAndSteps) {
			if (pageWithSteps.getPage().getName().equals(pageName)) {
				if (currentOccurrence == pageOccurrence) {
					return pageWithSteps;
				}
				currentOccurrence++;
			}
		}
		return null;
	}
	
	private int getTotalNumberOfPagesInScenario() {
		return pagesAndSteps.size();
	}
	
	public StepDescription getStepDescription(final String pageName, final int pageOccurrence, final int stepInPageOccurrence) {
		PageSteps pageWithSteps = getOccurrence(pageName, pageOccurrence);
		return pageWithSteps.getSteps().get(stepInPageOccurrence);
	}
	
}
