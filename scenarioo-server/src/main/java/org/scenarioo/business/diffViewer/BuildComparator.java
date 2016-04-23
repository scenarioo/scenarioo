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

import org.scenarioo.model.diffViewer.StructureDiffInfo;

/**
 * Comparator to compare one build. Results are persisted in a xml file.
 */
public class BuildComparator extends AbstractComparator {

	public BuildComparator(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		super(baseBranchName, baseBuildName, comparisonName);
	}

	/**
	 * Compares base and comparison build.
	 * 
	 * @return {@link StructureDiffInfo} with the summarized diff information.
	 */
	public StructureDiffInfo compare() {
		StructureDiffInfo buildDiffInfo = new UseCaseComparator(baseBranchName, baseBuildName, comparisonName)
				.compare();

		diffWriter.saveBuildDiffInfo(buildDiffInfo);

		return buildDiffInfo;
	}

}
