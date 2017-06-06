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

package org.scenarioo.rest.feature;

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
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.features.FeatureScenarios;
import org.scenarioo.model.docu.aggregates.features.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.features.FeatureSummary;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;

@Path("/rest/branch/{branchName}/build/{buildName}/feature/")
public class FeaturesResource {

	private static final Logger LOGGER = Logger.getLogger(FeaturesResource.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	AggregatedDocuDataReader dao = new ScenarioDocuAggregationDao(configurationRepository.getDocumentationDataDirectory());


	@GET
	@Produces({ "application/xml", "application/json" })
	public List<FeatureSummary> loadFeatureSummaries(@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName) {
		LOGGER.info("REQUEST: loadFeatureSummaryList(" + branchName + ", " + buildName + ")");
		final List<FeatureSummary> result = new LinkedList<FeatureSummary>();

		final BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
				branchName,
				buildName);

		final List<FeatureScenarios> featureScenariosList = dao.loadFeatureScenariosList(buildIdentifier);


		for (final FeatureScenarios featureScenarios : featureScenariosList) {

			for (ScenarioSummary scenarioSummary: featureScenarios.getScenarios()){
				ScenarioIdentifier scenarioIdentifier = new ScenarioIdentifier(buildIdentifier, featureScenarios.getFeature().id, scenarioSummary.getScenario().getName());
				ScenarioPageSteps pageSteps = dao.loadScenarioPageSteps(scenarioIdentifier);

				scenarioSummary.pageSteps = pageSteps;
			}
			result.add(mapSummary(featureScenarios));
		}

		return loadTree(result);
	}



	public List<FeatureSummary> loadTree(List<FeatureSummary> features){
		List<FeatureSummary> rootFeatures = new ArrayList<>();
		HashSet<FeatureSummary> featureClear = new HashSet<>();
		for (FeatureSummary feature: features){
			for (String featureName : feature.featureNames){
				System.out.println(featureName);
				FeatureSummary clear = getFor(featureName, features);
				feature.features.add(clear);
				featureClear.add(clear);
			}
		}
		features.removeAll(featureClear);
		rootFeatures.addAll(features);
		return rootFeatures;
	}

	private FeatureSummary getFor(String featureName, List<FeatureSummary> features) {
		for (FeatureSummary feature:features){
			if (feature.id == null) continue;
			if (feature.id.equals(featureName))
				return feature;
		}
		return null;
	}

	private FeatureSummary mapSummary(final FeatureScenarios featureScenarios) {
		final FeatureSummary summary = new FeatureSummary(featureScenarios.getFeature());
		summary.scenarios = featureScenarios.getScenarios();
		summary.setNumberOfScenarios(featureScenarios.getScenarios().size());
		return summary;
	}

}
