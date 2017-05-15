package org.scenarioo.model.docu.aggregates.usecases;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * An imported Scenario in the scenario list of a use case.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Scenario.class)
public class ScenarioSummary {

	private Scenario scenario;
	private int numberOfSteps;

	public ScenarioPageSteps pageSteps;

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
