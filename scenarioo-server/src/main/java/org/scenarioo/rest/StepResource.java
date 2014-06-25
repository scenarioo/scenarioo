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

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.dao.configuration.ConfigurationDAO;
import org.scenarioo.model.StepIdentifier;
import org.scenarioo.model.docu.aggregates.branches.BuildIdentifier;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.rest.dto.StepDto;
import org.scenarioo.rest.exceptions.PageOccurrenceDoesNotExistException;
import org.scenarioo.rest.exceptions.StepInPageOccurrenceDoesNotExistException;

@Path("/rest/branch/{branchName}/build/{buildName}/usecase/{usecaseName}/scenario/{scenarioName}/pageName/{pageName}/pageOccurrence/{pageOccurrence}/stepInPageOccurrence/{stepInPageOccurrence}")
public class StepResource {

	private static final Logger LOGGER = Logger.getLogger(StepResource.class);

	private final ScenarioDocuReader docuDAO = new ScenarioDocuReader(
			ConfigurationDAO.getDocuDataDirectoryPath());

	private final ScenarioDocuAggregationDAO aggregationsDAO = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());

	/**
	 * Get a step with all its data (meta data, html, ...) together with
	 * additional calculated navigation data
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	public StepDto loadStep(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName,
			@PathParam("usecaseName") final String usecaseName,
			@PathParam("scenarioName") final String scenarioName,
			@PathParam("pageName") final String pageName,
			@PathParam("pageOccurrence") final int pageOccurrence,
			@PathParam("stepInPageOccurrence") final int stepInPageOccurrence) {

		StepIdentifier stepIdentifier = new StepIdentifier(branchName,
				buildName, usecaseName, scenarioName, pageName, pageOccurrence,
				stepInPageOccurrence);

		LOGGER.info("loadStep(" + stepIdentifier + ")");

		String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE
				.resolveAliasBuildName(branchName, buildName);

		ScenarioPageSteps scenarioPagesAndSteps = aggregationsDAO
				.loadScenarioPageSteps(branchName, resolvedBuildName,
						usecaseName, scenarioName);

		int stepIndex = resolveStepIndex(scenarioPagesAndSteps, stepIdentifier);

		Step step = docuDAO.loadStep(branchName, resolvedBuildName,
				usecaseName, scenarioName, stepIndex);
		StepNavigation navigation = aggregationsDAO.loadStepNavigation(
				new BuildIdentifier(branchName, resolvedBuildName),
				usecaseName, scenarioName, stepIndex);
		StepStatistics statistics = scenarioPagesAndSteps.getStepStatistics(
				pageName, pageOccurrence);
		return new StepDto(step, navigation, statistics);
	}

	private int resolveStepIndex(final ScenarioPageSteps scenarioPagesAndSteps,
			final StepIdentifier stepIdentifier) {
		if (scenarioPagesAndSteps == null) {
			throw new RuntimeException(
					"resolveStepIndex: scenarioPagesAndSteps is null");
		}

		int occurrence = 0;

		for (PageSteps pageWithSteps : scenarioPagesAndSteps.getPagesAndSteps()) {
			if (isCorrectPage(pageWithSteps, stepIdentifier.getPageName())) {
				if (occurrence == stepIdentifier.getPageOccurrence()) {
					return resolveStepInPageOccurrence(pageWithSteps,
							stepIdentifier);
				}
				occurrence++;
			}
		}

		throw new PageOccurrenceDoesNotExistException(stepIdentifier);
	}

	private int resolveStepInPageOccurrence(final PageSteps pageWithSteps,
			final StepIdentifier stepIdentifier) {
		if (stepIdentifier.getStepInPageOccurrence() < pageWithSteps.getSteps()
				.size()) {
			return pageWithSteps.getSteps()
					.get(stepIdentifier.getStepInPageOccurrence()).getIndex();
		}

		throw new StepInPageOccurrenceDoesNotExistException(stepIdentifier);
	}

	private boolean isCorrectPage(final PageSteps pageWithSteps,
			final String pageName) {
		return pageName.equals(pageWithSteps.getPage().getName());
	}

}