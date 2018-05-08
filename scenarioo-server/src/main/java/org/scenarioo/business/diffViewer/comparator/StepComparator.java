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

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.aggregator.StepsAndPagesAggregator;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.StepInfo;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.utils.NumberFormatter;

import java.util.LinkedList;
import java.util.List;

public class StepComparator extends AbstractStructureComparator<StepLink, Integer, StepInfo> {

	private static final Logger LOGGER = Logger.getLogger(StepComparator.class);

	private String baseUseCaseName;
	private String baseScenarioName;
	private List<Step> comparisonSteps;
	private StepsAndPagesAggregator stepAndPagesAggregator = new StepsAndPagesAggregator(null, null);
	private ScreenshotComparator screenshotComparator;

	public StepComparator(ComparisonParameters parameters, ScreenshotComparator screenshotComparator) {
		super(parameters);
		this.screenshotComparator = screenshotComparator;
	}

	public ScenarioDiffInfo compare(final String baseUseCaseName, final String baseScenarioName) {
		this.baseUseCaseName = baseUseCaseName;
		this.baseScenarioName = baseScenarioName;

		final List<Step> baseSteps = loadSteps(parameters.getBaseBranchName(), parameters.getBaseBuildName());
		this.comparisonSteps = loadSteps(parameters.getComparisonConfiguration().getComparisonBranchName(),
			parameters.getComparisonConfiguration().getComparisonBuildName());

		final List<StepLink> baseStepLinks = stepAndPagesAggregator.calculateStepLinks(baseSteps, baseUseCaseName,
				baseScenarioName);
		final List<StepLink> comparisonStepLinks = stepAndPagesAggregator.calculateStepLinks(comparisonSteps,
				baseUseCaseName, baseScenarioName);

		final ScenarioDiffInfo scenarioDiffInfo = new ScenarioDiffInfo(baseScenarioName);

		calculateDiffInfo(baseStepLinks, comparisonStepLinks, scenarioDiffInfo);

		LOGGER.info(getLogMessage(scenarioDiffInfo,
				"Scenario " + parameters.getBaseBranchName() + "/" + parameters.getBaseBuildName() + "/" + baseUseCaseName + "/" + baseScenarioName));

		return scenarioDiffInfo;
	}

	@Override
	protected double compareElementAndWrite(final StepLink baseElement, final StepLink comparisonElement,
											final StructureDiffInfo<Integer, StepInfo> diffInfo) {
		if (comparisonElement == null) {
			return 0;
		} else {
			final String comparisonScreenshotName =
				NumberFormatter.formatMinimumThreeDigits(comparisonElement.getStepIndex()) + ".png";

			final double changeRate = screenshotComparator.compare(parameters, baseUseCaseName, baseScenarioName,
					baseElement, comparisonScreenshotName);

			final StepDiffInfo stepDiffInfo = getStepDiffInfo(baseElement, comparisonScreenshotName, changeRate);

			parameters.getDiffWriter().saveStepDiffInfo(baseUseCaseName, baseScenarioName, stepDiffInfo);

			if (stepDiffInfo.hasChanges()) {
				diffInfo.setChanged(diffInfo.getChanged() + 1);
			}
			return stepDiffInfo.getChangeRate();
		}
	}

	private List<StepInfo> getStepInfos(final List<StepLink> stepLinks) {
		final List<StepInfo> stepInfos = new LinkedList<StepInfo>();
		for (final StepLink stepLink : stepLinks) {
			final StepInfo stepInfo = new StepInfo();
			stepInfo.setStepLink(stepLink);
			stepInfo.setStepDescription(getStepDescription(stepLink));
			stepInfos.add(stepInfo);
		}
		return stepInfos;
	}

	private StepDescription getStepDescription(final StepLink stepLink) {
		for (final Step step : comparisonSteps) {
			if (step.getStepDescription() != null && stepLink.getStepIndex() == step.getStepDescription().getIndex()) {
				return step.getStepDescription();
			}
		}
		return null;
	}

	private StepDiffInfo getStepDiffInfo(final StepLink baseStepLink, final String comparisonScreenshotName,
			final double changeRate) {
		final StepDiffInfo stepDiffInfo = new StepDiffInfo();
		stepDiffInfo.setChangeRate(changeRate);
		stepDiffInfo.setIndex(baseStepLink.getStepIndex());
		stepDiffInfo.setPageName(baseStepLink.getPageName());
		stepDiffInfo.setPageOccurrence(baseStepLink.getPageOccurrence());
		stepDiffInfo.setStepInPageOccurrence(baseStepLink.getStepInPageOccurrence());
		stepDiffInfo.setComparisonScreenshotName(comparisonScreenshotName);
		return stepDiffInfo;
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

	private List<Step> loadSteps(final String branchName, final String buildName) {
		try {
			return scenarioDocuReader.loadSteps(branchName, buildName, baseUseCaseName, baseScenarioName);
		} catch (final ResourceNotFoundException e) {
			return new LinkedList<Step>();
		}
	}

	@Override
	protected String getElementIdentifier(final StepLink element) {
		return getStepIdentifier(element);
	}

	@Override
	protected Integer getAddedElementValue(final StepLink element) {
		return element.getStepIndex();
	}

	@Override
	protected List<StepInfo> getRemovedElementValues(final List<StepLink> removedElements) {
		return getStepInfos(removedElements);
	}
}
