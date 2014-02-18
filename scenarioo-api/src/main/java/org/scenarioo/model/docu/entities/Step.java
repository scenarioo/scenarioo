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

/**
 * Contains all the data collected from a webtest for one step of the webtest.
 * 
 * The data is processed by the Scenarioo webapplication to transform into webapps internal structure.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Step implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Page page;
	private StepDescription stepDescription;
	private StepHtml html;
	private StepMetadata metadata = new StepMetadata();
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}
	
	public StepDescription getStepDescription() {
		return stepDescription;
	}
	
	public void setStepDescription(StepDescription stepDescription) {
		this.stepDescription = stepDescription;
	}
	
	public StepHtml getHtml() {
		return html;
	}
	
	public void setHtml(StepHtml html) {
		this.html = html;
	}
	
	public StepMetadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(StepMetadata metadata) {
		this.metadata = metadata;
	}
	
}
