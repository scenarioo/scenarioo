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

package org.scenarioo.model.docu.entities.screenAnnotations;

import org.scenarioo.model.docu.entities.Detailable;
import org.scenarioo.model.docu.entities.generic.Details;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link ScreenAnnotation}s are used to visually mark regions of an image. This feature can e.g. be used to mark the
 * region where a click happens in the web tests.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScreenAnnotation implements Detailable {

	@XmlElement(required = true)
	private ScreenRegion region;
	private ScreenAnnotationStyle style = ScreenAnnotationStyle.DEFAULT;
	private String screenText = "";
	private String title = "";
	private String description = "";
	private Details details = new Details();
	private ScreenAnnotationClickAction clickAction = null;
	private String clickActionUrl = null;
	private String clickActionText = null;

	/**
	 * Default constructor, only for use by JAXB.
	 */
	public ScreenAnnotation() {
	}

	/**
	 * @param x
	 *            distance from left end of the screenshot in pixels
	 * @param y
	 *            distance from top end of the screenshot in pixels
	 * @param width
	 *            width of the region in pixels
	 * @param height
	 *            height of the region in pixels
	 */
	public ScreenAnnotation(final int x, final int y, final int width, final int height) {
		this.region = new ScreenRegion(x, y, width, height);
	}

	public ScreenRegion getRegion() {
		return region;
	}

	/**
	 * Set the rectangular region inside the screenshot to highlight and put an annotation on
	 *
	 * @param x
	 *            distance from left end of the screenshot in pixels
	 * @param y
	 *            distance from top end of the screenshot in pixels
	 * @param width
	 *            width of the region in pixels
	 * @param height
	 *            height of the region in pixels
	 */
	public void setRegion(final int x, final int y, final int width, final int height) {
		setRegion(new ScreenRegion(x, y, width, height));
	}

	/**
	 * Set the rectangular region inside the screenshot to highlight and put an annotation on.
	 * This property is mandatory.
	 */
	public void setRegion(final ScreenRegion region) {
		this.region = region;
	}

	public ScreenAnnotationStyle getStyle() {
		return style;
	}

	/**
	 * (optional) Set the visual style of the annotation (if not set, the same DEFAULT style will be used).
	 */
	public void setStyle(final ScreenAnnotationStyle style) {
		this.style = style;
	}

	public String getScreenText() {
		return screenText;
	}

	/**
	 * (optional) Set the text to display inside the annotation on the screenshot
	 * (should be short, use description for longer texts).
	 * Long texts will be truncated if they do not fit inside the annotation.
	 * The full text will be shown in the popup of the annotation, but only if the "title" is not set.
	 */
	public void setScreenText(final String text) {
		this.screenText = text;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * (optional) Set a separate title text, that is only displayed inside the popup window (and not on the screen).
	 * If set, this text is displayed instead of "screenText" inside the popup.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional) Set a longer textual description for this annotation. This description is displayed below the shorter
	 * 'text' inside an info popup that can be opened on the annotation.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public ScreenAnnotationClickAction getClickAction() {
		return clickAction;
	}

	/**
	 * (optional) Set a click action to define what happens, when the annotation is clicked on. If no click action ist
	 * set, the info popup with additional information about the annotation opens.
	 */
	public void setClickAction(final ScreenAnnotationClickAction clickAction) {
		this.clickAction = clickAction;
	}

	public String getClickActionUrl() {
		return clickActionUrl;
	}

	/**
	 * (optional, but mandatory in case that clickAction is {@link ScreenAnnotationClickAction#TO_URL})
	 * The URL that is opened in a new tab or window when the annotation is clicked on.
	 */
	public void setClickActionUrl(final String clickActionUrl) {
		this.clickActionUrl = clickActionUrl;
	}

	public String getClickActionText() {
		return clickActionText;
	}

	/**
	 * (optional) Text to display for the click action (for link in popup and as a tooltip on the annotation)
	 * If it is not set, a meaningful default is used.
	 */
	public void setClickActionText(final String clickActionText) {
		this.clickActionText = clickActionText;
	}

	@Override
	public Details getDetails() {
		return details;
	}

	@Override
	public void setDetails(final Details details) {
		this.details = details;
	}

	@Override
	public Details addDetail(final String key, final Object value) {
		details.put(key, value);
		return details;
	}

}
