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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.Feature;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;

@Path("/rest/branch/{branchName}/build/{buildName}/usecase/")
public class UseCasesResource {

	private static final Logger LOGGER = Logger.getLogger(UseCasesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	AggregatedDocuDataReader dao = new ScenarioDocuAggregationDao(configurationRepository.getDocumentationDataDirectory());


	@GET
	@Produces({ "application/xml", "application/json" })
	public List<UseCaseSummary> loadUseCaseSummaries(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		LOGGER.info("REQUEST: loadUseCaseSummaryList(" + branchName + ", " + buildName + ")");
		final List<UseCaseSummary> result = new LinkedList<UseCaseSummary>();

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				branchName,
				buildName);

		final List<UseCaseScenarios> useCaseScenariosList = dao.loadUseCaseScenariosList(buildIdentifier);


		for (final UseCaseScenarios useCaseScenarios : useCaseScenariosList) {

			for (ScenarioSummary scenarioSummary: useCaseScenarios.getScenarios()){
				ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, useCaseScenarios.getFeature().name, scenarioSummary.getScenario().getName());
				ScenarioPageSteps pageSteps = dao.loadScenarioPageSteps(scenarioIdentifier);

				scenarioSummary.pageSteps = pageSteps;
			}
			result.add(mapSummary(useCaseScenarios));
		}

		return loadTree(result);
	}



	public List<UseCaseSummary> loadTree(List<UseCaseSummary> features){
		List<UseCaseSummary> rootFeatures = new ArrayList<>();
		HashSet<Feature> featureClear = new HashSet<>();
		for (Feature feature: features){
			for (String featureName : feature.featureNames){
				System.out.println(featureName);
				Feature clear = getFor(featureName, features);
				feature.features.add(clear);
				featureClear.add(clear);
			}
		}
		features.removeAll(featureClear);
		rootFeatures.addAll(features);
		return rootFeatures;
	}

	private Feature getFor(String featureName, List<UseCaseSummary> features) {
		for (Feature feature:features){
			if (feature.folderName == null) continue;
			if (feature.folderName.equals(featureName))
				return feature;
		}
		return null;
	}

	private UseCaseSummary mapSummary(final UseCaseScenarios useCaseScenarios) {
		final UseCaseSummary summary = new UseCaseSummary(useCaseScenarios.getFeature());
		summary.scenarios = useCaseScenarios.getScenarios();
		summary.setNumberOfScenarios(useCaseScenarios.getScenarios().size());
		return summary;
	}

}
