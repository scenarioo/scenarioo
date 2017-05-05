package org.scenarioo.rest.scenario.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioStatistics;
import org.scenarioo.model.docu.entities.ImportFeature;
import org.scenarioo.model.docu.entities.Scenario;

/**
 * Information used for displaying the overview page of a scenario.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioDetails {

	private Scenario scenario;
	private ScenarioStatistics scenarioStatistics;
	private ImportFeature feature;
	private List<PageWithSteps> pagesAndSteps;

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}

	public ScenarioStatistics getScenarioStatistics() {
		return scenarioStatistics;
	}

	public void setScenarioStatistics(final ScenarioStatistics scenarioStatistics) {
		this.scenarioStatistics = scenarioStatistics;
	}

	public ImportFeature getFeature() {
		return feature;
	}

	public void setFeature(final ImportFeature feature) {
		this.feature = feature;
	}

	public List<PageWithSteps> getPagesAndSteps() {
		return pagesAndSteps;
	}

	public void setPagesAndSteps(final List<PageWithSteps> pages) {
		this.pagesAndSteps = pages;
	}

}
