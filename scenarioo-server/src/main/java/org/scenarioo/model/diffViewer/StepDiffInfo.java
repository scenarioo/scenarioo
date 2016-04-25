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

/**
 * Contains the diff information for a step.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepDiffInfo extends AbstractDiffInfo {

	private int index;
	private String pageName;
	private int pageOccurrence;
	private int stepInPageOccurrence;

	/**
	 * @see org.scenarioo.model.diffViewer.AbstractDiffInfo#hasChanges()
	 */
	@Override
	public boolean hasChanges() {
		return getChangeRate() != 0;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName
	 *            the pageName to set
	 */
	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return the pageOccurrence
	 */
	public int getPageOccurrence() {
		return pageOccurrence;
	}

	/**
	 * @param pageOccurrence
	 *            the pageOccurrence to set
	 */
	public void setPageOccurrence(final int pageOccurrence) {
		this.pageOccurrence = pageOccurrence;
	}

	/**
	 * @return the stepInPageOccurrence
	 */
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}

	/**
	 * @param stepInPageOccurrence
	 *            the stepInPageOccurrence to set
	 */
	public void setStepInPageOccurrence(final int stepInPageOccurrence) {
		this.stepInPageOccurrence = stepInPageOccurrence;
	}

}
