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

import org.apache.commons.lang3.StringUtils;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.dao.diffViewer.impl.DiffWriterXmlImpl;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Abstract comparator class. Contains common comparison functionality.
 */
public abstract class AbstractComparator {

	private static final double ADDED_REMOVED_CHANGE_RATE = 100.0;

	protected final static ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	protected ScenarioDocuBuildsManager docuBuildsManager = ScenarioDocuBuildsManager.INSTANCE;
	protected ScenarioDocuReader docuReader;
	protected DiffWriter diffWriter;
	protected String baseBranchName;
	protected String baseBuildName;
	protected String comparisonName;

	public AbstractComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		this.docuReader = new ScenarioDocuReader(
				configurationRepository.getDocumentationDataDirectory());
		this.diffWriter = new DiffWriterXmlImpl(configurationRepository.getDiffViewerDirectory(), baseBranchName,
				baseBuildName,
				comparisonName);
		this.baseBranchName = baseBranchName;
		this.baseBuildName = baseBuildName;
		this.comparisonName = comparisonName;
	}

	protected double calculateChangeRate(final double numberOfBaseElements, final double numberOfAddedElements,
			final double numberOfRemovedElements,
			final double childChangeRateSum) {

		double changeRateSum = 0.0;
		changeRateSum += numberOfAddedElements * ADDED_REMOVED_CHANGE_RATE;
		changeRateSum += childChangeRateSum;
		changeRateSum += numberOfRemovedElements * ADDED_REMOVED_CHANGE_RATE;

		return changeRateSum / (numberOfBaseElements + numberOfRemovedElements);
	}

	protected BuildIdentifier getComparisonBuildIdentifier(final String comparisonName) {
		final ComparisonConfiguration comparisonConfiguration = resolveComparisonName(comparisonName);
		final BuildIdentifier comparisonBuildIdentifier = docuBuildsManager.resolveBranchAndBuildAliases(
				comparisonConfiguration.getComparisonBranchName(),
				comparisonConfiguration.getComparisonBuildName());
		return comparisonBuildIdentifier;
	}

	private ComparisonConfiguration resolveComparisonName(final String comparisonName) {
		if (StringUtils.isEmpty(comparisonName)) {
			throw new IllegalArgumentException("Unable to resolve empty comparison name.");
		}

		final Configuration configuration = configurationRepository.getConfiguration();
		for (final ComparisonConfiguration comparisonConfiguration : configuration.getComparisonConfigurations()) {
			if (comparisonName.equals(comparisonConfiguration.getName())) {
				return comparisonConfiguration;
			}
		}

		throw new RuntimeException(
				"Could not resolve a comparison configuration for comparison name [" + comparisonName + "]");
	}

}
