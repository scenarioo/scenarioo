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

package org.scenarioo.business.builds;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.derived.BuildLink;

/**
 * Implementation of special sorting order for builds:
 * <ol>
 * <li>1. default build = last successful build (generated alias) is always sorted on top</li>
 * <li>2. other tagged builds in order of their dates</li>
 * <li>3. other builds in order of their dates</li>
 * <li>4. If dates are missing, the builds are sorted by names</li>
 * </ol>
 */
public class BuildSorter implements Comparator<BuildLink> {
	
	private String defaultBuildName = ConfigurationDAO.getConfiguration().getDefaultBuildName();
	
	private Collator collator = Collator.getInstance();
	
	/**
	 * Sort the passed list of builds
	 */
	public static void sort(final List<BuildLink> builds) {
		Collections.sort(builds, new BuildSorter());
	}
	
	@Override
	public int compare(final BuildLink bl1, final BuildLink bl2) {
		
		// Default build is always sorted to the top
		if (bl1.getLinkName().equals(defaultBuildName)) {
			return -1;
		}
		if (bl2.getLinkName().equals(defaultBuildName)) {
			return 1;
		}
		
		// Link (=tagged build) is always sorted to the top if other is
		// not a link
		boolean isLink1 = !bl1.getBuild().getName().equals(bl1.getLinkName());
		boolean isLink2 = !bl2.getBuild().getName().equals(bl2.getLinkName());
		if (isLink1 && !isLink2) {
			return -1;
		}
		if (!isLink1 && isLink2) {
			return 1;
		}
		
		// if date is missing simply sort by link name, but builds with
		// a date are sorted on top.
		if (bl1.getBuild().getDate() == null && bl2.getBuild().getDate() == null) {
			return collator.compare(bl1.getLinkName(), bl2.getLinkName());
		} else if (bl1.getBuild().getDate() == null) {
			return 1;
		} else if (bl2.getBuild().getDate() == null) {
			return -1;
		}
		
		// Links and non-links are both sorted by date (descending!)
		return bl2.getBuild().getDate().compareTo(bl1.getBuild().getDate());
	}
	
}
