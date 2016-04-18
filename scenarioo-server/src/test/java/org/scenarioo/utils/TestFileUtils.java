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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.scenarioo.repository.RepositoryLocator;


public class TestFileUtils {

	public static void createFolderAndClearContent(final File folder) {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		try {
			FileUtils.cleanDirectory(folder);
		} catch (IOException e) {
			fail();
		}
	}

	public static void createFolderAndSetItAsRootInConfigurationForUnitTest(final File folder) {
		createFolderAndClearContent(folder);
		RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(folder);
	}

}
