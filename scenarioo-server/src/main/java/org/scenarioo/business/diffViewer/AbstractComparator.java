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

import org.apache.commons.lang3.StringUtils;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.model.configuration.ComparisonAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Abstract comparator class. Contains common comparison functionality.
 */
public abstract class AbstractComparator {

	private static final double ADDED_REMOVED_CHANGE_RATE = 100.0;

	protected final static ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	protected ScenarioDocuReader docuReader;
	protected DiffWriter diffWriter;
	protected String baseBranchName;
	protected String baseBuildName;
	protected String comparisonName;

	public AbstractComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		this.docuReader = new ScenarioDocuReader(
				configurationRepository.getDiffViewerDirectory());
		this.diffWriter = new DiffWriter(configurationRepository.getDiffViewerDirectory(), baseBranchName,
				baseBuildName,
				comparisonName);
		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonName = comparisonName;
	}

	protected double calculateChangeRate(double numberOfBaseElements, double numberOfAddedElements,
			double numberOfRemovedElements,
			double childChangeRateSum) {

		double changeRateSum = 0.0;
		changeRateSum += numberOfAddedElements * ADDED_REMOVED_CHANGE_RATE;
		changeRateSum += childChangeRateSum;
		changeRateSum += numberOfRemovedElements * ADDED_REMOVED_CHANGE_RATE;

		return changeRateSum / (numberOfBaseElements + numberOfRemovedElements);
	}

	protected ComparisonAlias resolveComparisonName(final String comparisonName) {
		if (StringUtils.isEmpty(comparisonName)) {
			throw new IllegalArgumentException("Unable to resolve empty comparison name.");
		}

		Configuration configuration = configurationRepository.getConfiguration();
		for (ComparisonAlias comparisonAlias : configuration.getComparisonAliases()) {
			if (comparisonName.equals(comparisonAlias.getComparisonName())) {
				return comparisonAlias;
			}
		}

		throw new RuntimeException("Could not resolve a comparison alias for comparison name [" + comparisonName + "]");
	}

}
