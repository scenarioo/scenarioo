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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Comparison results are persisted in a xml file.
 */
public class ScenarioComparator extends AbstractStructureComparator<Scenario, String, ScenarioSummary> {

	private static final Logger LOGGER = Logger.getLogger(ScenarioComparator.class);

	private StepComparator stepComparator = new StepComparator(parameters);
	private String baseUseCaseName;

	private AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());

	public ScenarioComparator(ComparisonParameters parameters) {
		super(parameters);
	}

	public UseCaseDiffInfo compare(final String baseUseCaseName) {
		this.baseUseCaseName = baseUseCaseName;

		final List<Scenario> baseScenarios = docuReader.loadScenarios(parameters.getBaseBranchName(), parameters.getBaseBuildName(), baseUseCaseName);
		final List<Scenario> comparisonScenarios = docuReader.loadScenarios(
			parameters.getComparisonConfiguration().getComparisonBranchName(),
			parameters.getComparisonConfiguration().getComparisonBuildName(), baseUseCaseName);

		final UseCaseDiffInfo useCaseDiffInfo = new UseCaseDiffInfo(baseUseCaseName);

		calculateDiffInfo(baseScenarios, comparisonScenarios, useCaseDiffInfo);

		LOGGER.info(getLogMessage(useCaseDiffInfo,
				"Use Case " + parameters.getBaseBranchName() + "/" + parameters.getBaseBuildName() + "/" + baseUseCaseName));

		return useCaseDiffInfo;
	}

	@Override
	protected double compareElementAndWrite(final Scenario baseElement, final Scenario comparisonElement,
											final StructureDiffInfo<String, ScenarioSummary> diffInfo) {
		if (comparisonElement == null) {
			return 0;
		} else {
			final ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(baseUseCaseName,
					baseElement.getName());

			parameters.getDiffWriter().saveScenarioDiffInfo(scenarioDiffInfo, baseUseCaseName);

			if (scenarioDiffInfo.hasChanges()) {
				diffInfo.setChanged(diffInfo.getChanged() + 1);
			}
			return scenarioDiffInfo.getChangeRate();
		}
	}

	private List<ScenarioSummary> getScenarioSummaries(final List<Scenario> scenarios) {
		final List<ScenarioSummary> scenarioSummaries = new LinkedList<ScenarioSummary>();

		if (scenarios.isEmpty()) {
			return scenarioSummaries;
		}

		final BuildIdentifier comparisonBuildIdentifier = new BuildIdentifier(
			parameters.getComparisonConfiguration().getComparisonBranchName(),
			parameters.getComparisonConfiguration().getComparisonBuildName());
		final UseCaseScenarios useCaseScenarios = aggregatedDataReader.loadUseCaseScenarios(comparisonBuildIdentifier,
				baseUseCaseName);

		for (final Scenario scenario : scenarios) {
			for (final ScenarioSummary scenarioSummary : useCaseScenarios.getScenarios()) {
				if (scenario.getName().equals(scenarioSummary.getScenario().getName())) {
					scenarioSummaries.add(scenarioSummary);
				}
			}
		}

		return scenarioSummaries;
	}

	@Override
	protected String getElementIdentifier(final Scenario element) {
		return element.getName();
	}

	@Override
	protected String getAddedElementValue(final Scenario element) {
		return element.getName();
	}

	@Override
	protected List<ScenarioSummary> getRemovedElementValues(final List<Scenario> removedElements) {
		return getScenarioSummaries(removedElements);
	}
}
