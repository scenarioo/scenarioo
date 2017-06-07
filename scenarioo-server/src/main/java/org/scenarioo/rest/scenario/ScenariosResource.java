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

package org.scenarioo.rest.scenario;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.features.FeatureScenarios;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.scenario.dto.ScenarioDetails;
import org.scenarioo.rest.scenario.mapper.ScenarioDetailsMapper;

@Path("/rest/branch/{branchName}/build/{buildName}/feature/{featureName}/scenario/")
public class ScenariosResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());

	private final ScenarioDetailsMapper scenarioDetailsMapper = new ScenarioDetailsMapper();

	@GET
	@Produces({ "application/xml", "application/json" })
	public FeatureScenarios readFeatureScenarios(@PathParam("branchName") final String branchName,
												 @PathParam("buildName") final String buildName, @PathParam("featureName") final String featureName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		return aggregatedDataReader.loadFeatureScenarios(buildIdentifier, featureName);
	}

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("{scenarioName}/")
	public ScenarioDetails readScenarioWithPagesAndSteps(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName, @PathParam("featureName") final String featureName,
			@PathParam("scenarioName") final String scenarioName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, featureName, scenarioName);

		ScenarioPageSteps pageSteps = aggregatedDataReader.loadScenarioPageSteps(scenarioIdentifier);

		return scenarioDetailsMapper.map(pageSteps);
	}

}
