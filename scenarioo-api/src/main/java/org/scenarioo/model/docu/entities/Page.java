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

import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Page implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Details details = new Details();
	
	public Page() {
		this.name = "";
	}
	
	public Page(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(Details details) {
		this.details = details;
	}
	
	@Override
    	public int hashCode() {
    		final int prime = 31;
    		int result = 1;
    		result = prime * result + ((name == null) ? 0 : name.hashCode());
    		return result;
    	}

    	@Override
    	public boolean equals(Object obj) {
    		if (this == obj) {
    			return true;
    		}
    		if (obj == null) {
    			return false;
    		}
    		if (getClass() != obj.getClass()) {
    			return false;
    		}
    		Page other = (Page) obj;
    		if (name == null) {
    			if (other.name != null) {
    				return false;
    			}
    		} else if (!name.equals(other.name)) {
    			return false;
    		}
    		return true;
    	}
	
}
