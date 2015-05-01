/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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

package org.scenarioo.model.design.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.Detailable;
import org.scenarioo.model.docu.entities.generic.Details;

/**
 * Metadata for a step. This is a container for all additional detail data about a step that is only displayed on
 * details page for a step.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SketchStepMetadata implements Serializable, Detailable {

	private String visibleText;

	private Details details = new Details();

	public String getVisibleText() {
		return visibleText;
	}

	/**
	 * (optional) You can set all visible text of a step here to provide possibility to search inside visible step text.
	 * But currently the scenarioo webapplication does not yet support full text search anyway and also not to display
	 * this visible text anywhere in the webapplication.
	 * 
	 * @param visibleText
	 */
	public void setVisibleText(final String visibleText) {
		this.visibleText = visibleText;
	}

	@Override
	public Details getDetails() {
		return details;
	}

	@Override
	public Details addDetail(final String key, final Object value) {
		return details.addDetail(key, value);
	}

	@Override
	public void setDetails(final Details details) {
		Preconditions.checkNotNull(details, "Details not allowed to set to null");
		this.details = details;
	}

}
