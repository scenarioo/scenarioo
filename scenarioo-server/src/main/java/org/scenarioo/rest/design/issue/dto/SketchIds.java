package org.scenarioo.rest.design.issue.dto;

public class SketchIds {

	private String scenarioSketchId;
	private String stepSketchId;

	public String getScenarioSketchId() {
		return scenarioSketchId;
	}

	public void setScenarioSketchId(final String scenarioSketchId) {
		this.scenarioSketchId = scenarioSketchId;
	}

	public String getStepSketchId() {
		return stepSketchId;
	}

	public void setStepSketchId(final String stepSketchId) {
		this.stepSketchId = stepSketchId;
	}

	public static SketchIds fromIssueWithSketch(final IssueWithSketch issueWitchSketch) {
		SketchIds sketchIds = new SketchIds();
		sketchIds.setScenarioSketchId(issueWitchSketch.getScenarioSketch().getScenarioSketchId());
		sketchIds.setStepSketchId(issueWitchSketch.getStepSketch().getStepSketchId());
		return sketchIds;
	}

}
