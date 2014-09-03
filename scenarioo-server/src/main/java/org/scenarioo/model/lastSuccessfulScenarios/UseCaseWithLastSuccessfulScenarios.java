package org.scenarioo.model.lastSuccessfulScenarios;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public void removeScenario(final String scenarioName) {
		scenarios.remove(scenarioName);
	}
	
	public Date getLatestBuildDateOfAllScenarios() {
		Date latestBuildDate = null;
		for (Entry<String, LastSuccessfulScenario> scenario : scenarios.entrySet()) {
			if (isBuildDateNewer(scenario.getValue().getBuildDate(), latestBuildDate)) {
				latestBuildDate = scenario.getValue().getBuildDate();
			}
		}
		return latestBuildDate;
	}
	
	private boolean isBuildDateNewer(final Date buildDate, final Date latestBuildDate) {
		if (buildDate == null) {
			return false;
		}
		
		if (latestBuildDate == null) {
			return true;
		}
		
		return buildDate.after(latestBuildDate);
	}
	
}
