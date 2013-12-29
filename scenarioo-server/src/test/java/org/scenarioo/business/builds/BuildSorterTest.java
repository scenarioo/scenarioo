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

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Build;

public class BuildSorterTest {
	
	@Before
	public void setUp() {
		ConfigurationDAO.injectConfiguration(new Configuration());
	}
	
	@Test
	public void testSortingSomeBuilds() {
		
		// Given: Builds with Build aliases
		BuildLink build1 = createBuildSuccess("build1", 1);
		BuildLink build2 = createBuildSuccess("build2", 2);
		BuildLink build3 = createBuildFailed("build3", 3);
		BuildLink aliasCurrent = createBuildAlias("current", build2.getBuild());
		BuildLink aliasLast = createBuildAlias("last", build3.getBuild());
		List<BuildLink> buildsList = Arrays.asList(build1, build2, build3, aliasCurrent, aliasLast);
		
		// When: sorting this builds
		BuildSorter.sort(buildsList);
		
		// Then: the sorted list is in expected order
		assertSame("default build alias 'current' expected as first element", aliasCurrent, buildsList.get(0));
		assertSame("build alias 'last' expected as second element", aliasLast, buildsList.get(1));
		assertSame("build3 expected as first non-aliased build (builds sorted by dates decreasing)", build3,
				buildsList.get(2));
		assertSame("build1 expected as last non-aliased build (builds sorted by dates decreasing)", build1,
				buildsList.get(4));
	}
	
	private BuildLink createBuildSuccess(final String name, final int minuteOfDate) {
		return createNewBuild(name, name, "succcess", minuteOfDate);
	}
	
	private BuildLink createBuildFailed(final String name, final int minuteOfDate) {
		return createNewBuild(name, name, "failed", minuteOfDate);
	}
	
	private BuildLink createBuildAlias(final String aliasName, final Build build) {
		return new BuildLink(build, aliasName);
	}
	
	/**
	 * Create a usual build containing a valid date.
	 */
	private BuildLink createNewBuild(final String aliasName, final String name, final String status,
			final int minuteOfDate) {
		Build build = new Build(name);
		BuildLink result = new BuildLink(build, aliasName);
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MINUTE, minuteOfDate);
		build.setDate(cal.getTime());
		build.setRevision("1234");
		build.setStatus(status);
		return result;
	}
	
}
