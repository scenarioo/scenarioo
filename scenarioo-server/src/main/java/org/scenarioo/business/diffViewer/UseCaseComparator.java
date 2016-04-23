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
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.docu.entities.UseCase;

/**
 * Comparator to compare use cases of two builds. Results are persisted in a xml file.
 */
public class UseCaseComparator extends AbstractComparator {

	private static final Logger LOGGER = Logger.getLogger(UseCaseComparator.class);

	private ScenarioComparator scenarioComparator = new ScenarioComparator(baseBranchName, baseBuildName,
			comparisonName);

	public UseCaseComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
	}

	/**
	 * Compares the uses cases of the build.
	 * 
	 * @return {@link StructureDiffInfo} with the summarized diff information.
	 */
	public StructureDiffInfo compare() {
		ComparisonAlias comparisonAlias = resolveComparisonName(comparisonName);

		List<UseCase> baseUseCases = docuReader.loadUsecases(baseBranchName, baseBuildName);
		List<UseCase> comparisonUseCases = docuReader.loadUsecases(comparisonAlias.getComparisonBranchName(),
				comparisonAlias.getComparisonBuildName());

		StructureDiffInfo buildDiffInfo = new StructureDiffInfo(comparisonName);
		double useCaseChangeRateSum = 0;

		for (UseCase baseUseCase : baseUseCases) {
			if (StringUtils.isEmpty(baseUseCase.getName())) {
				throw new RuntimeException("Found empty name for use case.");
			}

			UseCase comparisonUseCase = getUseCaseByName(comparisonUseCases, baseUseCase.getName());
			if (comparisonUseCase == null) {
				LOGGER.debug("Found new use case called [" + baseUseCase.getName() + "] in base branch ["
						+ baseBranchName + "] and base build [" + baseBuildName + "]");
				buildDiffInfo.setAdded(buildDiffInfo.getAdded() + 1);
			} else {
				comparisonUseCases.remove(comparisonUseCase);

				StructureDiffInfo useCaseDiffInfo = scenarioComparator.compare(baseUseCase.getName());

				diffWriter.saveUseCaseDiffInfo(useCaseDiffInfo);

				if (useCaseDiffInfo.hasChanges()) {
					buildDiffInfo.setChanged(buildDiffInfo.getChanged() + 1);
					useCaseChangeRateSum += useCaseDiffInfo.getChangeRate();
				}
			}
		}
		LOGGER.debug(comparisonUseCases.size() + " use cases were removed in base branch ["
				+ baseBranchName + "] and base build [" + baseBuildName + "]");
		buildDiffInfo.setRemoved(comparisonUseCases.size());
		buildDiffInfo.setChangeRate(calculateChangeRate(baseUseCases.size(), buildDiffInfo.getAdded(),
				buildDiffInfo.getRemoved(), useCaseChangeRateSum));

		return buildDiffInfo;
	}

	private UseCase getUseCaseByName(final List<UseCase> useCases, final String useCaseName) {
		for (UseCase useCase : useCases) {
			if (useCaseName.equals(useCase.getName())) {
				return useCase;
			}
		}
		return null;
	}
}
