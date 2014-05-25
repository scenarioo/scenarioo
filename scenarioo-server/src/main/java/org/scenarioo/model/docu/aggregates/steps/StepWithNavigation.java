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

import org.scenarioo.model.docu.entities.Step;

/**
 * Container class to load a step with its navigation data
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepWithNavigation {
	
	private Step step;
	
	private StepNavigation stepNavigation;
	
	public StepWithNavigation(final Step step, final StepNavigation stepNavigation) {
		super();
		this.step = step;
		this.stepNavigation = stepNavigation;
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
	
}
