/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.business.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.steps.NeighborStep;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Handles calculation of all page steps for each scenario and collection of additional needed information for step
 * navigation per step and especially for page variants (all steps of one page over all scenarios).
 */
public class StepsAndPagesAggregator {
	
	private final Map<String, PageVariantNavigationData> pageVariants = new HashMap<String, PageVariantNavigationData>();
	
	private final BuildIdentifier build;
	
	private final ScenarioDocuAggregationDao dao;
	
	public StepsAndPagesAggregator(final BuildIdentifier build, final ScenarioDocuAggregationDao dao) {
		this.build = build;
		this.dao = dao;
	}
	
	public List<PageSteps> calculateScenarioPageSteps(final UseCase usecase,
			final Scenario scenario, final List<Step> steps, final List<ObjectReference> referencePath,
			final ObjectRepository objectRepository) {
		
		List<PageSteps> pageStepsList = new ArrayList<PageSteps>();
		List<StepLink> stepLinks = new ArrayList<StepLink>(steps.size());
		Map<String, Integer> pageOccurrences = new HashMap<String, Integer>();
		Page page = null;
		PageSteps pageSteps = null;
		int pageIndex = 0;
		int index = 0;
		int pageOccurrence = 0;
		int stepInPageOccurrence = 0;
		
		for (Step step : steps) {
			
			// Introduce a special dummy page for all steps not having any page to avoid problems.
			if (step.getPage() == null || StringUtils.isBlank(step.getPage().getName())) {
				step.setPage(new Page());
				step.getPage().setName("unknownPage");
			}
			
			// Check for new page and update indexes and occurrence accordingly
			if (isNewPage(page, step)) {
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				pageStepsList.add(pageSteps);
				stepInPageOccurrence = 0;
				pageOccurrence = increasePageOccurrence(pageOccurrences, page);
				if (index > 0) {
					pageIndex++;
				}
			}
			
			// add step to belonging page steps
			StepDescription stepDescription = step.getStepDescription();
			pageSteps.getSteps().add(stepDescription);
			
			StepLink stepLink = new StepLink(usecase.getName(),
					scenario.getName(), index, pageIndex, getPageName(page),
					pageOccurrence, stepInPageOccurrence);
			stepLinks.add(stepLink);
			
			objectRepository.addPageAndStep(referencePath, step, stepLink);
			
			index++;
			stepInPageOccurrence++;
		}
		
		calculateNavigationAndPageVariantsData(stepLinks);
		
		return pageStepsList;
		
	}
	
	private void calculateNavigationAndPageVariantsData(final List<StepLink> stepLinks) {
		if (stepLinks.size() == 0) {
			return;
		}

		NeighborStep firstStep = new NeighborStep(stepLinks.get(0));
		NeighborStep lastStep = new NeighborStep(stepLinks.get(stepLinks.size() - 1));

		for (int i = 0; i < stepLinks.size(); i++) {
			calculateNavigationAndPageVariantData(
					firstStep, getPreviousPage(stepLinks, i),
					getPreviousStep(stepLinks, i), stepLinks.get(i),
					getNextStep(stepLinks, i), getNextPage(stepLinks, i), lastStep);
		}
	}
	
	private NeighborStep getPreviousPage(final List<StepLink> stepLinks,
			final int currentStepIndex) {
		String currentPageName = stepLinks.get(currentStepIndex).getPageName();
		for (int i = currentStepIndex - 1; i >= 0; i--) {
			if (!stepLinks.get(i).getPageName().equals(currentPageName)) {
				return new NeighborStep(stepLinks.get(i));
			}
		}
		return null;
	}
	
	private NeighborStep getNextPage(final List<StepLink> stepLinks,
			final int currentStepIndex) {
		String currentPageName = stepLinks.get(currentStepIndex).getPageName();
		for (int i = currentStepIndex + 1; i < stepLinks.size(); i++) {
			if (!stepLinks.get(i).getPageName().equals(currentPageName)) {
				return new NeighborStep(stepLinks.get(i));
			}
		}
		return null;
	}
	
	private NeighborStep getPreviousStep(final List<StepLink> stepLinks,
			final int i) {
		if (i > 0) {
			return new NeighborStep(stepLinks.get(i - 1));
		} else {
			return null;
		}
	}
	
	private NeighborStep getNextStep(final List<StepLink> stepLinks, final int i) {
		if (i < stepLinks.size() - 1) {
			return new NeighborStep(stepLinks.get(i + 1));
		} else {
			return null;
		}
	}
	
	private boolean isNewPage(final Page page, final Step step) {
		return page == null || step.getPage() == null
				|| !page.equals(step.getPage());
	}
	
	/**
	 * Calculate the number of times this same page has already occurred and increase the occurrence in the passed
	 * pageOccurrences accordingly.
	 */
	private int increasePageOccurrence(
			final Map<String, Integer> pageOccurrences, final Page page) {
		String pageKey = getPageName(page);
		Integer occurrences = pageOccurrences.get(pageKey);
		if (occurrences == null) {
			occurrences = new Integer(0);
			pageOccurrences.put(pageKey, occurrences);
		} else {
			occurrences = occurrences + 1;
			pageOccurrences.put(pageKey, occurrences);
		}
		return occurrences.intValue();
	}
	
	private String getPageName(final Page page) {
		return (page == null) ? null : page.getName();
	}
	
	/**
	 * Calculate and save the step and page variant navigation data for current step.
	 * 
	 * The written data in this method is not yet complete, because the next step for the current page variant will only
	 * be processed later. See {@link #saveAggregatedPageVariantDataInStepNavigations()} for completing this step
	 * navigation data.
	 */
	private void calculateNavigationAndPageVariantData(
			final NeighborStep firstStep,
			final NeighborStep previousPage, final NeighborStep previousStep,
			final StepLink currentStep, final NeighborStep nextStep,
			final NeighborStep nextPage, final NeighborStep lastStep) {
		
		PageVariantNavigationData pageVariant = getPageVariantNavigationData(currentStep);
		
		// Calculate page variant data:
		StepLink previousPageVariantStep = null;
		StepLink previousPageVariantStepOtherSceanrio = null;
		if (pageVariant != null) {
			pageVariant.increaseStepsCount();
			previousPageVariantStep = pageVariant.getLastStep();
			if (previousPageVariantStep == null) {
				// first scenario, count it:
				pageVariant.increaseScenariosCount();
			} else if (!isSameScenario(currentStep, previousPageVariantStep)) {
				// different scenario than the last page variant:
				pageVariant.increaseScenariosCount();
				pageVariant
						.setLastStepFromDifferentScenario(previousPageVariantStep);
			}
			previousPageVariantStepOtherSceanrio = pageVariant
					.getLastStepFromDifferentScenario();
			pageVariant.setLastStep(currentStep);
		}
		
		// Create step navigation for current step (not yet complete, because
		// next page variant step is only processed
		// and added later)
		StepNavigation stepNavigation = new StepNavigation();
		stepNavigation.setPageName(currentStep.getPageName());
		stepNavigation.setPageIndex(currentStep.getPageIndex());
		stepNavigation.setStepIndex(currentStep.getStepIndex());
		stepNavigation.setPageOccurrence(currentStep.getPageOccurrence());
		stepNavigation.setStepInPageOccurrence(currentStep.getStepInPageOccurrence());
		stepNavigation.setFirstStep(firstStep);
		stepNavigation.setPreviousPage(previousPage);
		stepNavigation.setPreviousStep(previousStep);
		stepNavigation.setNextStep(nextStep);
		stepNavigation.setNextPage(nextPage);
		stepNavigation.setLastStep(lastStep);
		if (pageVariant != null) {
			stepNavigation.setPreviousStepVariant(previousPageVariantStep);
			stepNavigation
					.setPreviousStepVariantInOtherScenario(previousPageVariantStepOtherSceanrio);
			stepNavigation.setPageVariantIndex(pageVariant.getStepsCount() - 1);
			stepNavigation.setPageVariantScenarioIndex(pageVariant
					.getScenariosCount() - 1);
			// next page variant steps will be saved later, when all steps and
			// scenarios have been processed.
		}
		
		// save navigation data
		dao.saveStepNavigation(build, currentStep, stepNavigation);
		
	}
	
	/**
	 * Get or create a page variant navigation data for current step's page
	 */
	private PageVariantNavigationData getPageVariantNavigationData(
			final StepLink stepLink) {
		String pageName = stepLink.getPageName();
		if (pageName == null) {
			return null;
		}
		PageVariantNavigationData pageVariant = pageVariants.get(stepLink
				.getPageName());
		if (pageVariant == null) {
			pageVariant = new PageVariantNavigationData();
			pageVariants.put(pageName, pageVariant);
		}
		return pageVariant;
	}
	
	private boolean isSameScenario(final StepLink step1, final StepLink step2) {
		return step1.getUseCaseName().equals(step2.getUseCaseName())
				&& step1.getScenarioName().equals(step2.getScenarioName());
	}
	
	/**
	 * Write data that has been collected into all step's navigation files.
	 * <ul>
	 * <li>count of scenarios for same page</li>
	 * <li>count of steps for same page over all scenarios</li>
	 * <li>next step with same page</li>
	 * </ul>
	 */
	public void completeAggregatedPageVariantDataInStepNavigations() {
		
		for (Entry<String, PageVariantNavigationData> pageVariant : pageVariants
				.entrySet()) {
			
			PageVariantNavigationData pageVariantNavigation = pageVariant
					.getValue();
			
			// Process all step navigations for this page by passing backwards
			// through it and complete the navigation
			// data.
			StepLink lastStep = pageVariantNavigation.getLastStep();
			StepLink nextStepVariant = null;
			StepLink nextStepVariantInOtherScenario = null;
			while (lastStep != null) {
				
				if (nextStepVariant != null
						&& !isSameScenario(nextStepVariant, lastStep)) {
					nextStepVariantInOtherScenario = nextStepVariant;
				}
				
				StepNavigation stepNavigation = dao.loadStepNavigation(build,
						lastStep);
				stepNavigation.setPageVariantsCount(pageVariantNavigation
						.getStepsCount());
				stepNavigation
						.setPageVariantScenariosCount(pageVariantNavigation
								.getScenariosCount());
				stepNavigation.setNextStepVariant(nextStepVariant);
				stepNavigation
						.setNextStepVariantInOtherScenario(nextStepVariantInOtherScenario);
				dao.saveStepNavigation(build, lastStep, stepNavigation);
				
				nextStepVariant = lastStep;
				lastStep = stepNavigation.getPreviousStepVariant();
			}
			
		}
		
	}
	
}
