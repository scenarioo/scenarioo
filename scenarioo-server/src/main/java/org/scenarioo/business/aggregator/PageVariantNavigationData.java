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

package org.scenarioo.business.aggregator;

import org.scenarioo.model.docu.aggregates.steps.StepLink;

/**
 * 
 */
public class PageVariantNavigationData {
	
	private StepLink lastStep;
	private StepLink lastStepFromDifferentScenario;
	private int scenariosCount = 0;
	private int stepsCount = 0;
	
	public void increaseScenariosCount() {
		scenariosCount++;
	}
	
	public void increaseStepsCount() {
		stepsCount++;
	}
	
	public StepLink getLastStep() {
		return lastStep;
	}
	
	public void setLastStep(final StepLink lastStep) {
		this.lastStep = lastStep;
	}
	
	public StepLink getLastStepFromDifferentScenario() {
		return lastStepFromDifferentScenario;
	}
	
	public void setLastStepFromDifferentScenario(final StepLink lastStepFromDifferentScenario) {
		this.lastStepFromDifferentScenario = lastStepFromDifferentScenario;
	}
	
	public int getScenariosCount() {
		return scenariosCount;
	}
	
	public void setScenariosCount(final int scenariosCount) {
		this.scenariosCount = scenariosCount;
	}
	
	public int getStepsCount() {
		return stepsCount;
	}
	
	public void setStepsCount(final int stepsCount) {
		this.stepsCount = stepsCount;
	}
	
}
