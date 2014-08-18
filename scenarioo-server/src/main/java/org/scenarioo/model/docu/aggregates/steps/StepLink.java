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
	
	private String useCaseName;
	private String scenarioName;
	private String pageName;
	private int pageOccurrence;
	private int stepInPageOccurrence;
	private int pageIndex;
	private int stepIndex;
	
	public StepLink() {
	}
	
	public StepLink(final String useCaseName, final String scenarioName, final int stepIndex, final int pageIndex,
			final String pageName, final int pageOccurrence, final int stepInPageOccurrence) {
		super();
		this.useCaseName = useCaseName;
		this.scenarioName = scenarioName;
		this.stepIndex = stepIndex;
		this.pageIndex = pageIndex;
		this.pageName = pageName;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
	}
	
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
	public int getStepIndex() {
		return stepIndex;
	}
	
	public void setIndex(final int stepIndex) {
		this.stepIndex = stepIndex;
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
	 * Occurrence index of this page in the scenario. An occurrence of a page is a sequence of steps that have the same
	 * page and it ends as soon as there is a step with a different page.
	 */
	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public void setPageOccurrence(final int pageOccurrenceIndex) {
		this.pageOccurrence = pageOccurrenceIndex;
	}
	
	/**
	 * Step index inside current page occurrence.
	 */
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}
	
	public void setStepInPageOccurrence(final int stepInPageOccurrence) {
		this.stepInPageOccurrence = stepInPageOccurrence;
	}
	
	public String getStepIdentifierForObjectRepository() {
		return pageName + "/" + Integer.toString(pageOccurrence) + "/" + Integer.toString(stepInPageOccurrence);
	}
	
}
