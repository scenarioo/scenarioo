package org.scenarioo.model.docu.aggregates.steps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains informations about current steps positions inside its scenario as
 * well as about navigation in all page variants of same page.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepNavigation {

	private String pageName;
	private int pageIndex = -1;
	private int stepIndex = -1;
	private int pageOccurrence = -1;
	private int stepInPageOccurrence = -1;

	private int pageVariantIndex = -1;
	private int pageVariantScenarioIndex = -1;
	private StepLink previousStepVariant;
	private StepLink nextStepVariant;
	private StepLink previousStepVariantInOtherScenario;
	private StepLink nextStepVariantInOtherScenario;

	private int pageVariantsCount = -1;
	private int pageVariantScenariosCount = -1;

	private NeighborStep previousStep;
	private NeighborStep nextStep;
	private NeighborStep previousPage;
	private NeighborStep nextPage;

	public String getPageName() {
		return pageName;
	}

	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}

	/**
	 * index of step's page in current scenario
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(final int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(final int stepIndex) {
		this.stepIndex = stepIndex;
	}

	/**
	 * Occurrence index of this page in the scenario. An occurrence of a page is
	 * a sequence of steps that have the same page and it ends as soon as there
	 * is a step with a different page.
	 */
	public int getPageOccurrenceIndex() {
		return pageOccurrence;
	}

	public int getPageOccurrence() {
		return pageOccurrence;
	}

	public void setPageOccurrence(final int pageOccurrence) {
		this.pageOccurrence = pageOccurrence;
	}

	/**
	 * Index of step inside current page occurrence.
	 */
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}

	public void setStepInPageOccurrence(final int stepInPageOccurrence) {
		this.stepInPageOccurrence = stepInPageOccurrence;
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
	public int getPageVariantsCount() {
		return pageVariantsCount;
	}

	public void setPageVariantsCount(final int pageVariantCount) {
		this.pageVariantsCount = pageVariantCount;
	}

	/**
	 * number of scenarios in which this current page occurs.
	 */
	public int getPageVariantScenariosCount() {
		return pageVariantScenariosCount;
	}

	public void setPageVariantScenariosCount(final int pageVariantScenarioCount) {
		this.pageVariantScenariosCount = pageVariantScenarioCount;
	}

	/**
	 * previous step inside all page variant steps in other scenario (previous
	 * scenario)
	 */
	public StepLink getPreviousStepVariantInOtherScenario() {
		return previousStepVariantInOtherScenario;
	}

	public void setPreviousStepVariantInOtherScenario(
			final StepLink previousStepVariantInOtherScenario) {
		this.previousStepVariantInOtherScenario = previousStepVariantInOtherScenario;
	}

	/**
	 * next step inside all page variant steps in other scenario (previous
	 * scenario)
	 */
	public StepLink getNextStepVariantInOtherScenario() {
		return nextStepVariantInOtherScenario;
	}

	public void setNextStepVariantInOtherScenario(
			final StepLink nextStepVariantInOtherScenario) {
		this.nextStepVariantInOtherScenario = nextStepVariantInOtherScenario;
	}

	public NeighborStep getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(final NeighborStep previousStep) {
		this.previousStep = previousStep;
	}

	public NeighborStep getNextStep() {
		return nextStep;
	}

	public void setNextStep(final NeighborStep nextStep) {
		this.nextStep = nextStep;
	}

	public NeighborStep getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(final NeighborStep previousPage) {
		this.previousPage = previousPage;
	}

	public NeighborStep getNextPage() {
		return nextPage;
	}

	public void setNextPage(final NeighborStep nextPage) {
		this.nextPage = nextPage;
	}

}
