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

import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.UseCase;

/**
 * Handles calculation of all page steps for each scenario and collection of additional needed information for step
 * navigation per step and especially for page variants (all steps of one page over all scenarios).
 */
public class StepsAndPagesAggregator {
	
	private final Map<String, PageVariantNavigationData> pageVariants = new HashMap<String, PageVariantNavigationData>();
	
	private final BuildIdentifier build;
	
	private final ScenarioDocuAggregationDAO dao;
	
	public StepsAndPagesAggregator(final BuildIdentifier build, final ScenarioDocuAggregationDAO dao) {
		this.build = build;
		this.dao = dao;
	}
	
	public List<PageSteps> calculateScenarioPageSteps(final UseCase usecase,
			final Scenario scenario, final List<Step> steps) {
		
		List<PageSteps> pageStepsList = new ArrayList<PageSteps>();
		Map<String, Integer> pageOccurences = new HashMap<String, Integer>();
		Page page = null;
		PageSteps pageSteps = null;
		int pageIndex = 0;
		int pageStepIndex = 0;
		int index = 0;
		int pageOccurenceIndex = 0;
		for (Step step : steps) {
			
			// Check for new page and update indexes and occurence accordingly
			boolean isNewPage = page == null || step.getPage() == null
					|| !page.equals(step.getPage());
			if (isNewPage) {
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				pageStepsList.add(pageSteps);
				pageStepIndex = 0;
				pageOccurenceIndex = increasePageOccurence(pageOccurences, page);
				if (index > 0) {
					pageIndex++;
				}
			}
			
			// add step to belonging page steps
			StepDescription stepDescription = step.getStepDescription();
			pageSteps.getSteps().add(stepDescription);
			
			// Calculate page navigation data for this step (including page variant navigation)
			
			StepLink stepLink = new StepLink(usecase.getName(), scenario.getName(), index,
					pageIndex, getPageName(page), pageOccurenceIndex, pageStepIndex);
			calculateNavigationAndPageVariantData(stepLink);
			
			index++;
			pageStepIndex++;
			
		}
		return pageStepsList;
		
	}
	
	/**
	 * Calculate the number of times this same page has already occured and increase the occurence in the passed
	 * pageOccurences accordingly.
	 */
	private int increasePageOccurence(final Map<String, Integer> pageOccurences, final Page page) {
		String pageKey = getPageName(page);
		Integer occurences = pageOccurences.get(pageKey);
		if (occurences == null) {
			occurences = new Integer(0);
			pageOccurences.put(pageKey, occurences);
		}
		else {
			occurences = occurences + 1;
			pageOccurences.put(pageKey, occurences);
		}
		return occurences.intValue();
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
	private void calculateNavigationAndPageVariantData(final StepLink stepLink) {
		
		PageVariantNavigationData pageVariant = getPageVariantNavigationData(stepLink);
		
		// Calculate page variant data:
		StepLink previousPageVariantStep = null;
		StepLink previousPageVariantStepOtherSceanrio = null;
		if (pageVariant != null) {
			pageVariant.increaseStepsCount();
			previousPageVariantStep = pageVariant.getLastStep();
			if (previousPageVariantStep == null) {
				// first scenario, count it:
				pageVariant.increaseScenariosCount();
			}
			else if (!isSameScenario(stepLink, previousPageVariantStep)) {
				// different scenario than the last page variant:
				pageVariant.increaseScenariosCount();
				pageVariant.setLastStepFromDifferentScenario(previousPageVariantStep);
			}
			previousPageVariantStepOtherSceanrio = pageVariant.getLastStepFromDifferentScenario();
			pageVariant.setLastStep(stepLink);
		}
		
		// Create step navigation for current step (not yet complete, because next page variant step is only processed
		// and added later)
		StepNavigation stepNavigation = new StepNavigation();
		stepNavigation.setPageIndex(stepLink.getPageIndex());
		stepNavigation.setPageOccurenceIndex(stepLink.getPageOccurenceIndex());
		stepNavigation.setPageStepIndex(stepLink.getPageStepIndex());
		if (pageVariant != null) {
			stepNavigation.setPreviousStepVariant(previousPageVariantStep);
			stepNavigation.setPreviousStepVariantInOtherScenario(previousPageVariantStepOtherSceanrio);
			stepNavigation.setPageVariantIndex(pageVariant.getStepsCount() - 1);
			stepNavigation.setPageVariantScenarioIndex(pageVariant.getScenariosCount() - 1);
			// next page variant steps will be saved later, when all steps and scenarios have been processed.
		}
		
		// save navigation data
		dao.saveStepNavigation(build, stepLink, stepNavigation);
		
	}
	
	/**
	 * Get or create a page variant navigation data for current step's page
	 */
	private PageVariantNavigationData getPageVariantNavigationData(final StepLink stepLink) {
		String pageName = stepLink.getPageName();
		if (pageName == null) {
			return null;
		}
		PageVariantNavigationData pageVariant = pageVariants.get(stepLink.getPageName());
		if (pageVariant == null) {
			pageVariant = new PageVariantNavigationData();
			pageVariants.put(pageName, pageVariant);
		}
		return pageVariant;
	}
	
	private boolean isSameScenario(final StepLink step1,
			final StepLink step2) {
		return step1.getUseCaseName().equals(
				step2.getUseCaseName())
				&& step1.getScenarioName().equals(
						step2.getScenarioName());
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
			
			PageVariantNavigationData pageVariantNavigation = pageVariant.getValue();
			
			// Process all step navigations for this page by passing backwards through it and complete the navigation
			// data.
			StepLink lastStep = pageVariantNavigation.getLastStep();
			StepLink nextStepVariant = null;
			StepLink nextStepVariantInOtherScenario = null;
			while (lastStep != null) {
				
				if (nextStepVariant != null && !isSameScenario(nextStepVariant, lastStep)) {
					nextStepVariantInOtherScenario = nextStepVariant;
				}
				
				StepNavigation stepNavigation = dao.loadStepNavigation(build, lastStep);
				stepNavigation.setPageVariantCount(pageVariantNavigation.getStepsCount());
				stepNavigation.setPageVariantScenarioCount(pageVariantNavigation.getScenariosCount());
				stepNavigation.setNextStepVariant(nextStepVariant);
				stepNavigation.setNextStepVariantInOtherScenario(nextStepVariantInOtherScenario);
				dao.saveStepNavigation(build, lastStep, stepNavigation);
				
				nextStepVariant = lastStep;
				lastStep = stepNavigation.getPreviousStepVariant();
			}
			
		}
		
	}
	
}
