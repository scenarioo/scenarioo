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

package org.scenarioo.model.docu.aggregates.steps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Describes a link to a step containing all needed information (in context of a build) to reference a step in all
 * possible address formats.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepLink {
	
	public StepLink() {
	}
	
	public StepLink(final String useCaseName, final String scenarioName, final int index, final int pageIndex,
			final String pageName, final int pageOccurenceIndex, final int pageStepIndex) {
		super();
		this.useCaseName = useCaseName;
		this.scenarioName = scenarioName;
		this.index = index;
		this.pageIndex = pageIndex;
		this.pageName = pageName;
		this.pageOccurenceIndex = pageOccurenceIndex;
		this.pageStepIndex = pageStepIndex;
	}
	
	private String useCaseName;
	private String scenarioName;
	private int index;
	private int pageIndex;
	private String pageName;
	private int pageOccurenceIndex;
	private int pageStepIndex;
	
	public String getUseCaseName() {
		return useCaseName;
	}
	
	public void setUseCaseName(final String useCaseName) {
		this.useCaseName = useCaseName;
	}
	
	public String getScenarioName() {
		return scenarioName;
	}
	
	public void setScenarioName(final String scenarioName) {
		this.scenarioName = scenarioName;
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}
	
	/**
	 * Overall step index inside the scenario.
	 */
	public int getIndex() {
		return index;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
	/**
	 * the index of the page inside all pages of current scenario
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setPageIndex(final int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	/**
	 * number of occurence of this same page inside the scenario
	 */
	public int getPageOccurenceIndex() {
		return pageOccurenceIndex;
	}
	
	public void setPageOccurenceIndex(final int pageOccurenceIndex) {
		this.pageOccurenceIndex = pageOccurenceIndex;
	}
	
	/**
	 * step index inside current page
	 */
	public int getPageStepIndex() {
		return pageStepIndex;
	}
	
	public void setPageStepIndex(final int pageStepIndex) {
		this.pageStepIndex = pageStepIndex;
	}
	
}
