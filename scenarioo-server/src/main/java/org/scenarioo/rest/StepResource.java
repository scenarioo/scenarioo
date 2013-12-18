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

package org.scenarioo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.entities.Step;


@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/steps/")
public class StepResource {
	
	private final ScenarioDocuReader filesystem = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
	
	/**
	 * Get a step with all its data (meta data, html, ...)
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{stepIndex}/")
	public Step readScenarioWithPagesAndSteps(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("stepIndex") final int stepIndex) {
		return filesystem.loadStep(branchName, buildName, usecaseName, scenarioName, stepIndex);
	}
	
}