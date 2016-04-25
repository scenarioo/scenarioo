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

package org.scenarioo.model.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a comparison alias to a comparison build.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComparisonAlias {

	private String comparisonName;
	private String baseBranchName;
	private String comparisonBranchName;
	private String comparisonBuildName;

	/**
	 * @return the comparisonName
	 */
	public String getComparisonName() {
		return comparisonName;
	}

	/**
	 * @param comparisonName
	 *            the comparisonName to set
	 */
	public void setComparisonName(final String comparisonName) {
		this.comparisonName = comparisonName;
	}

	/**
	 * @return the baseBranchName
	 */
	public String getBaseBranchName() {
		return baseBranchName;
	}

	/**
	 * @param baseBranchName
	 *            the baseBranchName to set
	 */
	public void setBaseBranchName(final String baseBranchName) {
		this.baseBranchName = baseBranchName;
	}

	/**
	 * @return the comparisonBranchName
	 */
	public String getComparisonBranchName() {
		return comparisonBranchName;
	}

	/**
	 * @param comparisonBranchName
	 *            the comparisonBranchName to set
	 */
	public void setComparisonBranchName(final String comparisonBranchName) {
		this.comparisonBranchName = comparisonBranchName;
	}

	/**
	 * @return the comparisonBuildName
	 */
	public String getComparisonBuildName() {
		return comparisonBuildName;
	}

	/**
	 * @param comparisonBuildName
	 *            the comparisonBuildName to set
	 */
	public void setComparisonBuildName(final String comparisonBuildName) {
		this.comparisonBuildName = comparisonBuildName;
	}

}
