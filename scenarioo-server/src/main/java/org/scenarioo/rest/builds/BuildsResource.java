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
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.business.uploadBuild.BuildUploader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.dao.basic.FileSystemOperationsDao;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.FileResponseCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * This path has a security constraint for POST requests (see web.xml).
 * Only authenticated users with the required role can post new builds.
 */
@RestController
@RequestMapping("/rest/builds")
public class BuildsResource {

	private static final Logger LOGGER = Logger.getLogger(BuildsResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	@GetMapping("updateAndImport")
	public void updateAllBuildsAndSubmitNewBuildsForImport() {
		ScenarioDocuBuildsManager.INSTANCE.updateBuildsIfValidDirectoryConfigured();
	}

	@GetMapping("buildImportSummaries")
	public List<BuildImportSummary> listImportedBuilds() {
		return ScenarioDocuBuildsManager.INSTANCE.getBuildImportSummaries();
	}

	@GetMapping(value = "importLogs/{branchName}/{buildName}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity loadBuildImportLog(@PathVariable("branchName") final String branchName,
											 @PathVariable("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);

		ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());
		File logFile = dao.getBuildImportLogFile(buildIdentifier);
		if (logFile == null || !logFile.exists()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		return FileResponseCreator.createLogFileResponse(logFile);
	}

	/**
	 * Queues the import / reimport of the specified build and then calculates the configured comparisons.
	 */
	@GetMapping("{branchName}/{buildName}/import")
	public ResponseEntity importBuild(@PathVariable("branchName") final String branchName,
									  @PathVariable("buildName") final String buildName) {

		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(branchName);
		BuildIdentifier buildIdentifier = new BuildIdentifier(resolvedBranchName, buildName);
		if (buildFolderDoesNotExist(buildIdentifier)) {
			LOGGER.info("Can't import. Build " + branchName + "/" + buildName + " does not exist.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		ScenarioDocuBuildsManager.INSTANCE.reimportBuild(buildIdentifier);

		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "{branchName}/{buildName}/importStatus", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity importStatusString(@PathVariable("branchName") final String branchName,
											 @PathVariable("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);
		BuildImportStatus importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);

		if (importStatus == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(importStatus);
	}

	@GetMapping("{branchName}/{buildName}/importStatus")
	public ResponseEntity importStatusJson(@PathVariable("branchName") final String branchName,
										   @PathVariable("buildName") final String buildName) {

		BuildIdentifier buildIdentifier = new BuildIdentifier(branchName, buildName);
		BuildImportStatus importStatus = ScenarioDocuBuildsManager.INSTANCE.getImportStatus(buildIdentifier);

		if (importStatus == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(new ImportStatus(importStatus.name()));
	}

	private boolean buildFolderDoesNotExist(BuildIdentifier buildIdentifier) {
		FileSystemOperationsDao dao = new FileSystemOperationsDao();
		return !dao.buildFolderExists(buildIdentifier);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity uploadBuildAsZipFile(@RequestBody final MultipartFile formData) {
		return new BuildUploader().uploadBuild(formData);
	}

}
