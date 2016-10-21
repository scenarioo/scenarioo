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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepDiffInfo extends AbstractDiffInfo {

	private int index;
	private String pageName;
	private int pageOccurrence;
	private int stepInPageOccurrence;
	private String comparisonScreenshotName;

	@Override
	public boolean hasChanges() {
		return getChangeRate() != 0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}

	public int getPageOccurrence() {
		return pageOccurrence;
	}

	public void setPageOccurrence(final int pageOccurrence) {
		this.pageOccurrence = pageOccurrence;
	}

	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}

	public void setStepInPageOccurrence(final int stepInPageOccurrence) {
		this.stepInPageOccurrence = stepInPageOccurrence;
	}

	public String getComparisonScreenshotName() {
		return comparisonScreenshotName;
	}

	public void setComparisonScreenshotName(final String comparisonScreenshotName) {
		this.comparisonScreenshotName = comparisonScreenshotName;
	}

}
