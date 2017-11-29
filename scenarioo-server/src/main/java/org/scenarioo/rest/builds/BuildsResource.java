/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.rest.builds;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.business.uploadBuild.BuildUploader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.diffViewer.ComparisonResult;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

/**
 * This path has a security constraint for POST requests (see web.xml).
 * Only authenticated users with the required role can post new builds.
 */
@Path("/rest/builds/")
public class BuildsResource {

	private static final Logger LOGGER = Logger.getLogger(BuildsResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	@GET
	@Path("updateAndImport")
	@Produces({"application/xml", "application/json"})
	public void updateAllBuildsAndSubmitNewBuildsForImport() {
		ScenarioDocuBuildsManager.INSTANCE.updateBuildsIfValidDirectoryConfigured();
	}

	@GET
	@Path("buildImportSummaries")
	@Produces({"application/xml", "application/json"})
	public List<BuildImportSummary> listImportedBuilds() {
		return ScenarioDocuBuildsManager.INSTANCE.getBuildImportSummaries();
	}

	@GET
	@Path("importLogs/{branchName}/{buildName}")
	@Produces({"text/plain"})
	public Response loadBuildImportLog(@PathParam("branchName") final String branchName,
									   @PathParam("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);

		ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());
		File logFile = dao.getBuildImportLogFile(buildIdentifier);
		if (logFile == null || !logFile.exists()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		ResponseBuilder response = Response.ok(logFile);
		response.header("Content-Disposition", "attachment; filename=\"" + logFile + "\"");
		return response.build();
	}

	/**
	 * Imports the specified build and then calculates the configured comparisons.
	 * Reimports the build if it's already imported.
	 */
	@GET
	@Path("{branchName}/{buildName}/import")
	@Produces({"application/xml", "application/json"})
	public void importBuild(@PathParam("branchName") final String branchName,
							@PathParam("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);

		ScenarioDocuBuildsManager.INSTANCE.reimportBuild(buildIdentifier);
	}

	@GET
	@Path("{branchName}/{buildName}/importStatus")
	@Produces({"text/plain", "application/xml"})
	public Response importStatusString(@PathParam("branchName") final String branchName,
									   @PathParam("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);
		String importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);

		if (importStatus == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(importStatus).build();
	}

	@GET
	@Path("{branchName}/{buildName}/importStatus")
	@Produces({"application/json"})
	public Response importStatusJson(@PathParam("branchName") final String branchName,
									 @PathParam("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);
		String importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);

		if (importStatus == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(new ImportStatus(importStatus)).build();
	}

	/**
	 * Imports the build (if not imported yet) and then calculates the comparisons synchronously.
	 * <p>
	 * Previously this was:
	 * /rest/builds/importBuild/{branchName}/{buildName}/{comparisonBranchName}/{comparisonBuildName}/{comparisonName}
	 * <p>
	 * Set branchName and buildName for the build that should be imported and compared with the comparison build.
	 * <p>
	 * comparisonBranchName and comparisonBuildName must be the names of an existing build that is already imported or
	 * scheduled for import. Import for this build is not triggered by this endpoint. You can use aliases for these two
	 * values though.
	 */
	@GET
	@Path("{branchName}/{buildName}/comparisons/{comparisonBranchName}/{comparisonBuildName}/{comparisonName}/importAndCompare")
	@Produces({"application/xml", "application/json"})
	public Response importAndCompare(
		@PathParam("branchName") final String branchName,
		@PathParam("buildName") final String buildName,
		@PathParam("comparisonBranchName") final String comparisonBranchName,
		@PathParam("comparisonBuildName") final String comparisonBuildName,
		@PathParam("comparisonName") final String comparisonName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);
		BuildIdentifier comparisonBuildIdentifier = new BuildIdentifier(comparisonBranchName, comparisonBuildName);

		Future<ComparisonResult> comparisonResultFuture =
			ScenarioDocuBuildsManager.INSTANCE.importBuildAndCreateComparison(buildIdentifier,
				comparisonBuildIdentifier, comparisonName);

		ComparisonResult comparisonResult;
		try {
			comparisonResult = comparisonResultFuture.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return Response.ok(comparisonResult).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({"application/xml", "application/json"})
	public Response uploadBuildAsZipFile(final MultipartFormDataInput formData) {
		return new BuildUploader().uploadBuild(formData);
	}

}
