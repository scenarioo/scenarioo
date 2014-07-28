package org.scenarioo.model;

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

	final String branchName;

	final String buildName;
	final String usecaseName;
	final String scenarioName;
	final String pageName;
	final int pageOccurrence;
	final int stepInPageOccurrence;

	public StepIdentifier(final String branchName, final String buildName,
			final String usecaseName, final String scenarioName,
			final String pageName, final int pageOccurrence,
			final int stepInPageOccurrence) {
		super();
		this.branchName = branchName;
		this.buildName = buildName;
		this.usecaseName = usecaseName;
		this.scenarioName = scenarioName;
		this.pageName = pageName;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getBuildName() {
		return buildName;
	}

	public String getUsecaseName() {
		return usecaseName;
	}

	public String getScenarioName() {
		return scenarioName;
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
		return new ToStringBuilder(this).append(branchName).append(buildName)
				.append(usecaseName).append(scenarioName).append(pageName)
				.append(pageOccurrence).append(stepInPageOccurrence).build();
	}

}
