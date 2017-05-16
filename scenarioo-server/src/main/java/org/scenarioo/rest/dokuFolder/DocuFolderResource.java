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

package org.scenarioo.rest.dokuFolder;

import org.apache.log4j.Logger;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/rest/branch/{branchName}/build/{buildName}/documentation/{path}")
public class DocuFolderResource {

	private static final Logger LOGGER = Logger.getLogger(DocuFolderResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile(@PathParam("path") final String path,
							@PathParam("branchName") final String branchName,
							@PathParam("buildName") final String buildName) {

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
			branchName,
			buildName);

		File docuFolder = new File(configurationRepository.getDocumentationDataDirectory().getAbsolutePath() + "/" + buildIdentifier.getBranchName() +"/" + buildIdentifier.getBuildName() + "/docu");
		if (!docuFolder.exists()){
			LOGGER.info("No Documentaion directory found for build "+branchName+" / "+buildName);
			return Response.ok("Not-Found").status(Response.Status.NOT_FOUND).build();
		}

		java.nio.file.Path filePath = docuFolder.toPath().resolve(new File(FilesUtil.decodeName(path)).toPath().normalize().toFile().getPath());
		if (!filePath.toFile().exists()){
			LOGGER.info("Did not find Documentation file "+path);
			return Response.ok("Not-Found").status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok(filePath.toFile(), MediaType.APPLICATION_OCTET_STREAM)
			.header("Content-Disposition", "attachment; filename=\"" + filePath.toFile().getName() + "\"" ) //optional
			.build();
	}

}
