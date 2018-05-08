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
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.model.docu.entities.UseCase;

import java.util.List;

public class UseCaseComparator extends AbstractStructureComparator<UseCase, String, UseCase> {

	private static final Logger LOGGER = Logger.getLogger(UseCaseComparator.class);

	private ScenarioComparator scenarioComparator = new ScenarioComparator(parameters);

	public UseCaseComparator(ComparisonParameters parameters) {
		super(parameters);
	}

	public BuildDiffInfo compare() {
		final List<UseCase> baseUseCases = scenarioDocuReader.loadUsecases(parameters.getBaseBranchName(), parameters.getBaseBuildName());
		final List<UseCase> comparisonUseCases = scenarioDocuReader.loadUsecases(
			parameters.getComparisonConfiguration().getComparisonBranchName(),
			parameters.getComparisonConfiguration().getComparisonBuildName());

		final BuildDiffInfo buildDiffInfo = new BuildDiffInfo(parameters.getComparisonConfiguration().getName(),
			parameters.getComparisonConfiguration().getComparisonBranchName(), parameters.getComparisonConfiguration().getComparisonBuildName());

		calculateDiffInfo(baseUseCases, comparisonUseCases, buildDiffInfo);
		buildDiffInfo.setComparisonCalculationStatus(ComparisonCalculationStatus.SUCCESS);

		LOGGER.info(getLogMessage(buildDiffInfo, "Build " + parameters.getBaseBranchName() + "/" + parameters.getBaseBuildName()));

		return buildDiffInfo;
	}

	@Override
	protected double compareElementAndWrite(final UseCase baseElement, final UseCase comparisonElement,
											final StructureDiffInfo<String, UseCase> diffInfo) {
		if (comparisonElement == null) {
			return 0;
		} else {
			final UseCaseDiffInfo useCaseDiffInfo = scenarioComparator.compare(baseElement.getName());

			parameters.getDiffWriter().saveUseCaseDiffInfo(useCaseDiffInfo);

			if (useCaseDiffInfo.hasChanges()) {
				diffInfo.setChanged(diffInfo.getChanged() + 1);
			}
			return useCaseDiffInfo.getChangeRate();
		}
	}

	@Override
	protected String getElementIdentifier(final UseCase element) {
		return element.getName();
	}

	@Override
	protected String getAddedElementValue(final UseCase element) {
		return element.getName();
	}

	@Override
	protected List<UseCase> getRemovedElementValues(final List<UseCase> removedElements) {
		return removedElements;
	}

}
