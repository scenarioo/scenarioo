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

package org.scenarioo.business.diffViewer.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.StepInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Comparator to compare steps of two scenarios. Results are persisted in a xml file.
 */
public class StepComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(StepComparator.class);

	private ScreenshotComparator screenshotComparator = new ScreenshotComparator(baseBranchName, baseBuildName,
			comparisonName);

	public StepComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
	}

	/**
	 * Compares the steps of the given scenario.
	 * 
	 * @param baseUseCaseName
	 *            the use case of the steps.
	 * @param baseScenarioName
	 *            the scenario to compare the steps.
	 * @return {@link ScenarioDiffInfo} with the summarized diff information.
	 */
	public ScenarioDiffInfo compare(final String baseUseCaseName, final String baseScenarioName) {
		final BuildIdentifier comparisonBuildIdentifier = getComparisonBuildIdentifier(comparisonName);

		final List<Step> baseSteps = docuReader.loadSteps(baseBranchName, baseBuildName, baseUseCaseName,
				baseScenarioName);
		final List<Step> comparisonSteps = docuReader.loadSteps(comparisonBuildIdentifier.getBranchName(),
				comparisonBuildIdentifier.getBuildName(), baseUseCaseName, baseScenarioName);

		final List<StepLink> baseStepLinks = getStepLinks(baseSteps, baseUseCaseName, baseScenarioName);
		final List<StepLink> comparisonStepLinks = getStepLinks(comparisonSteps, baseUseCaseName, baseScenarioName);

		final ScenarioDiffInfo scenarioDiffInfo = new ScenarioDiffInfo(baseScenarioName);
		double stepChangeRateSum = 0;

		for (final StepLink baseStepLink : baseStepLinks) {
			final StepLink comparisonStepLink = getStepLinkByIdentifier(comparisonStepLinks,
					getStepIdentifier(baseStepLink));
			if (comparisonStepLink == null) {
				LOGGER.debug("Found new step called [" + getStepIdentifier(baseStepLink) + "] in base branch ["
						+ baseBranchName + "] and base build [" + baseBuildName + "] and base use case ["
						+ baseUseCaseName + "] and base scenario [" + baseScenarioName + "]");
				scenarioDiffInfo.setAdded(scenarioDiffInfo.getAdded() + 1);
				scenarioDiffInfo.getAddedElements().add(baseStepLink.getStepIndex());
			} else {
				comparisonStepLinks.remove(comparisonStepLink);

				final double changeRate = screenshotComparator.compare(baseUseCaseName, baseScenarioName,
						baseStepLink, comparisonStepLink);

				final StepDiffInfo stepDiffInfo = new StepDiffInfo();
				stepDiffInfo.setChangeRate(changeRate);
				stepDiffInfo.setIndex(baseStepLink.getStepIndex());
				stepDiffInfo.setPageName(baseStepLink.getPageName());
				stepDiffInfo.setPageOccurrence(baseStepLink.getPageOccurrence());
				stepDiffInfo.setStepInPageOccurrence(baseStepLink.getStepInPageOccurrence());

				diffWriter.saveStepDiffInfo(baseUseCaseName, baseScenarioName, stepDiffInfo);

				if (stepDiffInfo.hasChanges()) {
					scenarioDiffInfo.setChanged(scenarioDiffInfo.getChanged() + 1);
					stepChangeRateSum += stepDiffInfo.getChangeRate();
				}
			}
		}
		LOGGER.debug(comparisonStepLinks.size() + " steps were removed in base branch ["
				+ baseBranchName + "] and base build [" + baseBuildName + "] and base use case ["
				+ baseUseCaseName + "] and base scenario [" + baseScenarioName + "]");
		scenarioDiffInfo.setRemoved(comparisonStepLinks.size());
		scenarioDiffInfo.getRemovedElements().addAll(getStepInfos(comparisonStepLinks, comparisonSteps));
		scenarioDiffInfo.setChangeRate(calculateChangeRate(baseSteps.size(), scenarioDiffInfo.getAdded(),
				scenarioDiffInfo.getRemoved(), stepChangeRateSum));

		return scenarioDiffInfo;
	}

	private List<StepInfo> getStepInfos(final List<StepLink> stepLinks, final List<Step> steps) {
		final List<StepInfo> stepInfos = new LinkedList<StepInfo>();
		for (final StepLink stepLink : stepLinks) {
			final StepInfo stepInfo = new StepInfo();
			stepInfo.setStepLink(stepLink);
			stepInfo.setStepDescription(getStepDescription(stepLink, steps));
			stepInfos.add(stepInfo);
		}
		return stepInfos;
	}

	private StepDescription getStepDescription(final StepLink stepLink, final List<Step> steps) {
		for (final Step step : steps) {
			if (step.getStepDescription() != null && stepLink.getStepIndex() == step.getStepDescription().getIndex()) {
				return step.getStepDescription();
			}
		}
		return null;
	}
	private List<StepLink> getStepLinks(final List<Step> steps, final String useCaseName, final String scenarioName) {
		// TODO pforster: refactor this method in a new class. also used in StepsAndPagesAggregator. Oder Wert einfach
		// abspeichern beim aggregieren.
		final List<StepLink> stepLinks = new ArrayList<StepLink>(steps.size());
		final Map<String, Integer> pageOccurrences = new HashMap<String, Integer>();
		Page page = null;
		int pageIndex = 0;
		int index = 0;
		int pageOccurrence = 0;
		int stepInPageOccurrence = 0;

		for (final Step step : steps) {

			// Introduce a special dummy page for all steps not having any page to avoid problems.
			if (step.getPage() == null || StringUtils.isBlank(step.getPage().getName())) {
				step.setPage(new Page());
				step.getPage().setName("unknownPage");
			}

			// Check for new page and update indexes and occurrence accordingly
			if (isNewPage(page, step)) {
				page = step.getPage();
				stepInPageOccurrence = 0;
				pageOccurrence = increasePageOccurrence(pageOccurrences, page);
				if (index > 0) {
					pageIndex++;
				}
			}

			final StepLink stepLink = new StepLink(useCaseName,
					scenarioName, index, pageIndex, getPageName(page),
					pageOccurrence, stepInPageOccurrence);
			stepLinks.add(stepLink);

			index++;
			stepInPageOccurrence++;
		}
		return stepLinks;
	}

	private boolean isNewPage(final Page page, final Step step) {
		// TODO pforster: refactor this method in a new class. also used in StepsAndPagesAggregator. Oder Wert einfach
		// abspeichern beim aggregieren.
		return page == null || step.getPage() == null
				|| !page.equals(step.getPage());
	}

	/**
	 * Calculate the number of times this same page has already occurred and increase the occurrence in the passed
	 * pageOccurrences accordingly.
	 */
	private int increasePageOccurrence(
			final Map<String, Integer> pageOccurrences, final Page page) {
		// TODO pforster: refactor this method in a new class. also used in StepsAndPagesAggregator. Oder Wert einfach
		// abspeichern beim aggregieren.
		final String pageKey = getPageName(page);
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

	private String getStepIdentifier(final StepLink stepLink) {
		final StringBuilder stepIdentifier = new StringBuilder();
		stepIdentifier.append(stepLink.getPageName() == null ? "" : stepLink.getPageName());
		stepIdentifier.append("_");
		stepIdentifier.append(stepLink.getPageOccurrence());
		stepIdentifier.append("_");
		stepIdentifier.append(stepLink.getStepInPageOccurrence());

		return stepIdentifier.toString();
	}

	private StepLink getStepLinkByIdentifier(final List<StepLink> stepLinks, final String stepIdentifier) {
		for (final StepLink stepLink : stepLinks) {
			if (stepIdentifier.equals(getStepIdentifier(stepLink))) {
				return stepLink;
			}
		}
		return null;
	}

}
