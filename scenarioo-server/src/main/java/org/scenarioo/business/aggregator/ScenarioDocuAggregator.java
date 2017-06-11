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

package org.scenarioo.business.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.dao.search.FullTextSearch;
import org.scenarioo.dao.version.ApplicationVersionHolder;
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.branches.BuildStatistics;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.features.FeatureScenarios;
import org.scenarioo.model.docu.aggregates.features.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.features.FeatureScenariosList;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * The aggregator reads the input docu files of a build and generates the aggregated docu files with additional
 * precalculated data (like indexes etc.).
 *
 * Make sure to adjust the value of `scenariooAggregatedDataFormatVersion` in gradle.build when the format of
 * generated data is extended or changed.
 *
 * TODO #194: Make build import more friendly:<br>
 * Make aggregator more fail safe ... let him continue in case of exceptions or unexpected data (null pointers?) to
 * aggregate at least that part of a documentation build that is okay, such that this part can be accessed and read.
 */
public class ScenarioDocuAggregator {

	private final static Logger LOGGER = Logger.getLogger(ScenarioDocuAggregator.class);

	private static final String SUCCESS_STATE = Status.SUCCESS.getKeyword();

	private static final String FAILED_STATE = Status.FAILED.getKeyword();

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private final String internalFormatVersion = ApplicationVersionHolder.INSTANCE.getApplicationVersion().getAggregatedDataFormatVersion();

	/**
	 * Import summary of the build currently being aggregated. Contains all info
	 * about the build and can be used to store summary information (like
	 * warnings, status, etc.) about the import process and aggregation beeing
	 * executed on it.
	 */
	private final BuildImportSummary buildSummary;

	private final ScenarioDocuReader reader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());

	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();

	private final ScenarioDocuAggregationDao dao = new ScenarioDocuAggregationDao(
			configurationRepository.getDocumentationDataDirectory(), longObjectNamesResolver);

	private final BuildStatistics buildStatistics = new BuildStatistics();

	private StepsAndPagesAggregator stepsAndPagesAggregator;

	private ObjectRepository objectRepository;

	public ScenarioDocuAggregator(final BuildImportSummary buildSummary) {
		this.buildSummary = buildSummary;
	}

	private BuildIdentifier getBuildIdentifier() {
		return buildSummary.getIdentifier();
	}

	public boolean isAggregatedDataForBuildAlreadyAvailableAndCurrentVersion() {
		String version = dao.loadVersion(getBuildIdentifier());
		return !StringUtils.isBlank(version) && version.equals(internalFormatVersion);
	}

	public void removeAggregatedDataForBuild() {
		dao.deleteDerivedFiles(getBuildIdentifier());
		objectRepository = new ObjectRepository(getBuildIdentifier(), dao);
		objectRepository.removeAnyExistingObjectData();
	}

	public void calculateAggregatedDataForBuild() {
		stepsAndPagesAggregator = new StepsAndPagesAggregator(getBuildIdentifier(), dao);

		objectRepository = new ObjectRepository(getBuildIdentifier(), dao);
		objectRepository.removeAnyExistingObjectData();

		LOGGER.info("  calculating aggregated data for build " + getBuildIdentifier() + " ... ");
		FeatureScenariosList featureScenariosList = calculateFeatureScenariosList();

		FullTextSearch fullTextSearch = new FullTextSearch();
		fullTextSearch.indexFeatures(featureScenariosList, getBuildIdentifier());

		for (FeatureScenarios scenarios : featureScenariosList.getFeatureScenarios()) {
			calulateAggregatedDataForFeature(scenarios);
			addFeatureToBuildStatistics(scenarios.getFeature());
		}
		stepsAndPagesAggregator.completeAggregatedPageVariantDataInStepNavigations();

		saveAggregatedBuildStatusInBuildDescriptionIfEmpty();

		dao.saveFeatureScenariosList(getBuildIdentifier(), featureScenariosList);

		objectRepository.calculateAndSaveObjectLists();

		objectRepository.saveCustomObjectTabTrees();

		dao.saveLongObjectNamesIndex(getBuildIdentifier(), longObjectNamesResolver);

		dao.saveVersion(getBuildIdentifier(), internalFormatVersion);

		buildSummary.setBuildStatistics(buildStatistics);
	}

	private void saveAggregatedBuildStatusInBuildDescriptionIfEmpty() {
		Build build = buildSummary.getBuildDescription();
		if (StringUtils.isEmpty(build.getStatus())) {
			build.setStatus(getBuildStatus(buildStatistics));
			dao.saveBuild(getBuildIdentifier().getBranchName(), build);
		}
	}

	private Status getBuildStatus(final BuildStatistics buildStatistics) {
		if (buildStatistics.getNumberOfFailedScenarios() > 0 || buildStatistics.getNumberOfFailedFeatures() > 0) {
			return Status.FAILED;
		} else {
			return Status.SUCCESS;
		}
	}

	private FeatureScenariosList calculateFeatureScenariosList() {

		FeatureScenariosList result = new FeatureScenariosList();
		List<FeatureScenarios> featureScenarios = new ArrayList<FeatureScenarios>();
		List<Feature> features = reader.loadFeatures(getBuildIdentifier().getBranchName(), getBuildIdentifier().getBuildName());
		for (Feature feature : features) {
			FeatureScenarios featureWithScenarios = new FeatureScenarios();
			List<Scenario> scenarios = reader.loadScenarios(getBuildIdentifier().getBranchName(),
					getBuildIdentifier().getBuildName(), feature.getId());

			boolean atLeastOneScenarioFailed = false;
			for (Scenario scenario : scenarios) {
				if (StringUtils.equals(scenario.getStatus(), FAILED_STATE)) {
					atLeastOneScenarioFailed = true;
					break;
				}
			}

			if (feature.getStatus() == null) {
				feature.setStatus(atLeastOneScenarioFailed ? FAILED_STATE : SUCCESS_STATE);
			}
			featureWithScenarios.setScenarios(createScenarioSummaries(scenarios));
			featureWithScenarios.setFeature(feature);
			featureScenarios.add(featureWithScenarios);
		}
		result.setFeatureScenarios(featureScenarios);
		return result;
	}

	private List<ScenarioSummary> createScenarioSummaries(final List<Scenario> scenarios) {
		List<ScenarioSummary> scenarioSummaries = new ArrayList<ScenarioSummary>(scenarios.size());
		for (Scenario scenario : scenarios) {
			scenarioSummaries.add(createSummary(scenario));
		}
		return scenarioSummaries;
	}

	private ScenarioSummary createSummary(final Scenario scenario) {
		ScenarioSummary summary = new ScenarioSummary();
		summary.setScenario(scenario);
		return summary;
	}

	private void calulateAggregatedDataForFeature(final FeatureScenarios featureScenarios) {

		LOGGER.info("    calculating aggregated data for feature : " + featureScenarios.getFeature().getId());

		List<ObjectReference> referencePath = objectRepository.addReferencedFeatureObjects(featureScenarios
				.getFeature());

		for (ScenarioSummary scenario : featureScenarios.getScenarios()) {
			try {
				calculateAggregatedDataForScenario(referencePath, featureScenarios.getFeature(), scenario);
				addScenarioToBuildStatistics(scenario.getScenario());
			} catch (ResourceNotFoundException ex) {
				LOGGER.warn("could not load scenario " + scenario.getScenario().getName() + " in feature"
						+ featureScenarios.getFeature().getId());
			}
		}

		dao.saveFeatureScenarios(getBuildIdentifier(), featureScenarios);

		objectRepository.updateAndSaveObjectIndexesForCurrentCase();
	}

	private void addFeatureToBuildStatistics(final Feature feature) {
		String status = feature.getStatus();
		if (SUCCESS_STATE.equals(status)) {
			buildStatistics.incrementSuccessfulFeature();
		} else if (FAILED_STATE.equals(status)) {
			buildStatistics.incrementFailedFeature();
		}
	}

	private void addScenarioToBuildStatistics(final Scenario scenario) {
		String status = scenario.getStatus();
		if (SUCCESS_STATE.equals(status)) {
			buildStatistics.incrementSuccessfulScenario();
		} else if (FAILED_STATE.equals(status)) {
			buildStatistics.incrementFailedScenario();
		}
	}

	private void calculateAggregatedDataForScenario(List<ObjectReference> referencePath, final Feature feature,
			final ScenarioSummary scenarioSummary) {
		Scenario scenario = scenarioSummary.getScenario();

		referencePath = objectRepository.addReferencedScenarioObjects(referencePath, scenario);

		LOGGER.info("      calculating aggregated data for scenario : " + scenario.getName());
		ScenarioPageSteps scenarioPageSteps = calculateAggregatedDataForSteps(feature, scenario, referencePath);

		scenarioSummary.setNumberOfSteps(scenarioPageSteps.getTotalNumberOfStepsInScenario());

		dao.saveScenarioPageSteps(getBuildIdentifier(), scenarioPageSteps);
	}

	private ScenarioPageSteps calculateAggregatedDataForSteps(final Feature feature, final Scenario scenario,
															  final List<ObjectReference> referencePath) {

		ScenarioPageSteps scenarioPageSteps = new ScenarioPageSteps();
		scenarioPageSteps.setFeature(feature);
		scenarioPageSteps.setScenario(scenario);
		List<Step> steps = reader.loadSteps(getBuildIdentifier().getBranchName(), getBuildIdentifier().getBuildName(), feature.getId(), scenario.getName());
		PageNameSanitizer.sanitizePageNames(steps);
		List<PageSteps> pageStepsList = stepsAndPagesAggregator.calculateScenarioPageSteps(feature, scenario, steps, referencePath, objectRepository);
		scenarioPageSteps.setPagesAndSteps(pageStepsList);

		return scenarioPageSteps;
	}

	public void updateBuildSummary(final BuildLink buildLink) {
		BuildIdentifier buildIdentifier = buildSummary.getIdentifier();
		buildSummary.setBuildDescription(buildLink.getBuild());
		String version = dao.loadVersion(buildIdentifier);
		boolean aggregated = !StringUtils.isBlank(version);
		boolean outdated = aggregated && !version.equals(internalFormatVersion);
		boolean error = buildSummary.getStatus().isFailed();
		if (error) {
			buildSummary.setStatus(BuildImportStatus.FAILED);
		} else if (outdated) {
			buildSummary.setStatus(BuildImportStatus.OUTDATED);
		} else if (aggregated) {
			buildSummary.setStatus(BuildImportStatus.SUCCESS);
		} else {
			buildSummary.setStatus(BuildImportStatus.UNPROCESSED);
		}
	}

	public BuildStatistics getBuildStatistics() {
		return this.buildStatistics;
	}
}
