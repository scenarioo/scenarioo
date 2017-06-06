package org.scenarioo.model.docu.aggregates.branches;

public class BuildStatistics {

	private int numberOfFailedScenarios = 0;
	private int numberOfSuccessfulScenarios = 0;
	private int numberOfSuccessfulFeatures = 0;
	private int numberOfFailedFeatures = 0;

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


	public int getNumberOfSuccessfulFeatures() {
		return numberOfSuccessfulFeatures;
	}


	public void setNumberOfSuccessfulFeatures(final int numberOfFeatures) {
		this.numberOfSuccessfulFeatures = numberOfFeatures;
	}

	public int getNumberOfFailedFeatures() {
		return numberOfFailedFeatures;
	}

	public void setNumberOfFailedFeatures(final int numberOfFailedFeatures) {
		this.numberOfFailedFeatures = numberOfFailedFeatures;
	}

	public void incrementSuccessfulScenario() {
		numberOfSuccessfulScenarios++;
	}

	public void incrementFailedScenario() {
		numberOfFailedScenarios++;
	}

	public void incrementSuccessfulFeature() {
		numberOfSuccessfulFeatures++;
	}

	public void incrementFailedFeature() {
		numberOfFailedFeatures++;
	}


}
