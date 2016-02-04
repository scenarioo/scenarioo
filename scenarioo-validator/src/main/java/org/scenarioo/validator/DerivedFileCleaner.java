package org.scenarioo.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DerivedFileCleaner {

    private static final Logger LOGGER = Logger.getLogger(DerivedFileCleaner.class);

    /**
     * Removes all derived files and directories in given docuDirectory.
     * i.e. removes files like "*.derived.xml", "*.derived.log", "version.derived.properties", etc.
     */
    static void cleanDerivedFiles(File rootDirectory) {

        List<File> derivedFilesAndDirectories = getDerivedFilesAndDirectories(rootDirectory);

        LOGGER.info(String.format("Cleaning %d derived files and directories from docu directory %s", derivedFilesAndDirectories.size(), rootDirectory));

        for (final File file : derivedFilesAndDirectories) {
            removeFileOrDirectory(file);
        }
    }

    private static List<File> getDerivedFilesAndDirectories(File rootDirectory) {
        // Note: it would be sufficient to delete "version.derived.properties". But we like to start with a clean slate.
        final RegexFileFilter regexFileFilter = new RegexFileFilter(".*derived.*");
        Collection<File> derivedFiles = FileUtils.listFiles(rootDirectory, regexFileFilter, TrueFileFilter.INSTANCE);
        Collection<File> derivedDirectories = FileUtils.listFilesAndDirs(rootDirectory, FalseFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        CollectionUtils.filter(derivedDirectories, new Predicate<File>() {
            @Override
            public boolean evaluate(File file) {
                return regexFileFilter.accept(file);
            }
        });

        ArrayList<File> fileList = new ArrayList<File>(derivedFiles);
        fileList.addAll(derivedDirectories);
        return fileList;
    }

    private static void removeFileOrDirectory(File file) {
        if (file.isDirectory()) {
            // if given file is a directory, remove it recursively!
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                LOGGER.error("Can't remove directory " + file.getAbsolutePath());
            }
        } else if (file.delete()) {
            LOGGER.trace("Removed " + file.getAbsolutePath());
        } else {
            LOGGER.error("Can't remove file " + file.getAbsolutePath());
        }
    }

}
