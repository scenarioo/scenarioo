package org.scenarioo.model.docu.aggregates.steps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains informations about current steps positions inside its scenarioo as well as about navigation in all page
 * variants of same page.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepNavigation {
	
	private int pageIndex = 0;
	private int pageOccurenceIndex = 0;
	private int pageStepIndex = 0;
	
	private int pageVariantIndex = 0;
	private int pageVariantScenarioIndex = 0;
	private StepLink previousStepVariant;
	private StepLink nextStepVariant;
	private StepLink previousStepVariantInOtherScenario;
	private StepLink nextStepVariantInOtherScenario;
	
	private int pageVariantCount = 0;
	private int pageVariantScenarioCount = 0;
	
	/**
	 * index of step's page in current scenario
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setPageIndex(final int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	/**
	 * number of occurence of this step's page in current scenario.
	 */
	public int getPageOccurenceIndex() {
		return pageOccurenceIndex;
	}
	
	public void setPageOccurenceIndex(final int pageOccurenceIndex) {
		this.pageOccurenceIndex = pageOccurenceIndex;
	}
	
	/**
	 * index of step inside current page occurence.
	 */
	public int getPageStepIndex() {
		return pageStepIndex;
	}
	
	public void setPageStepIndex(final int pageStepIndex) {
		this.pageStepIndex = pageStepIndex;
	}
	
	/**
	 * index of this step inside all page variants of same page
	 */
	public int getPageVariantIndex() {
		return pageVariantIndex;
	}
	
	public void setPageVariantIndex(final int pageVariantIndex) {
		this.pageVariantIndex = pageVariantIndex;
	}
	
	/**
	 * index of this scenario inside all scenarios where this same page occurs
	 */
	public int getPageVariantScenarioIndex() {
		return pageVariantScenarioIndex;
	}
	
	public void setPageVariantScenarioIndex(final int pageVariantScenarioIndex) {
		this.pageVariantScenarioIndex = pageVariantScenarioIndex;
	}
	
	/**
	 * next step of all variants of same page (possible in other scenario).
	 */
	public StepLink getPreviousStepVariant() {
		return previousStepVariant;
	}
	
	public void setPreviousStepVariant(final StepLink previousStepVariant) {
		this.previousStepVariant = previousStepVariant;
	}
	
	/**
	 * previous step of all variants of same page (possible in other scenario).
	 */
	public StepLink getNextStepVariant() {
		return nextStepVariant;
	}
	
	public void setNextStepVariant(final StepLink nextStepVariant) {
		this.nextStepVariant = nextStepVariant;
	}
	
	/**
	 * count of all steps of the same page
	 */
	public int getPageVariantCount() {
		return pageVariantCount;
	}
	
	public void setPageVariantCount(final int pageVariantCount) {
		this.pageVariantCount = pageVariantCount;
	}
	
	/**
	 * previous step inside all page variant steps in other scenario (previous scenario)
	 */
	public StepLink getPreviousStepVariantInOtherScenario() {
		return previousStepVariantInOtherScenario;
	}
	
	public void setPreviousStepVariantInOtherScenario(final StepLink previousStepVariantInOtherScenario) {
		this.previousStepVariantInOtherScenario = previousStepVariantInOtherScenario;
	}
	
	/**
	 * next step inside all page variant steps in other scenario (previous scenario)
	 */
	public StepLink getNextStepVariantInOtherScenario() {
		return nextStepVariantInOtherScenario;
	}
	
	public void setNextStepVariantInOtherScenario(final StepLink nextStepVariantInOtherScenario) {
		this.nextStepVariantInOtherScenario = nextStepVariantInOtherScenario;
	}
	
	/**
	 * number of scenarios in which this current page occurs.
	 */
	public int getPageVariantScenarioCount() {
		return pageVariantScenarioCount;
	}
	
	public void setPageVariantScenarioCount(final int pageVariantScenarioCount) {
		this.pageVariantScenarioCount = pageVariantScenarioCount;
	}
	
}
