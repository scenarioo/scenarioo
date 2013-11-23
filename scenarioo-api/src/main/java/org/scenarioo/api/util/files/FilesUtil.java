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
