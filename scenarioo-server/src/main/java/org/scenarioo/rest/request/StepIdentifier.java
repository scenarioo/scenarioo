package org.scenarioo.rest.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains all the properties needed to identify a step unambiguously.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepIdentifier {
	
	private final ScenarioIdentifier scenarioIdentifier;
	final String pageName;
	final int pageOccurrence;
	final int stepInPageOccurrence;
	
	public StepIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName,
			final String pageName, final int pageOccurrence, final int stepInPageOccurrence) {
		super();
		
		this.scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName);
		this.pageName = pageName;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
	}
	
	public ScenarioIdentifier getScenarioIdentifier() {
		return scenarioIdentifier;
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(scenarioIdentifier).append(pageName).append(pageOccurrence)
				.append(stepInPageOccurrence).build();
	}
	
}
