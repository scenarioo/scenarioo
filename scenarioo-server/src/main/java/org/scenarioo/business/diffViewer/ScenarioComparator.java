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

package org.scenarioo.business.diffViewer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.entities.Scenario;

/**
 * Comparator to compare scenarios of two use cases. Results are persisted in a xml file.
 */
public class ScenarioComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(ScenarioComparator.class);

	private StepComparator stepComparator = new StepComparator(baseBranchName, baseBuildName, comparisonName);

	public ScenarioComparator(String baseBranchName, String baseBuildName, String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
	}

	/**
	 * Compares the scenarios of the given use case.
	 * 
	 * @param baseUseCaseName
	 *            the use case to compare the scenarios.
	 * @return {@link StructureDiffInfo} with the summarized diff information.
	 */
	public UseCaseDiffInfo compare(final String baseUseCaseName) {
		ComparisonAlias comparisonAlias = resolveComparisonName(comparisonName);

		List<Scenario> baseScenarios = docuReader.loadScenarios(baseBranchName, baseBuildName, baseUseCaseName);
		List<Scenario> comparisonScenarios = docuReader.loadScenarios(comparisonAlias.getComparisonBranchName(),
				comparisonAlias.getComparisonBuildName(), baseUseCaseName);

		UseCaseDiffInfo useCaseDiffInfo = new UseCaseDiffInfo(baseUseCaseName);
		double scenarioChangeRateSum = 0;

		for (Scenario baseScenario : baseScenarios) {
			if (StringUtils.isEmpty(baseScenario.getName())) {
				throw new RuntimeException("Found empty name for scenario.");
			}

			Scenario comparisonScenario = getScenarioByName(comparisonScenarios, baseScenario.getName());
			if (comparisonScenario == null) {
				LOGGER.debug("Found new scenario called [" + baseScenario.getName() + "] in base branch ["
						+ baseBranchName + "] and base build [" + baseBuildName + "] and base use case ["
						+ baseUseCaseName + "]");
				useCaseDiffInfo.setAdded(useCaseDiffInfo.getAdded() + 1);
				useCaseDiffInfo.getAddedElements().add(baseScenario.getName());
			} else {
				comparisonScenarios.remove(comparisonScenario);

				ScenarioDiffInfo scenarioDiffInfo = stepComparator.compare(baseUseCaseName, baseScenario.getName());

				diffWriter.saveScenarioDiffInfo(scenarioDiffInfo, baseUseCaseName);

				if (scenarioDiffInfo.hasChanges()) {
					useCaseDiffInfo.setChanged(useCaseDiffInfo.getChanged() + 1);
					scenarioChangeRateSum += scenarioDiffInfo.getChangeRate();
				}
			}
		}
		LOGGER.debug(comparisonScenarios.size() + " scenarios were removed in base branch ["
				+ baseBranchName + "] and base build [" + baseBuildName + "] and base use case ["
				+ baseUseCaseName + "]");
		useCaseDiffInfo.setRemoved(comparisonScenarios.size());
		useCaseDiffInfo.getRemovedElements().addAll(comparisonScenarios);
		useCaseDiffInfo.setChangeRate(calculateChangeRate(baseScenarios.size(), useCaseDiffInfo.getAdded(),
				useCaseDiffInfo.getRemoved(), scenarioChangeRateSum));

		return useCaseDiffInfo;
	}

	private Scenario getScenarioByName(final List<Scenario> scenarios, final String scenarioName) {
		for (Scenario scenario : scenarios) {
			if (scenarioName.equals(scenario.getName())) {
				return scenario;
			}
		}
		return null;
	}

}
