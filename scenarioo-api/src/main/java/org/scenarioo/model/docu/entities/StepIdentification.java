/* scenarioo-api
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

package org.scenarioo.model.docu.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepIdentification implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public StepIdentification() {
	}
	
	public StepIdentification(final String useCaseName, final String scenarioName, final String pageName,
			final int index,
			final int occurence, final int relativeIndex) {
		this.useCaseName = useCaseName;
		this.scenarioName = scenarioName;
		this.pageName = pageName;
		this.index = index;
		this.occurence = occurence;
		this.relativeIndex = relativeIndex;
	}
	
	private String useCaseName;
	private String scenarioName;
	private String pageName;
	private int index;
	private int occurence;
	private int relativeIndex;
}
