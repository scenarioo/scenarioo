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

import java.util.List;

import org.apache.log4j.Logger;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.FeatureDiffInfo;
import org.scenarioo.model.diffViewer.StructureDiffInfo;
import org.scenarioo.model.docu.entities.Feature;

/**
 * Comparison results are persisted in a xml file.
 */
public class FeatureComparator extends AbstractStructureComparator<Feature, String, Feature> {

	private static final Logger LOGGER = Logger.getLogger(FeatureComparator.class);

	private ScenarioComparator scenarioComparator = new ScenarioComparator(baseBranchName, baseBuildName,
			comparisonConfiguration);

	public FeatureComparator(final String baseBranchName, final String baseBuildName,
							 final ComparisonConfiguration comparisonConfiguration) {
		super(baseBranchName, baseBuildName, comparisonConfiguration);
	}

	public BuildDiffInfo compare() {
		final List<Feature> baseFeatures = docuReader.loadFeatures(baseBranchName, baseBuildName);
		final List<Feature> comparisonFeatures = docuReader.loadFeatures(
				comparisonConfiguration.getComparisonBranchName(),
				comparisonConfiguration.getComparisonBuildName());

		final BuildDiffInfo buildDiffInfo = new BuildDiffInfo(comparisonConfiguration.getName(),
				comparisonConfiguration.getComparisonBranchName(), comparisonConfiguration.getComparisonBuildName());

		calculateDiffInfo(baseFeatures, comparisonFeatures, buildDiffInfo);

		LOGGER.info(getLogMessage(buildDiffInfo, "Build " + baseBranchName + "/" + baseBuildName));

		return buildDiffInfo;
	}

	@Override
	protected double compareElementAndWrite(final Feature baseElement, final Feature comparisonElement,
											final StructureDiffInfo<String, Feature> diffInfo) {
		if (comparisonElement == null) {
			return 0;
		} else {
			final FeatureDiffInfo featureDiffInfo = scenarioComparator.compare(baseElement.getId());

			diffWriter.saveFeatureDiffInfo(featureDiffInfo);

			if (featureDiffInfo.hasChanges()) {
				diffInfo.setChanged(diffInfo.getChanged() + 1);
			}
			return featureDiffInfo.getChangeRate();
		}
	}

	@Override
	protected String getElementIdentifier(final Feature element) {
		return element.getId();
	}

	@Override
	protected String getAddedElementValue(final Feature element) {
		return element.getId();
	}

	@Override
	protected List<Feature> getRemovedElementValues(final List<Feature> removedElements) {
		return removedElements;
	}
}
