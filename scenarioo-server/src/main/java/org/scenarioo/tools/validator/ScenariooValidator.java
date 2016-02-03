package org.scenarioo.tools.validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Validates a given docu directory by trying to import it.
 * If import (parsing + aggregation) was successful, the produced folder structure and xml files within the given directory are valid.
 * <p/>
 * This can also be used as command line tool in order to validate any scenarioo documentation folder (e.g. produced by another writer API like scenarioo-js)
 */
public class ScenariooValidator {

    private static final Logger LOGGER = Logger.getLogger(ScenariooValidator.class);
    private ValidationBuildImporter validationBuildImporter;
    private File docuDirectory;
    private boolean doCleanDerivedFiles;

    public ScenariooValidator(File docuDirectory, boolean doCleanDerivedFiles) {
        this.docuDirectory = docuDirectory;
        this.doCleanDerivedFiles = doCleanDerivedFiles;
        this.validationBuildImporter = new ValidationBuildImporter(docuDirectory);
    }

    public boolean validate() throws InterruptedException {
        cleanDerivedFilesIfRequested();
        Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = validationBuildImporter.importBuildsAndWaitForExecutor();
        return evaluateSummaries(buildImportSummaries);
    }

    /**
     * will remove all derived files in given docuDirectory (if requested).
     * removes "*.derived.xml"  and "*.derived.log"  files.
     */
    private void cleanDerivedFilesIfRequested() {
        if (!this.doCleanDerivedFiles) {
            return;
        }

        Collection<File> derivedFiles = FileUtils.listFiles(this.docuDirectory, new RegexFileFilter("(.*derived\\.(xml|log))|(.*derived\\.properties)"), TrueFileFilter.INSTANCE);
        LOGGER.info("Cleaning " + derivedFiles.size() + " derived files from docu directory " + this.docuDirectory);

        for (final File file : derivedFiles) {
            if (!file.delete()) {
                LOGGER.error("Can't remove " + file.getAbsolutePath());
            }
        }
    }

    /**
     * @return true if all buildImports were successful. false otherwise
     */
    private boolean evaluateSummaries(Map<BuildIdentifier, BuildImportSummary> buildImportSummaries) {
        boolean allImportsSuccessful = true;

        for (Map.Entry<BuildIdentifier, BuildImportSummary> importSummary : buildImportSummaries.entrySet()) {
            BuildIdentifier identifier = importSummary.getKey();
            BuildImportSummary summary = importSummary.getValue();
            if (summary.getStatus() == BuildImportStatus.FAILED) {
                LOGGER.info(String.format("Imported %s/%s %s\n   %s", identifier.getBranchName(), identifier.getBuildName(), summary.getStatus(), summary.getStatusMessage()));
                allImportsSuccessful = false;
            }
        }

        return allImportsSuccessful;
    }


    /**
     * ---- Main method to make this class available as command line tool ----
     **/

    public static void main(String[] args) throws InterruptedException {

        boolean successful = new ScenariooValidator(getDocuDirectory(args), shouldCleanBefore(args)).validate();

        if (!successful) {
            throw new RuntimeException("Validation failed. See log output for more information!");
        } else {
            LOGGER.info("Validation successful!");
        }
    }

    private static File getDocuDirectory(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please specify the path to the docu directory to validate!");
        }
        return new File(args[0]);
    }

    private static boolean shouldCleanBefore(String[] args) {
        return args.length >= 2 && Boolean.parseBoolean(args[1]);
    }

}
