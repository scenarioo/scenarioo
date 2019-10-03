package org.scenarioo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.scenarioo.utils.ZipFileExtractor.ZipFileExtractionException;

import static org.junit.jupiter.api.Assertions.*;

class ZipFileExtractorTest {

	private static File currentFolder = new File(ZipFileExtractorTest.class.getResource(".").getPath());
	private static File targetFolder = new File(currentFolder, "extractedZipFile");
	private static File zipFile;

	@BeforeAll
	static void setupClass() {
		zipFile = TestResourceFile.getResourceFile("org/scenarioo/utils/ZipFileExtractorTestData.zip");
		assertTrue(zipFile.exists());
	}

	@Test
	void extractFile_withFileThatExists_successful() throws IOException, ZipFileExtractionException {
		File extractedFolder = new File(targetFolder, "aFolder");
		FileUtils.deleteDirectory(extractedFolder);
		assertFalse(extractedFolder.exists());

		ZipFileExtractor.extractFile(zipFile, targetFolder);

		assertAllFoldersAndFilesAreExtracted(extractedFolder);
		assertZipFileStillExists(zipFile);
	}

	@Test
	void extractFile_withFileThatDoesNotExists_throwsException() {
		File zipFile = new File(currentFolder, "InexistentFile.zip");
		final ZipFileExtractionException thrown = assertThrows(ZipFileExtractionException.class, () -> ZipFileExtractor.extractFile(zipFile, targetFolder));
		assertTrue(thrown.getMessage().startsWith("Zip file does not exist "));
	}

	@Test
	void extractFile_andTargetFolderExistsWithExistingFile_successfulAndExistingFilesAreRemoved()
			throws IOException, ZipFileExtractionException {
		File extractedFolder = new File(targetFolder, "aFolder");
		File existingFileInTargetFolder = new File(extractedFolder, "existingFile.txt");
		givenTargetFolderWithExistingFile(extractedFolder, existingFileInTargetFolder);

		ZipFileExtractor.extractFile(zipFile, targetFolder);

		assertAllFoldersAndFilesAreExtracted(extractedFolder);
		assertZipFileStillExists(zipFile);
		assertFalse(existingFileInTargetFolder.exists());
	}

	private void givenTargetFolderWithExistingFile(final File extractedFolder, final File existingFileInTargetFolder)
			throws IOException {
		FileUtils.deleteDirectory(extractedFolder);
		FileUtils.forceMkdir(extractedFolder);
		existingFileInTargetFolder.createNewFile();
		assertTrue(extractedFolder.exists());
		assertTrue(existingFileInTargetFolder.exists());
	}

	private void assertAllFoldersAndFilesAreExtracted(final File extractedFolder) {
		assertTrue(extractedFolder.exists());
		assertTrue(new File(extractedFolder, "aSubFolder/anotherFile.txt").exists());
		assertTrue(new File(extractedFolder, "anotherSubFolder").exists());
		assertTrue(new File(extractedFolder, "aFile.txt").exists());
	}

	private void assertZipFileStillExists(final File zipFile) {
		assertTrue(zipFile.exists());
	}

}
