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

package org.scenarioo.api.util.files;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.util.UriEncoder;
import org.scenarioo.api.exception.ResourceNotFoundException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesUtil {

	private static final Pattern PATTERN = Pattern.compile("[^A-Za-z0-9_\\-\\.]");

	private static final int MAX_LENGTH = 127;
	private FilesUtil() {
	}

	public static String decodeName(final String name) {
		return UriEncoder.decode(name);
	}

	public static String encodeName(final String name) {
		if (name.contains(" ") ||
			name.contains("?")||
			name.contains("&")||
			name.contains(":")) {

			StringBuffer sb = new StringBuffer();

			Matcher m = PATTERN.matcher(name);

			while (m.find()) {
				String replacement = "%" + Integer.toHexString(m.group().charAt(0)).toUpperCase();
				m.appendReplacement(sb, replacement);
			}
			m.appendTail(sb);

			String encoded = sb.toString();

			int end = Math.min(encoded.length(), MAX_LENGTH);
			return encoded.substring(0, end);
		}

		try {
			new URI(name);
			return name;
		} catch (URISyntaxException e) {
			return UriEncoder.encode(name);
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
