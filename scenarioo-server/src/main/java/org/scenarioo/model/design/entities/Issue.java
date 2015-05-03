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
public class Issue implements Serializable, Labelable, Detailable {

	private String name;
	private String description;
	private String status;

	private Details details = new Details();
	private Labels labels = new Labels();

	public Issue() {
	}

	public Issue(final String name, final String description) {
		this();
		this.name = name;
		this.description = description;
		this.status = "Open";
	}

	public String getName() {
		return name;
	}

	/**
	 * A unique name for this issue.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional but recommended) More detailed description for current scenario (additionally to descriptive name).
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getIssueStatus() {
		return status;
	}

	/**
	 * Set status of current issue. Scenarioo supports open and closed by default.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	// public void setStatus(final IssueStatus status) {
	// setStatus(IssueStatus.toKeywordNullSafe(status));
	// }

	public void setStatus(final String status) {
		this.status = status;
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
