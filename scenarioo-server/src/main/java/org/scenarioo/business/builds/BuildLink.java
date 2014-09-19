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

package org.scenarioo.business.builds;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.scenarioo.model.docu.entities.Build;

/**
 * Represents a build that might also be linked in file system under a different name (used for tagging builds). Usually
 * the linkName should be the same as the real build name, but could be different in case a build is linked under a
 * directory with a different name.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildLink implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * If this name is set, it is used as the display name of the build. If its not set, the
	 * <code>linkName<code> is used and if the
	 * <code>linkName</code> is not set, the build revision is used.
	 */
	private String displayName;
	
	/**
	 * This is the name used for all REST calls. It can be either a physical build name or a build alias.
	 */
	private String linkName;
	
	private Build build;
	
	public BuildLink() {
	}
	
	/**
	 * Create build that is linked in file system under given linkName (might be the same as name but does not have to
	 * be).
	 */
	public BuildLink(final Build build, final String linkName) {
		this.build = build;
		this.linkName = linkName;
	}
	
	public String getLinkName() {
		return linkName;
	}
	
	public void setLinkName(final String linkName) {
		this.linkName = linkName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}
	
	public Build getBuild() {
		return build;
	}
	
	public void setBuild(final Build build) {
		this.build = build;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(build).append(linkName).append(displayName).toHashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BuildLink other = (BuildLink) obj;
		
		return new EqualsBuilder().append(build, other.build).append(linkName, other.linkName)
				.append(displayName, other.displayName).isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(build).append(linkName).append(displayName).toString();
	}
	
}
