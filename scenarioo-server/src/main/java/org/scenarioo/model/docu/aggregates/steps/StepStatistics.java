package org.scenarioo.model.docu.aggregates.steps;

public class StepStatistics {

	private int totalNumberOfStepsInScenario = -1;
	private int totalNumberOfStepsInPageOccurrence = -1;
	private int totalNumberOfPagesInScenario = -1;

	public int getTotalNumberOfStepsInScenario() {
		return totalNumberOfStepsInScenario;
	}

	public void setTotalNumberOfStepsInScenario(
			final int totalNumberOfStepsInScenario) {
		this.totalNumberOfStepsInScenario = totalNumberOfStepsInScenario;
	}

	public int getTotalNumberOfStepsInPageOccurrence() {
		return totalNumberOfStepsInPageOccurrence;
	}

	public void setTotalNumberOfStepsInPageOccurrence(
			final int totalNumberOfStepsInPageOccurrence) {
		this.totalNumberOfStepsInPageOccurrence = totalNumberOfStepsInPageOccurrence;
	}

	public int getTotalNumberOfPagesInScenario() {
		return totalNumberOfPagesInScenario;
	}

	public void setTotalNumberOfPagesInScenario(
			final int totalNumberOfPagesInScenario) {
		this.totalNumberOfPagesInScenario = totalNumberOfPagesInScenario;
	}

}
