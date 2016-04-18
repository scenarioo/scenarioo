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
import org.scenarioo.model.docu.aggregates.branches.BuildImportStatus;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.branches.BuildStatistics;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.usecases.ScenarioSummary;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * The aggregator reads the input docu files of a build and generates the aggregated docu files with additional
 * precalculated data (like indexes etc.).
 * 
 * Make sure to adjust the value of {@link ScenarioDocuAggregator#CURRENT_FILE_FORMAT_VERSION} when the format of
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
		return !StringUtils.isBlank(version) && version.equals(ServerVersion.DERIVED_FILE_FORMAT_VERSION);
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
		UseCaseScenariosList useCaseScenariosList = calculateUseCaseScenariosList();
		for (UseCaseScenarios scenarios : useCaseScenariosList.getUseCaseScenarios()) {
			calulateAggregatedDataForUseCase(scenarios);
			addUsecaseToBuildStatistics(scenarios.getUseCase());
		}
		stepsAndPagesAggregator.completeAggregatedPageVariantDataInStepNavigations();

		saveAggregatedBuildStatusInBuildDescriptionIfEmpty();

		dao.saveUseCaseScenariosList(getBuildIdentifier(), useCaseScenariosList);

		objectRepository.calculateAndSaveObjectLists();

		objectRepository.saveCustomObjectTabTrees();

		dao.saveLongObjectNamesIndex(getBuildIdentifier(), longObjectNamesResolver);

		dao.saveVersion(getBuildIdentifier(), ServerVersion.DERIVED_FILE_FORMAT_VERSION);

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
		if (buildStatistics.getNumberOfFailedScenarios() > 0 || buildStatistics.getNumberOfFailedUseCases() > 0) {
			return Status.FAILED;
		} else {
			return Status.SUCCESS;
		}
	}

	private UseCaseScenariosList calculateUseCaseScenariosList() {

		UseCaseScenariosList result = new UseCaseScenariosList();
		List<UseCaseScenarios> useCaseScenarios = new ArrayList<UseCaseScenarios>();
		List<UseCase> usecases = reader.loadUsecases(getBuildIdentifier().getBranchName(), getBuildIdentifier().getBuildName());
		for (UseCase usecase : usecases) {
			UseCaseScenarios useCaseWithScenarios = new UseCaseScenarios();
			List<Scenario> scenarios = reader.loadScenarios(getBuildIdentifier().getBranchName(),
					getBuildIdentifier().getBuildName(), usecase.getName());
			
			boolean atLeastOneScenarioFailed = false;
			for (Scenario scenario : scenarios) {
				if (StringUtils.equals(scenario.getStatus(), FAILED_STATE)) {
					atLeastOneScenarioFailed = true;
					break;
				}
			}
			
			if (usecase.getStatus() == null) {
				usecase.setStatus(atLeastOneScenarioFailed ? FAILED_STATE : SUCCESS_STATE);
			}
			useCaseWithScenarios.setScenarios(createScenarioSummaries(scenarios));
			useCaseWithScenarios.setUseCase(usecase);
			useCaseScenarios.add(useCaseWithScenarios);
		}
		result.setUseCaseScenarios(useCaseScenarios);
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
	
	private void calulateAggregatedDataForUseCase(final UseCaseScenarios useCaseScenarios) {
		
		LOGGER.info("    calculating aggregated data for use case : " + useCaseScenarios.getUseCase().getName());
		
		List<ObjectReference> referencePath = objectRepository.addReferencedUseCaseObjects(useCaseScenarios
				.getUseCase());

		for (ScenarioSummary scenario : useCaseScenarios.getScenarios()) {
			try {
				calculateAggregatedDataForScenario(referencePath, useCaseScenarios.getUseCase(), scenario);
				addScenarioToBuildStatistics(scenario.getScenario());
			} catch (ResourceNotFoundException ex) {
				LOGGER.warn("could not load scenario " + scenario.getScenario().getName() + " in use case"
						+ useCaseScenarios.getUseCase().getName());
			}
		}

		dao.saveUseCaseScenarios(getBuildIdentifier(), useCaseScenarios);
		
		objectRepository.updateAndSaveObjectIndexesForCurrentCase();
	}

	private void addUsecaseToBuildStatistics(final UseCase usecase) {
		String status = usecase.getStatus();
		if (SUCCESS_STATE.equals(status)) {
			buildStatistics.incrementSuccessfulUseCase();
		} else if (FAILED_STATE.equals(status)) {
			buildStatistics.incrementFailedUseCase();
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
	
	private void calculateAggregatedDataForScenario(List<ObjectReference> referencePath, final UseCase usecase,
			final ScenarioSummary scenarioSummary) {
		Scenario scenario = scenarioSummary.getScenario();
		
		referencePath = objectRepository.addReferencedScenarioObjects(referencePath, scenario);
		
		LOGGER.info("      calculating aggregated data for scenario : " + scenario.getName());
		ScenarioPageSteps scenarioPageSteps = calculateAggregatedDataForSteps(usecase, scenario, referencePath);
		
		scenarioSummary.setNumberOfSteps(scenarioPageSteps.getTotalNumberOfStepsInScenario());
		
		dao.saveScenarioPageSteps(getBuildIdentifier(), scenarioPageSteps);
	}
	
	private ScenarioPageSteps calculateAggregatedDataForSteps(final UseCase usecase, final Scenario scenario,
			final List<ObjectReference> referencePath) {
		
		ScenarioPageSteps scenarioPageSteps = new ScenarioPageSteps();
		scenarioPageSteps.setUseCase(usecase);
		scenarioPageSteps.setScenario(scenario);
		List<Step> steps = reader.loadSteps(getBuildIdentifier().getBranchName(), getBuildIdentifier().getBuildName(), usecase.getName(), scenario.getName());
		PageNameSanitizer.sanitizePageNames(steps);
		List<PageSteps> pageStepsList = stepsAndPagesAggregator.calculateScenarioPageSteps(usecase, scenario, steps, referencePath, objectRepository);
		scenarioPageSteps.setPagesAndSteps(pageStepsList);
		
		return scenarioPageSteps;
	}

	public void updateBuildSummary(final BuildLink buildLink) {
		BuildIdentifier buildIdentifier = buildSummary.getIdentifier();
		buildSummary.setBuildDescription(buildLink.getBuild());
		String version = dao.loadVersion(buildIdentifier);
		boolean aggregated = !StringUtils.isBlank(version);
		boolean outdated = aggregated && !version.equals(ServerVersion.DERIVED_FILE_FORMAT_VERSION);
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
