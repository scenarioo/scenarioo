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

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.scenario.dto.ScenarioDetails;
import org.scenarioo.rest.scenario.mapper.ScenarioDetailsMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/branch/{branchName}/build/{buildName}/usecase/{usecaseName}/scenario/")
public class ScenariosResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory());

	private final ScenarioDetailsMapper scenarioDetailsMapper = new ScenarioDetailsMapper();

	@GetMapping
	public UseCaseScenarios readUseCaseScenarios(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("usecaseName") final String usecaseName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		return aggregatedDataReader.loadUseCaseScenarios(buildIdentifier, usecaseName);
	}

	@GetMapping("{scenarioName}")
	public ScenarioDetails readScenarioWithPagesAndSteps(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("usecaseName") final String usecaseName,
			@PathVariable("scenarioName") final String scenarioName) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);
		ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName);

		ScenarioPageSteps pageSteps = aggregatedDataReader.loadScenarioPageSteps(scenarioIdentifier);

		return scenarioDetailsMapper.map(pageSteps);
	}

}
