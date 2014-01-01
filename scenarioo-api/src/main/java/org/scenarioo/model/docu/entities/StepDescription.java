/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules, according 
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
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

import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepDescription implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int index = 0;
	private String title = "";
	private String status = "";
	
	/**
	 * Only the filename of the image, the directory is defined through the scenario, usecase etc.
	 * 
	 * TODO: may be we should remove this from the model and replace by a convention ({stepIndex}.png: 001.png, 002.png
	 * etc.)
	 */
	private String screenshotFileName;
	
	private final Details details = new Details();
	
	/**
	 * TODO the following data should be removed from here, this does not belong into the API for generating data
	 */
	
	// TODO: name should be pageIndex
	private int occurence = 0;
	
	// TODO: name should be pageStepIndex
	private int relativeIndex = 0;
	
	private int variantIndex = 0;
	private StepIdentification previousStepVariant;
	private StepIdentification nextStepVariant;
	
	public void addDetails(final String key, final Object value) {
		details.addDetail(key, value);
	}
	
}
