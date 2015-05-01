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
import org.scenarioo.model.docu.entities.Labelable;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.generic.Details;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SketchStepDescription implements Serializable, Labelable, Detailable {

	private int index = 0;
	private String title = "";
	private final String status = "";
	private String originalScreenshotFileName;
	private String renderedSketchFileName;

	private Labels labels = new Labels();
	private Details details = new Details();

	public int getIndex() {
		return index;
	}

	/**
	 * The index needs to be the index of this sketchStep inside current scenario, starting with 0.
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * The title of current sketchStep. Usually the title shown on the page in the UI.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getOriginalScreenshotFileName() {
		return originalScreenshotFileName;
	}

	/**
	 * Conventions:
	 * - Standard file format: PNG
	 * - File name only, without any path (the path is fixed and defined by Scenarioo conventions)
	 * - File name must be unique for current sketchStep inside this proposal
	 */
	public void setOriginalScreenshotFileName(final String originalscreenshotFileName) {
		this.originalScreenshotFileName = originalscreenshotFileName;
	}

	public String getRenderedSketchFileName() {
		return renderedSketchFileName;
	}

	public void setRenderedSketchFileName(final String renderedSketchFileName) {
		this.renderedSketchFileName = renderedSketchFileName;
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

	@Override
	public Labels getLabels() {
		return labels;
	}

	@Override
	public Labels addLabel(final String label) {
		return labels.addLabel(label);
	}

	@Override
	public void setLabels(final Labels labels) {
		Preconditions.checkNotNull(labels, "Labels not allowed to set to null");
		this.labels = labels;
	}

}
