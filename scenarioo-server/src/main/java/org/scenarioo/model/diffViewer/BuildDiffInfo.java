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

package org.scenarioo.model.diffViewer;

import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.Date;

@XmlRootElement
@XmlSeeAlso(UseCase.class)
public class BuildDiffInfo extends StructureDiffInfo<String, UseCase> {

	private BuildIdentifier baseBuild;

	private BuildIdentifier compareBuild;

	private ComparisonCalculationStatus status;

	private Date calculationDate;

	private Date baseBuildDate;

	public BuildDiffInfo() {
		super();
	}

	public BuildDiffInfo(final String comparisonName, final String comparisonBranchName,
			final String comparisonBuildName) {
		super(comparisonName);
		this.compareBuild = new BuildIdentifier(comparisonBranchName, comparisonBuildName);
	}

	public BuildIdentifier getBaseBuild() {
		return baseBuild;
	}

	public void setBaseBuild(BuildIdentifier baseBuild) {
		this.baseBuild = baseBuild;
	}

	public Date getBaseBuildDate() {
		return baseBuildDate;
	}

	public void setBaseBuildDate(Date baseBuildDate) {
		this.baseBuildDate = baseBuildDate;
	}

	public BuildIdentifier getCompareBuild() {
		return compareBuild;
	}

	public void setCompareBuild(BuildIdentifier compareBuild) {
		this.compareBuild = compareBuild;
	}

	public ComparisonCalculationStatus getStatus() {
		return status;
	}

	public void setStatus(ComparisonCalculationStatus status) {
		this.status = status;
	}

	public Date getCalculationDate() {
		return calculationDate;
	}

	public void setCalculationDate(Date calculationDate) {
		this.calculationDate = calculationDate;
	}

}
