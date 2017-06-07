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

package org.scenarioo.model.docu.aggregates.features;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.ImportFeature;

/**
 * Represents a feature with all its scenarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureScenarios {

	private ImportFeature feature;

	@XmlElementWrapper(name = "scenarios")
	@XmlElement(name = "scenarioSummary")
	private List<ScenarioSummary> scenarios = new ArrayList<ScenarioSummary>();

	public ImportFeature getFeature() {
		return feature;
	}

	public void setFeature(final ImportFeature feature) {
		this.feature = feature;
	}

	public List<ScenarioSummary> getScenarios() {
		return scenarios;
	}

	public void setScenarios(final List<ScenarioSummary> scenarios) {
		this.scenarios = scenarios;
	}

}
