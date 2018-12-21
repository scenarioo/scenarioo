package org.scenarioo.rest.diffViewer;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.BranchAliasResolver;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.FileResponseCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}")
public class ComparisonsResource {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Queues a comparison calculation as specified. This allows the calculation of comparisons that are not
	 * in the comparisons configuration. branchName can be a branch alias.
	 *
	 * @return 404 NOT FOUND if the build specified by branchName/buildName does not exist.
	 * 412 PRECONDITION FAILED if the build was not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@PostMapping("/calculate")
	public ResponseEntity calculate(
		@PathVariable("branchName") final String branchName,
		@PathVariable("buildName") final String buildName,
		@PathVariable("comparisonName") final String comparisonName,
		@RequestBody final BuildIdentifier comparisonBuildIdentifier) {

		BuildIdentifier buildIdentifier = resolveAndCreateBuildIdentifier(branchName, buildName);
		checkBuildIsSuccessfullyImported(branchName, buildName, buildIdentifier);

		ScenarioDocuBuildsManager.INSTANCE.submitBuildForSingleComparison(buildIdentifier,
			comparisonBuildIdentifier, comparisonName);

		return ResponseEntity.ok().build();
	}

	/**
	 * Queues a comparison calculation of a comparison that already was calculated once, to recalculate it when needed.
	 *
	 * @return 404 NOT FOUND if the specified build or comparison does not exist
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@PostMapping("/recalculate")
	public ResponseEntity recalculate(
		@PathVariable("branchName") final String branchName,
		@PathVariable("buildName") final String buildName,
		@PathVariable("comparisonName") final String comparisonName) {

		BuildIdentifier buildIdentifier = resolveAndCreateBuildIdentifier(branchName, buildName);
		checkBuildIsSuccessfullyImported(branchName, buildName, buildIdentifier);

		BuildDiffInfo buildDiffInfo = getComparisonCalculation(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(), comparisonName);
		BuildIdentifier comparisonBuildIdentifier = buildDiffInfo.getCompareBuild();

		ScenarioDocuBuildsManager.INSTANCE.submitBuildForSingleComparison(buildIdentifier,
			comparisonBuildIdentifier, comparisonName);

		return ResponseEntity.ok().build();
	}

	/**
	 * Returns the calculation status as a string. This allows the consumer to poll until the calculation is done.
	 *
	 * @return 404 NOT FOUND if the build specified by branchName/buildName or the comparison does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@GetMapping(path = "/calculationStatus", produces = "text/plain")
	public ResponseEntity getCalculationStatus(
		@PathVariable("branchName") final String branchName,
		@PathVariable("buildName") final String buildName,
		@PathVariable("comparisonName") final String comparisonName) {

		BuildDiffInfo buildDiffInfo = getComparisonCalculation(branchName, buildName, comparisonName);
		String status = buildDiffInfo.getStatus().toString();
		return ResponseEntity.ok(status);
	}


	/**
	 * Returns the calculation status as a string. This allows the consumer to poll until the calculation is done.
	 *
	 * @return 404 NOT FOUND if the build specified by branchName/buildName or the comparison does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@GetMapping(path = "/log", produces = "text/plain")
	public ResponseEntity getLog(@PathVariable("branchName") final String branchName,
						   @PathVariable("buildName") final String buildName,
						   @PathVariable("comparisonName") final String comparisonName) {

		DiffViewerDao diffViewerDao = new DiffViewerDao();
		File logFile = diffViewerDao.getBuildComparisonLogFile(branchName, buildName, comparisonName);
		if (logFile == null || !logFile.exists()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return FileResponseCreator.createLogFileResponse(logFile);
	}


	/**
	 * Returns the calculation object. It includes the status and in case of successful calculation also the result.
	 *
	 * @return 404 NOT FOUND if the build specified by branchName/buildName or the comparison does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@GetMapping
	public ResponseEntity getCalculation(
		@PathVariable("branchName") final String branchName,
		@PathVariable("buildName") final String buildName,
		@PathVariable("comparisonName") final String comparisonName) {

		BuildDiffInfo buildDiffInfo = getComparisonCalculation(branchName, buildName, comparisonName);
		return ResponseEntity.ok(buildDiffInfo);
	}

	/**
	 * Imports the build (if not imported yet) and then calculates the comparison synchronously. Be aware that this
	 * call can be very slow when comparing large builds.
	 * <p>
	 * Set branchName and buildName for the build that should be imported and compared with the comparison build.
	 * branchName can be an alias, but buildName can't.
	 * <p>
	 * comparisonBranchName and comparisonBuildName must be the names of an existing build that is already imported or
	 * scheduled for import. Import for this build is not triggered by this endpoint. You can use aliases for these two
	 * values though.
	 */
	@PostMapping("importAndCompare")
	public ResponseEntity importAndCompare(
		@PathVariable("branchName") final String branchName,
		@PathVariable("buildName") final String buildName,
		@PathVariable("comparisonName") final String comparisonName,
		@RequestBody BuildIdentifier comparisonBuildIdentifier) {

		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		BuildIdentifier buildIdentifier = new BuildIdentifier(resolvedBranchName, buildName);
		if (buildFolderDoesNotExist(buildIdentifier)) {
			logger.info("Can't import. Build " + branchName + "/" + buildName + " does not exist.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Future<Future<BuildDiffInfo>> buildDiffInfoFuture =
			ScenarioDocuBuildsManager.INSTANCE.importBuildIfNewAndScheduleHiPrioComparison(buildIdentifier,
				comparisonBuildIdentifier, comparisonName);

		BuildDiffInfo buildDiffInfo;
		try {
			buildDiffInfo = buildDiffInfoFuture.get().get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.ok(buildDiffInfo);
	}

	private BuildDiffInfo getComparisonCalculation(String branchName, String buildName, String comparisonName) {
		BuildIdentifier buildIdentifier = resolveAndCreateBuildIdentifier(branchName, buildName);
		checkBuildIsSuccessfullyImported(branchName, buildName, buildIdentifier);
		return getBuildDiffInfo(branchName, buildName, comparisonName);
	}

	private BuildIdentifier resolveAndCreateBuildIdentifier(@PathVariable("branchName") String branchName, @PathVariable("buildName") String buildName) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		return new BuildIdentifier(resolvedBranchName, buildName);
	}

	private void checkBuildIsSuccessfullyImported(@PathVariable("branchName") String branchName, @PathVariable("buildName") String buildName, BuildIdentifier buildIdentifier) {
		if (buildFolderDoesNotExist(buildIdentifier)) {
			logger.info("Build " + branchName + "/" + buildName + " does not exist.");
			throw new NotFoundException();
		}
		if (buildIsNotSuccessfullyImported(buildIdentifier)) {
			logger.info("Build " + branchName + "/" + buildName + " is not successfully imported.");
			throw new PreconditionFailedException();
		}
	}

	private boolean buildIsNotSuccessfullyImported(BuildIdentifier buildIdentifier) {
		BuildImportStatus importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);
		return !BuildImportStatus.SUCCESS.equals(importStatus);
	}

	private boolean buildFolderDoesNotExist(BuildIdentifier buildIdentifier) {
		FileSystemOperationsDao dao = new FileSystemOperationsDao();
		return !dao.buildFolderExists(buildIdentifier);
	}

	private BuildDiffInfo getBuildDiffInfo(String branchName, String buildName, String comparisonName) {
		DiffViewerDao diffViewerDao = new DiffViewerDao();
		try {
			return diffViewerDao.loadBuildDiffInfo(branchName, buildName, comparisonName);
		} catch (Exception e) {
			logger.error("Could not load build diff info for  " + branchName + "/" + buildName + "/" + comparisonName + ".", e);
			throw new NotFoundException();
		}
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	private class NotFoundException extends RuntimeException {}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	private class PreconditionFailedException extends RuntimeException {}

}
