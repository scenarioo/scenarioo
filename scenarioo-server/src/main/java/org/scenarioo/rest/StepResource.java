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
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.steps.StepWithNavigation;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;

@Path("/rest/branches/{branchName}/builds/{buildName}/usecases/{usecaseName}/scenarios/{scenarioName}/steps/")
public class StepResource {
	
	private final ScenarioDocuReader docuDAO = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
	
	private final ScenarioDocuAggregationDAO aggregationsDAO = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());
	
	/**
	 * Get a step with all its data (meta data, html, ...) together with additional calculated navigation data
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{stepIndex}/")
	public StepWithNavigation loadStepWithNavigationData(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName, @PathParam("stepIndex") final int stepIndex) {
		
		String resolvedBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBranchName(branchName);
		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE.resolveAliasBuildName(resolvedBranchName, buildName);
		
		Step step = docuDAO.loadStep(resolvedBranchName, resolvedBuildName, usecaseName, scenarioName, stepIndex);
		StepNavigation navigation = aggregationsDAO.loadStepNavigation(new BuildIdentifier(resolvedBranchName,
				resolvedBuildName),
				usecaseName, scenarioName, stepIndex);
		
		Scenario scenario = docuDAO.loadScenario(resolvedBranchName, resolvedBuildName, usecaseName, scenarioName);
		UseCase usecase = docuDAO.loadUsecase(resolvedBranchName, resolvedBuildName, usecaseName);
		
		return new StepWithNavigation(step, navigation, usecase.getLabels(), scenario.getLabels());
	}
}