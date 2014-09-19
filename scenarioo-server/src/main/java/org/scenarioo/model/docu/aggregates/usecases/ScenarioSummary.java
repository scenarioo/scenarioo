package org.scenarioo.model.docu.aggregates.usecases;

import org.scenarioo.model.docu.entities.Scenario;

/**
 * An imported Scenario in the scenario list of a use case.
 */
public class ScenarioSummary {
	
	private Scenario scenario;
	private int numberOfSteps;
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}
	
	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	
	public void setNumberOfSteps(final int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}
	
}
