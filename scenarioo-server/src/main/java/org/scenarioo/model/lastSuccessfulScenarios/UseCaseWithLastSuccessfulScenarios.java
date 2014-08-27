package org.scenarioo.model.lastSuccessfulScenarios;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UseCaseWithLastSuccessfulScenarios {
	
	private final Map<String, LastSuccessfulScenario> scenarios = new HashMap<String, LastSuccessfulScenario>();
	
	public LastSuccessfulScenario getScenarioCreateIfNotNull(final String scenarioName) {
		LastSuccessfulScenario scenario = scenarios.get(scenarioName);
		if (scenario == null) {
			scenario = new LastSuccessfulScenario();
			scenarios.put(scenarioName, scenario);
		}
		return scenario;
	}
	
	public LastSuccessfulScenario getScenario(final String scenarioName) {
		return scenarios.get(scenarioName);
	}
	
}
