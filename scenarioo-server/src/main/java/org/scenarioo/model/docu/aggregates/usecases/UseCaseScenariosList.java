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

package org.scenarioo.model.docu.aggregates.usecases;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * List of all use cases with its scenarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UseCaseScenariosList {

	private String version;
	@XmlElementWrapper(name = "list")
	@XmlElement(name = "useCaseScenarios")
	private List<UseCaseScenarios> useCaseScenarios;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<UseCaseScenarios> getUseCaseScenarios() {
		return useCaseScenarios;
	}

	public void setUseCaseScenarios(List<UseCaseScenarios> useCaseScenarios) {
		this.useCaseScenarios = useCaseScenarios;
	}

}
