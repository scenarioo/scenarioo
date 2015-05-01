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

package org.scenarioo.rest.sketchStep.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.entities.SketchStep;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.rest.base.design.SketchStepIdentifier;

/**
 * All the information needed to display a single step.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SketchStepDetails {
	
	private SketchStepIdentifier sketchStepIdentifier;
	private boolean fallback;
	private SketchStep sketchStep;
	private StepNavigation stepNavigation;
	private StepStatistics stepStatistics;
	private Labels issueLabels;
	private Labels proposalLabels;
	
	public SketchStepDetails() {
	}
	
	public SketchStepDetails(final SketchStepIdentifier sketchStepIdentifier, final boolean fallback,
			final SketchStep sketchStep,
			final StepNavigation stepNavigation, final StepStatistics stepStatistics, final Labels useCaseLabels,
			final Labels scenarioLabels) {
		this.sketchStepIdentifier = sketchStepIdentifier;
		this.fallback = fallback;
		this.sketchStep = sketchStep;
		this.stepNavigation = stepNavigation;
		this.stepStatistics = stepStatistics;
		this.issueLabels = useCaseLabels;
		this.proposalLabels = scenarioLabels;
	}
	
	public SketchStepIdentifier getSketchStepIdentifier() {
		return sketchStepIdentifier;
	}
	
	public void setSketchStepIdentifier(final SketchStepIdentifier sketchStepIdentifier) {
		this.sketchStepIdentifier = sketchStepIdentifier;
	}
	
	public boolean isFallback() {
		return fallback;
	}
	
	public void setFallback(final boolean fallback) {
		this.fallback = fallback;
	}
	
	public SketchStep getSketchStep() {
		return sketchStep;
	}
	
	public void setSketchStep(final SketchStep sketchStep) {
		this.sketchStep = sketchStep;
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
	
	public Labels getIssueLabels() {
		return issueLabels;
	}
	
	public void setIssueLabels(final Labels issueLabels) {
		this.issueLabels = issueLabels;
	}
	
	public Labels getProposalLabels() {
		return proposalLabels;
	}
	
	public void setPropoalLabels(final Labels proposalLabels) {
		this.proposalLabels = proposalLabels;
	}
	
}
