package org.scenarioo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipFileExtractor {

	public static class ZipFileExtractionException extends Exception {
		public ZipFileExtractionException(final String message, final Exception e) {
			super(message, e);
		}
	}

	/**
	 * Extracts the content of a ZIP file into a target directory. The target directory will be deleted first, if it
	 * already exists. The ZIP file is not deleted.
	 * 
	 * @throws ZipFileExtractionException
	 *             in case something goes wrong
	 */
	public static void extractFile(final File zipFileToExtract, final File targetDir) throws ZipFileExtractionException {
		ZipFile zipFile = getZipFile(zipFileToExtract);

		if (targetDir.exists()) {
			try {
				FileUtils.deleteDirectory(targetDir);
			} catch (IOException e) {
				throw new ZipFileExtractionException("Could not delete target directory.", e);
			}
		}

		try {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File entryDestination = new File(targetDir, entry.getName());
				if (entry.isDirectory()) {
					entryDestination.mkdirs();
				} else {
					entryDestination.getParentFile().mkdirs();
					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new FileOutputStream(entryDestination);
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
					out.close();
				}
			}
		} catch (IOException e) {
			throw new ZipFileExtractionException("IO Error while extracting ZIP file.", e);
		}
	}

	private static ZipFile getZipFile(final File zipFileToExtract) throws ZipFileExtractionException {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(zipFileToExtract);
		} catch (ZipException e) {
			throw new ZipFileExtractionException("Error while reading ZIP file " + zipFileToExtract, e);
		} catch (IOException e) {
			throw new ZipFileExtractionException("IO Error while reading ZIP file " + zipFileToExtract, e);
		}
		return zipFile;
	}

}
