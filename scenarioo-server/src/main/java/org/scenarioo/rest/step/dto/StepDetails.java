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

package org.scenarioo.rest.step.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.rest.base.StepIdentifier;

/**
 * All the information needed to display a single step.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepDetails {
	
	private StepIdentifier stepIdentifier;
	private boolean fallback;
	private Step step;
	private StepNavigation stepNavigation;
	private StepStatistics stepStatistics;
	private Labels useCaseLabels;
	private Labels scenarioLabels;
	
	public StepDetails() {
	}
	
	public StepDetails(final StepIdentifier stepIdentifier, final boolean fallback, final Step step,
			final StepNavigation stepNavigation, final StepStatistics stepStatistics, final Labels useCaseLabels,
			final Labels scenarioLabels) {
		this.stepIdentifier = stepIdentifier;
		this.fallback = fallback;
		this.step = step;
		this.stepNavigation = stepNavigation;
		this.stepStatistics = stepStatistics;
		this.useCaseLabels = useCaseLabels;
		this.scenarioLabels = scenarioLabels;
	}
	
	public StepIdentifier getStepIdentifier() {
		return stepIdentifier;
	}
	
	public void setStepIdentifier(final StepIdentifier stepIdentifier) {
		this.stepIdentifier = stepIdentifier;
	}
	
	public boolean isFallback() {
		return fallback;
	}
	
	public void setFallback(final boolean fallback) {
		this.fallback = fallback;
	}
	
	public Step getStep() {
		return step;
	}
	
	public void setStep(final Step step) {
		this.step = step;
	}
	
	public StepNavigation getStepNavigation() {
		return stepNavigation;
	}
	
	public void setStepNavigation(final StepNavigation stepNavigation) {
		this.stepNavigation = stepNavigation;
	}
	
	public StepStatistics getStepStatistics() {
		return stepStatistics;
	}
	
	public void setStepStatistics(final StepStatistics stepStatistics) {
		this.stepStatistics = stepStatistics;
	}
	
	public Labels getUseCaseLabels() {
		return useCaseLabels;
	}
	
	public void setUseCaseLabels(final Labels useCaseLabels) {
		this.useCaseLabels = useCaseLabels;
	}
	
	public Labels getScenarioLabels() {
		return scenarioLabels;
	}
	
	public void setScenarioLabels(final Labels scenarioLabels) {
		this.scenarioLabels = scenarioLabels;
	}
	
}
