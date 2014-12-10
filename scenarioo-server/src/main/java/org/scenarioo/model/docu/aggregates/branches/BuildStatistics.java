package org.scenarioo.model.docu.aggregates.branches;

public class BuildStatistics {

	private int numberOfFailedScenarios;
	private int numberOfSuccessfulScenarios;
	private int numberOfSuccessfulUseCases;
	private int numberOfFailedUseCases;

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


	public int getNumberOfSuccessfulUseCases() {
		return numberOfSuccessfulUseCases;
	}


	public void setNumberOfSuccessfulUseCases(final int numberOfUseCases) {
		this.numberOfSuccessfulUseCases = numberOfUseCases;
	}

	public int getNumberOfFailedUsecases() {
		return numberOfFailedUseCases;
	}

	public void setNumberOfFailedUseCases(final int numberOfFailedUseCases) {
		this.numberOfFailedUseCases = numberOfFailedUseCases;
	}

	public void incrementSuccessfulScenario() {
		numberOfSuccessfulScenarios++;
	}

	public void incrementFailedScenario() {
		numberOfFailedScenarios++;
	}

	public void incrementSuccessfulUseCase() {
		numberOfSuccessfulUseCases++;
	}

	public void incrementFailedUseCase() {
		numberOfFailedUseCases++;
	}


}
