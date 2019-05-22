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

package org.scenarioo.rest.usecase;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseSummary;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/rest/branch/{branchName}/build/{buildName}/usecase")
public class UseCasesResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private AggregatedDocuDataReader dao = new ScenarioDocuAggregationDao(configurationRepository.getDocumentationDataDirectory());

	/**
	 * Lightweight call, which does not send all scenario information.
	 */
	@GetMapping
	public List<UseCaseSummary> loadUseCaseSummaries(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName) {
		final List<UseCaseSummary> result = new LinkedList<>();

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				branchName,
				buildName);

		final List<UseCaseScenarios> useCaseScenariosList = dao.loadUseCaseScenariosList(buildIdentifier);

		for (final UseCaseScenarios useCaseScenarios : useCaseScenariosList) {
			result.add(mapSummary(useCaseScenarios));
		}

		return result;
	}

	private UseCaseSummary mapSummary(final UseCaseScenarios useCaseScenarios) {
		final UseCaseSummary summary = new UseCaseSummary();
		final UseCase useCase = useCaseScenarios.getUseCase();
		summary.setName(useCase.getName());
		summary.setDescription(useCase.getDescription());
		summary.setStatus(useCase.getStatus());
		summary.setNumberOfScenarios(useCaseScenarios.getScenarios().size());
		summary.setLabels(useCase.getLabels());
		return summary;
	}

}
