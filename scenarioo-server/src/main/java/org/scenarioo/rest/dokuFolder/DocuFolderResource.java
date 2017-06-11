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
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Path("/rest/branch/{branchName}/build/{buildName}/documentation")
public class DocuFolderResource {

	private static final Logger LOGGER = Logger.getLogger(DocuFolderResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	@GET
	public Response getFile(@QueryParam("path") final String path,
							@PathParam("branchName") final String branchName,
							@PathParam("buildName") final String buildName,
							@QueryParam("referer") final String referer) {

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
			branchName,
			buildName);

		File docuFolder = new File(configurationRepository.getDocumentationDataDirectory().getAbsolutePath() + "/" + buildIdentifier.getBranchName() +"/" + buildIdentifier.getBuildName() + "/docu");
		if (!docuFolder.exists()){
			LOGGER.info("No Documentaion directory found for build "+branchName+" / "+buildName);
			return null;
		}
		java.nio.file.Path filePath;
		String filePathStr = FilesUtil.decodeName(path);

		filePath = docuFolder.toPath().resolve(filePathStr);
		if (referer!=null) {
			String refererPath = FilesUtil.decodeName(referer);
			if (!refererPath.startsWith("http")){
				int last = refererPath.lastIndexOf('/');
				if (last == -1) last = 0;
				String newPath = refererPath.substring(0, last);
				filePath = docuFolder.toPath().resolve(newPath).resolve(filePathStr);
			}
		}

		if (!filePath.toFile().exists()){
			LOGGER.info("Did not find Documentation file "+path);
			return null;
		}

		if (!filePath.toFile().getAbsolutePath().startsWith(docuFolder.getAbsolutePath())){
			LOGGER.info("Did not find Documentation file "+path+" in docu folder");
			return null;
		}

		File file = filePath.toFile();

		Response.ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=\""+file.getName()+"\"");
		return response.build();
	}
}
