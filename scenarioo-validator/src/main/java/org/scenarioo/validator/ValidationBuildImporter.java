package org.scenarioo.validator;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.AvailableBuildsList;
import org.scenarioo.business.builds.BuildImporter;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationBuildImporter {

    private static final Logger LOGGER = Logger.getLogger(ScenariooValidator.class);

    public ValidationBuildImporter(File docuDirectory) {
        // various components need the configurationRepository, so let's initialize it with our docuDirectory.
        RepositoryLocator.INSTANCE.initializeConfigurationRepositoryForUnitTest(docuDirectory);
    }

    /**
     * Imports builds in given directory.
     * Will wait until asyncExecutor is done!
     *
     * @throws InterruptedException
     */
    public Map<BuildIdentifier, BuildImportSummary> importBuildsAndWaitForExecutor() throws InterruptedException {
        AvailableBuildsList availableBuilds = new AvailableBuildsList();
        BuildImporter buildImporter = new BuildImporter();

        LOGGER.info("Updating the list of available builds and their states ...");
        List<BranchBuilds> branchBuildsList = ScenarioDocuBuildsManager.loadBranchBuildsList();
        buildImporter.updateBuildImportStates(branchBuildsList, ScenarioDocuBuildsManager.loadBuildImportSummaries());
        availableBuilds.updateBuildsWithSuccessfullyImportedBuilds(branchBuildsList,
                buildImporter.getBuildImportSummaries());

        int numberOfBuilds = buildImporter.getBuildImportSummaries().size();
        int numberOfBuildsToImport = buildImporter.submitUnprocessedBuildsForImport(availableBuilds);

        if (numberOfBuildsToImport != numberOfBuilds) {
            LOGGER.warn("The given directory to validate contains builds that were already tested.\nBuilds total: " + numberOfBuilds + ", builds to import: " + numberOfBuildsToImport);
        }

        if (numberOfBuildsToImport == 0) {
            LOGGER.warn("No builds get validated. You can clear 'derived' files from your docu directory before. Use -c (--clean-derived) command line option.");
            return new HashMap<BuildIdentifier, BuildImportSummary>();
        }

        // Shutsown and wait for asynch imports to be done
		buildImporter.shutdownAndAwaitTerminationOfRunningImportJob();
        return buildImporter.getBuildImportSummaries();
    }


}
