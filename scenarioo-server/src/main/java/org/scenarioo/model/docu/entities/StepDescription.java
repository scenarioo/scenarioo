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

import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.generic.Details;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepDescription implements Serializable, Labelable, Detailable {

	private int index = 0;
	private String title = "";
	private String status = "";
	private String screenshotFileName;

	private Labels labels = new Labels();
	private Details details = new Details();

	public int getIndex() {
		return index;
	}

	/**
	 * The index needs to be the index of this step inside current scenario, starting with 0.
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * The title of current step. Usually the title shown on the page in the UI.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * Set status of current step.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	public void setStatus(final Status status) {
		setStatus(Status.toKeywordNullSafe(status));
	}

	/**
	 * A status of current step. Usually it is "success", "failed" or "unknown". Use "failed" in case an assertion
	 * failed on current step, otherwise use "success" or "unknown". You can use different application-specific strings
	 * for marking any other special states of a step.
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	public String getScreenshotFileName() {
		return screenshotFileName;
	}

	/**
	 * Usualy this field is set for you by scenarioo on saving a step automatically, therefore you do not have to set it
	 * manually. Just use {@link ScenarioDocuWriter#saveScreenshotAsPng} to save your image as PNG with usual default
	 * filename and format.
	 *
	 * You can set a different screenshot file name here, if you like to use a different file format, than proposed by
	 * the API (which is PNG). In this case you have to ensure the following:
	 * <ul>
	 * <li>Only set the file name here, without any path (the path is fixed and defined by Scenarioo conventions)</li>
	 * <li>Make sure that the filename is unique for current step inside this scenario (e.g. something like
	 * "{stepIndex}.jpg")</li>
	 * <li>Save the file on your own in your preferred format under the passed name in following directory:
	 * {@link ScenarioDocuWriter#getScreenshotsDirectory(String, String)}</li>
	 * </ul>
	 */
	public void setScreenshotFileName(final String screenshotFileName) {
		this.screenshotFileName = screenshotFileName;
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
