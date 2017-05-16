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

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.diffViewer.DiffWriter;
import org.scenarioo.dao.diffViewer.impl.DiffWriterXmlImpl;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.utils.NumberFormatCreator;

import java.text.NumberFormat;

public abstract class AbstractComparator {

	protected static final String SCREENSHOT_FILE_EXTENSION = ".png";
	protected static final NumberFormat THREE_DIGIT_NUM_FORMAT = NumberFormatCreator
		.createNumberFormatWithMinimumIntegerDigits(3);

	protected static final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	protected ComparisonParameters parameters;
	protected ScenarioDocuReader docuReader;
	protected DiffWriter diffWriter;
	protected String baseBranchName;
	protected String baseBuildName;
	protected ComparisonConfiguration comparisonConfiguration;

	public AbstractComparator(ComparisonParameters parameters) {
		this.parameters = parameters;
		this.baseBranchName = parameters.getBaseBranchName();
		this.baseBuildName = parameters.getBaseBuildName();
		this.comparisonConfiguration = parameters.getComparisonConfiguration();

		this.docuReader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());
		this.diffWriter = new DiffWriterXmlImpl(parameters.getBaseBranchName(),
			parameters.getBaseBuildName(),
			parameters.getComparisonConfiguration().getName());
	}

}
