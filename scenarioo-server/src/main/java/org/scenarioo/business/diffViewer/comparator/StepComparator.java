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

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.aggregator.StepsAndPagesAggregator;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.StepInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.utils.NumberFormatCreator;

/**
 * Comparator to compare steps of two scenarios. Results are persisted in a xml file.
 */
public class StepComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(StepComparator.class);
	private static final String SCREENSHOT_FILE_EXTENSION = ".png";
	private static NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
			.createNumberFormatWithMinimumIntegerDigits(3);

	private ScreenshotComparator screenshotComparator = new ScreenshotComparator(baseBranchName, baseBuildName,
			comparisonConfiguration);
	private StepsAndPagesAggregator stepAndPagesAggregator = new StepsAndPagesAggregator(null, null);

	public StepComparator(final String baseBranchName, final String baseBuildName,
			final ComparisonConfiguration comparisonConfiguration) {
		super(baseBranchName, baseBuildName, comparisonConfiguration);
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
		final List<Step> baseSteps = loadSteps(baseBranchName, baseBuildName, baseUseCaseName, baseScenarioName);
		final List<Step> comparisonSteps = loadSteps(comparisonConfiguration.getComparisonBranchName(),
				comparisonConfiguration.getComparisonBuildName(), baseUseCaseName, baseScenarioName);

		final List<StepLink> baseStepLinks = stepAndPagesAggregator.calculateStepLinks(baseSteps, baseUseCaseName,
				baseScenarioName);
		final List<StepLink> comparisonStepLinks = stepAndPagesAggregator.calculateStepLinks(comparisonSteps,
				baseUseCaseName, baseScenarioName);

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
				final String comparisonScreenshotName = THREE_DIGIT_NUM_FORMAT
						.format(comparisonStepLink.getStepIndex())
						+ SCREENSHOT_FILE_EXTENSION;

				final double changeRate = screenshotComparator.compare(baseUseCaseName, baseScenarioName,
						baseStepLink, comparisonScreenshotName);

				final StepDiffInfo stepDiffInfo = new StepDiffInfo();
				stepDiffInfo.setChangeRate(changeRate);
				stepDiffInfo.setIndex(baseStepLink.getStepIndex());
				stepDiffInfo.setPageName(baseStepLink.getPageName());
				stepDiffInfo.setPageOccurrence(baseStepLink.getPageOccurrence());
				stepDiffInfo.setStepInPageOccurrence(baseStepLink.getStepInPageOccurrence());
				stepDiffInfo.setComparisonScreenshotName(comparisonScreenshotName);

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

	private List<Step> loadSteps(final String branchName, final String buildName, final String baseUseCaseName,
			final String baseScenarioName) {
		try {
			return docuReader.loadSteps(branchName, buildName, baseUseCaseName, baseScenarioName);
		} catch (final ResourceNotFoundException e) {
			return new LinkedList<Step>();
		}
	}

}
