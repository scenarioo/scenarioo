package org.scenarioo.model.lastSuccessfulScenarios;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Keeps track of which scenario comes from which build by storing the build date for each scenario.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LastSuccessfulScenariosIndex {
	
	Map<String, UseCaseWithLastSuccessfulScenarios> useCases = new HashMap<String, UseCaseWithLastSuccessfulScenarios>();
	
	public UseCaseWithLastSuccessfulScenarios getUseCaseCreateIfNotNull(final String useCaseName) {
		UseCaseWithLastSuccessfulScenarios useCase = useCases.get(useCaseName);
		if (useCase == null) {
			useCase = new UseCaseWithLastSuccessfulScenarios();
			useCases.put(useCaseName, useCase);
		}
		return useCase;
	}
	
	public UseCaseWithLastSuccessfulScenarios getUseCase(final String useCaseName) {
		return useCases.get(useCaseName);
	}
	
	public void setScenarioBuildDate(final String useCaseName, final String scenarioName, final Date time) {
		getScenario(useCaseName, scenarioName).setBuildDate(time);
	}
	
	public Date getScenarioBuildDate(final String useCaseName, final String scenarioName) {
		UseCaseWithLastSuccessfulScenarios useCase = getUseCase(useCaseName);
		if (useCase == null) {
			return null;
		}
		
		LastSuccessfulScenario scenario = useCase.getScenario(scenarioName);
		if (scenario == null) {
			return null;
		}
		
		return scenario.getBuildDate();
	}
	
	private LastSuccessfulScenario getScenario(final String useCaseName, final String scenarioName) {
		return getUseCaseCreateIfNotNull(useCaseName).getScenarioCreateIfNotNull(scenarioName);
	}
	
}
