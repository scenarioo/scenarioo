package org.scenarioo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.utils.ZipFileExtractor.ZipFileExtractionException;

public class ZipFileExtractorTest {

	private static File currentFolder = new File(ZipFileExtractorTest.class.getResource(".").getPath());
	private static File targetFolder = new File(currentFolder, "extractedZipFile");
	private static File zipFile;

	@BeforeClass
	public static void setupClass() {
		zipFile = TestResourceFile.getResourceFile("org/scenarioo/utils/ZipFileExtractorTestData.zip");
		Assert.assertTrue(zipFile.exists());
	}

	@Test
	public void extractFile_withFileThatExists_successful() throws IOException, ZipFileExtractionException {
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
			Assert.assertTrue(e.getMessage().startsWith("Zip file does not exist "));
		}
	}

	@Test
	public void extractFile_andTargetFolderExistsWithExistingFile_successfulAndExistingFilesAreRemoved()
			throws IOException, ZipFileExtractionException {
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
