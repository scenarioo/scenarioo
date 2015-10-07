package org.scenarioo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.scenarioo.utils.ZipFileExtractor.ZipFileExtractionException;

// TODO #424 Fix unit tests
@Ignore
public class ZipFileExtractorTest {

	private final File currentFolder = new File(this.getClass().getResource(".").getPath());
	private final File targetFolder = new File(currentFolder, "extractedZipFile");

	@Test
	public void extractFile_withFileThatExists_successful() throws IOException, ZipFileExtractionException {
		File zipFile = new File(currentFolder, "ZipFileExtractorTestData.zip");
		File extractedFolder = new File(targetFolder, "aFolder");
		FileUtils.deleteDirectory(extractedFolder);
		Assert.assertTrue(!extractedFolder.exists());

		ZipFileExtractor.extractFile(zipFile, targetFolder);

		assertAllFoldersAndFilesAreExtracted(extractedFolder);
		assertZipFileStillExists(zipFile);
	}

	@Test
	public void extractFile_withFileThatDoesNotExists_throwsException() {
		File zipFile = new File(currentFolder, "InexistentFile.zip");
		try {
			ZipFileExtractor.extractFile(zipFile, targetFolder);
			Assert.fail();
		} catch (ZipFileExtractionException e) {
			Assert.assertEquals("Error while reading ZIP file.", e.getMessage());
		}
	}

	@Test
	public void extractFile_andTargetFolderExistsWithExistingFile_successfulAndExistingFilesAreRemoved()
			throws IOException, ZipFileExtractionException {
		File zipFile = new File(currentFolder, "ZipFileExtractorTestData.zip");
		File extractedFolder = new File(targetFolder, "aFolder");
		File existingFileInTargetFolder = new File(extractedFolder, "existingFile.txt");
		givenTargetFolderWithExistingFile(extractedFolder, existingFileInTargetFolder);

		ZipFileExtractor.extractFile(zipFile, targetFolder);

		assertAllFoldersAndFilesAreExtracted(extractedFolder);
		assertZipFileStillExists(zipFile);
		Assert.assertTrue(!existingFileInTargetFolder.exists());
	}

	private void givenTargetFolderWithExistingFile(final File extractedFolder, final File existingFileInTargetFolder)
			throws IOException {
		FileUtils.deleteDirectory(extractedFolder);
		FileUtils.forceMkdir(extractedFolder);
		existingFileInTargetFolder.createNewFile();
		Assert.assertTrue(extractedFolder.exists());
		Assert.assertTrue(existingFileInTargetFolder.exists());
	}

	private void assertAllFoldersAndFilesAreExtracted(final File extractedFolder) {
		Assert.assertTrue(extractedFolder.exists());
		Assert.assertTrue(new File(extractedFolder, "aSubFolder/anotherFile.txt").exists());
		Assert.assertTrue(new File(extractedFolder, "anotherSubFolder").exists());
		Assert.assertTrue(new File(extractedFolder, "aFile.txt").exists());
	}

	private void assertZipFileStillExists(final File zipFile) {
		Assert.assertTrue(zipFile.exists());
	}

}
