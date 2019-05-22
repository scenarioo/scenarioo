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

package org.scenarioo.model.docu.aggregates.branches;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.model.docu.entities.Branch;

/**
 * Represents a branch and all its belonging builds that have been successfully processed by server and therefore are
 * ready for browsing.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchBuilds {

	@JsonProperty("isAlias")
	private boolean isAlias;

	private Branch branch;

	@XmlElementWrapper(name = "builds")
	@XmlElement(name = "buildLink")
	private List<BuildLink> builds = new ArrayList<>();

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(final Branch branch) {
		this.branch = branch;
	}

	public List<BuildLink> getBuilds() {
		return builds;
	}

	public void setBuilds(final List<BuildLink> builds) {
		this.builds = builds;
	}

	public boolean isAlias() {
		return isAlias;
	}

	public void setAlias(final boolean isAlias) {
		this.isAlias = isAlias;
	}

}
