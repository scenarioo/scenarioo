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

package org.scenarioo.utils;

import java.io.File;

/**
 * Eclipse and Gradle unfortunately do not use the same folders for to store test resources. This utility
 * helps you to get a test resource {@link File} that works in both builds.
 */
public class TestResourceFile {

	private static final File basePath = new File(TestResourceFile.class.getResource("../../../").getPath());

	public static File getResourceFile(final String filePackageAndFilename) {
		System.out.println("Base path: " + basePath);

		if (basePath.getAbsolutePath().matches(".*build.classes.test.*")) {
			// Gradle build
			File correctedBasePath = new File(basePath.getAbsolutePath().replaceAll("build.classes.test",
					"build/resources/test"));
			return new File(correctedBasePath, filePackageAndFilename);
		} else {
			// Eclipse build
			return new File(basePath, filePackageAndFilename);
		}
	}

}
