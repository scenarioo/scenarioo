package org.scenarioo.rest.diffViewer;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.BranchAliasResolver;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.concurrent.Future;

@Path("/rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}")
public class ComparisonsResource {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Queues a comparison calculation as specified. This allows the calculation of comparisons that are not
	 * in the comparisons configuration. branchName can be a branch alias.
	 *
	 * @return
	 * 404 NOT FOUND if the build specified by branchName/buildName does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@POST
	@Path("/calculate")
	@Produces({"application/json"})
	public Response calculate(
		@PathParam("branchName") final String branchName,
		@PathParam("buildName") final String buildName,
		@PathParam("comparisonName") final String comparisonName,
		BuildIdentifier comparisonBuildIdentifier) {

		BuildIdentifier buildIdentifier = resolveAndCreateBuildIdentifier(branchName, buildName);
		checkBuildIsSuccessfullyImported(branchName, buildName, buildIdentifier);

		ScenarioDocuBuildsManager.INSTANCE.submitBuildForSingleComparison(buildIdentifier,
			comparisonBuildIdentifier, comparisonName);

		return Response.ok().build();
	}

	/**
	 * Returns the calculation status as a string. This allows the consumer to poll until the calculation is done.
	 *
	 * @return
	 * 404 NOT FOUND if the build specified by branchName/buildName or the comparison does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@GET
	@Path("/calculationStatus")
	@Produces({"text/plain"})
	public Response status(
		@PathParam("branchName") final String branchName,
		@PathParam("buildName") final String buildName,
		@PathParam("comparisonName") final String comparisonName) {

		BuildDiffInfo buildDiffInfo = getComparisonCalculation(branchName, buildName, comparisonName);
		String status = buildDiffInfo.getComparisonCalculationStatus().toString();
		return Response.ok(status).build();
	}

	/**
	 * Returns the calculation object. It includes the status and in case of successful calculation also the result.
	 *
	 * @return
	 * 404 NOT FOUND if the build specified by branchName/buildName or the comparison does not exist.
	 * 412 PRECONDITION FAILED if the build is not imported successfully.
	 * 200 OK otherwise (does not indicate successful comparison calculation, as this happens asynchronously)
	 */
	@GET
	@Produces({"application/json"})
	public Response calculation(
		@PathParam("branchName") final String branchName,
		@PathParam("buildName") final String buildName,
		@PathParam("comparisonName") final String comparisonName) {

		BuildDiffInfo buildDiffInfo = getComparisonCalculation(branchName, buildName, comparisonName);
		return Response.ok(buildDiffInfo).build();
	}

	/**
	 * Imports the build (if not imported yet) and then calculates the comparisons synchronously. Be aware that this
	 * call can be very slow when comparing large builds.
	 * <p>
	 * Previously this endpoint was:
	 * /rest/builds/importBuild/{branchName}/{buildName}/{comparisonBranchName}/{comparisonBuildName}/{comparisonName}
	 * <p>
	 * Set branchName and buildName for the build that should be imported and compared with the comparison build.
	 * branchName can be an alias, but buildName can't.
	 * <p>
	 * comparisonBranchName and comparisonBuildName must be the names of an existing build that is already imported or
	 * scheduled for import. Import for this build is not triggered by this endpoint. You can use aliases for these two
	 * values though.
	 */
	@POST
	@Path("importAndCompare")
	@Produces({"application/xml", "application/json"})
	public Response importAndCompare(
		@PathParam("branchName") final String branchName,
		@PathParam("buildName") final String buildName,
		@PathParam("comparisonName") final String comparisonName,
		BuildIdentifier comparisonBuildIdentifier) {

		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		BuildIdentifier buildIdentifier = new BuildIdentifier(resolvedBranchName, buildName);
		if(buildFolderDoesNotExist(buildIdentifier)) {
			logger.info("Can't import. Build " + branchName + "/" + buildName + " does not exist.");
			return Response.status(Status.NOT_FOUND).build();
		}

		Future<BuildDiffInfo> buildDiffInfoFuture =
			ScenarioDocuBuildsManager.INSTANCE.importBuildAndCreateComparison(buildIdentifier,
				comparisonBuildIdentifier, comparisonName);

		BuildDiffInfo buildDiffInfo;
		try {
			buildDiffInfo = buildDiffInfoFuture.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return Response.ok(buildDiffInfo).build();
	}

	private BuildDiffInfo getComparisonCalculation(String branchName, String buildName, String comparisonName) {
		BuildIdentifier buildIdentifier = resolveAndCreateBuildIdentifier(branchName, buildName);
		checkBuildIsSuccessfullyImported(branchName, buildName, buildIdentifier);
		return getBuildDiffInfo(branchName, buildName, comparisonName);
	}

	private BuildIdentifier resolveAndCreateBuildIdentifier(@PathParam("branchName") String branchName, @PathParam("buildName") String buildName) {
		String resolvedBranchName = new BranchAliasResolver().resolveBranchAlias(branchName);
		return new BuildIdentifier(resolvedBranchName, buildName);
	}

	private void checkBuildIsSuccessfullyImported(@PathParam("branchName") String branchName, @PathParam("buildName") String buildName, BuildIdentifier buildIdentifier) {
		if (buildFolderDoesNotExist(buildIdentifier)) {
			logger.info("Build " + branchName + "/" + buildName + " does not exist.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		if (buildIsNotSuccessfullyImported(buildIdentifier)) {
			logger.info("Build " + branchName + "/" + buildName + " is not successfully imported.");
			throw new WebApplicationException(Status.PRECONDITION_FAILED);
		}
	}

	private boolean buildIsNotSuccessfullyImported(BuildIdentifier buildIdentifier) {
		BuildImportStatus importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);
		return importStatus == null || !BuildImportStatus.SUCCESS.equals(importStatus);
	}

	private boolean buildFolderDoesNotExist(BuildIdentifier buildIdentifier) {
		FileSystemOperationsDao dao = new FileSystemOperationsDao();
		return !dao.buildFolderExists(buildIdentifier);
	}

	private BuildDiffInfo getBuildDiffInfo(String branchName, String buildName, String comparisonName) {
		try {
			return new DiffReaderXmlImpl().loadBuildDiffInfo(branchName, buildName, comparisonName);
		} catch(Exception e) {
			logger.info("Comparison " + branchName + "/" + buildName + "/" + comparisonName + " does not exist.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

}
