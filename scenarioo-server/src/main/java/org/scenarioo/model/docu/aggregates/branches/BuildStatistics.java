package org.scenarioo.model.docu.aggregates.branches;

public class BuildStatistics {

	private int numberOfFailedScenarios;
	private int numberOfSuccessfulScenarios;
	private int numberOfUseCases;

	public int getNumberOfFailedScenarios() {
		return numberOfFailedScenarios;
	}

	public void setNumberOfFailedScenarios(final int numberOfFailedScenarios) {
		this.numberOfFailedScenarios = numberOfFailedScenarios;
	}

	public int getNumberOfSuccessfulScenarios() {
		return numberOfSuccessfulScenarios;
	}

	public void setNumberOfSuccessfulScenarios(final int numberOfSuccessfulScenarios) {
		this.numberOfSuccessfulScenarios = numberOfSuccessfulScenarios;
	}


	public int getNumberOfUseCases() {
		return numberOfUseCases;
	}


	public void setNumberOfUseCases(final int numberOfUseCases) {
		this.numberOfUseCases = numberOfUseCases;
	}

	public void incrementSuccessfulScenario() {
		numberOfSuccessfulScenarios++;
	}

	public void incrementFailedScenario() {
		numberOfFailedScenarios++;
	}

	public void incrementUseCase() {
		numberOfUseCases++;
	}
}
