/* scenarioo-api
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

package org.scenarioo.api.util.files;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.scenarioo.api.exception.ResourceNotFoundException;

public class FilesUtil {
	
	private FilesUtil() {
	}

	public static String encodeName(final String name) {
		try {
			return URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"Unsupported UTF-8 charset. Scenarioo needs to run on a JVM or server environment that supports 'UTF-8'.",
					e);
		}
	}

	/**
	 * List all files in the given directory sorted alphanumerically using a collator.
	 */
	public static File[] listFiles(final File directory) {
		File[] files = directory.listFiles();
		Arrays.sort(files, new AlphanumericFileComparator());
		return files;
	}

	/**
	 * Read all files from 'directory'.
	 */
	public static List<File> getListOfFiles(final File directory) {
		List<File> result = new ArrayList<File>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File file : listFiles(directory)) {
			if (!file.isDirectory()) {
				result.add(file);
			}
		}
		return result;
	}

	/**
	 * Read all files with given name from all subdirectories of 'directory'.
	 */
	public static List<File> getListOfFilesFromSubdirs(final File directory, final String filename) {
		List<File> result = new ArrayList<File>();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new ResourceNotFoundException(directory.getAbsolutePath());
		}
		for (File subDir : listFiles(directory)) {
			if (subDir.isDirectory()) {
				File file = new File(subDir, filename);
				if (file.exists()) {
					result.add(file);
				}
			}
		}
		return result;
	}
	
}
