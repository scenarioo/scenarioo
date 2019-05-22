package org.scenarioo.validator;

import org.apache.log4j.Logger;
import org.scenarioo.dao.version.ApplicationVersionHolder;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.util.Map;

/**
 * Validates a given docu directory by trying to import it.
 * If import (parsing + aggregation) was successful, the folder structure and xml files within the given directory are valid.
 */
public class ScenariooValidator {

    private static final Logger LOGGER = Logger.getLogger(ScenariooValidator.class);
    private ValidationBuildImporter validationBuildImporter;
    private File docuDirectory;
    private boolean doCleanDerivedFiles;

    public ScenariooValidator(File docuDirectory, boolean doCleanDerivedFiles) {
		ApplicationVersionHolder.INSTANCE.initializeFromClassContext();
        this.docuDirectory = docuDirectory;
        this.doCleanDerivedFiles = doCleanDerivedFiles;
        this.validationBuildImporter = new ValidationBuildImporter(docuDirectory);
    }

    public boolean validate() throws InterruptedException {
        cleanDerivedFilesIfRequested();
        Map<BuildIdentifier, BuildImportSummary> buildImportSummaries = validationBuildImporter.importBuildsAndWaitForExecutor();
        return evaluateSummaries(buildImportSummaries);
    }

    private void cleanDerivedFilesIfRequested() {
        if (this.doCleanDerivedFiles) {
            DerivedFileCleaner.cleanDerivedFiles(this.docuDirectory);
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

}
