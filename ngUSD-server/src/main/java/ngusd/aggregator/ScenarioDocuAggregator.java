package ngusd.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;
import ngusd.api.ScenarioDocuReader;
import ngusd.api.util.files.ResourceNotFoundException;
import ngusd.dao.ConfigurationDAO;
import ngusd.dao.ScenarioDocuAggregationDAO;
import ngusd.model.docu.aggregates.scenarios.PageSteps;
import ngusd.model.docu.aggregates.scenarios.ScenarioPageSteps;
import ngusd.model.docu.aggregates.usecases.PageVariantsCounter;
import ngusd.model.docu.aggregates.usecases.UseCaseScenarios;
import ngusd.model.docu.aggregates.usecases.UseCaseScenariosList;
import ngusd.model.docu.entities.Page;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.ScenarioCalculatedData;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.StepDescription;
import ngusd.model.docu.entities.StepIdentification;
import ngusd.model.docu.entities.UseCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The aggregator reads the input docu files of a build and generates the aggregated docu files with additional
 * precalculated data (like indexes etc.).
 * 
 * Make sure to adjust the value of {@link ScenarioDocuAggregator#CURRENT_FILE_FORMAT_VERSION} when the format of
 * generated data is extended or changed.
 */
public class ScenarioDocuAggregator {
	
	/**
	 * Version of the file format in filesystem. The data aggregator checks whether the file format is the same,
	 * otherwise the data has to be recalculated.
	 */
	public static final String CURRENT_FILE_FORMAT_VERSION = "0.5";
	
	private final static Logger LOGGER = Logger.getLogger(ScenarioDocuAggregator.class);
	
	private final ScenarioDocuReader reader = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
	
	private final ScenarioDocuAggregationDAO dao = new ScenarioDocuAggregationDAO(
			ConfigurationDAO.getDocuDataDirectoryPath());
	
	private final Map<String, StepVariantState> mapOfStepVariant = new HashMap<String, StepVariantState>();
	
	@Data
	public class StepVariantState {
		private StepIdentification firstStep;
		private StepIdentification previousStep;
		private Integer counter;
		
		public StepVariantState(final StepIdentification firstStep) {
			this.firstStep = firstStep;
			this.counter = new Integer(0);
		}
		
		public void increaseCounter() {
			counter++;
		}
	}
	
	public boolean containsAggregatedDataForBuild(final String branchName, final String buildName) {
		String version = dao.loadVersion(branchName, buildName);
		return !StringUtils.isBlank(version)
				&& version.equals(CURRENT_FILE_FORMAT_VERSION);
	}
	
	public void calculateAggregatedDataForBuild(final String branchName,
			final String buildName) {
		
		LOGGER.info("  calculating aggregated data for build : " + buildName);
		UseCaseScenariosList useCaseScenariosList = calculateUseCaseScenariosList(
				branchName, buildName);
		for (UseCaseScenarios scenarios : useCaseScenariosList
				.getUseCaseScenarios()) {
			calulateAggregatedDataForUseCase(branchName, buildName, scenarios);
		}
		
		// Calculate page variant counters
		HashMap<String, Integer> counters = new HashMap<String, Integer>();
		for (Entry<String, StepVariantState> entry : mapOfStepVariant
				.entrySet()) {
			StepVariantState variant = entry.getValue();
			counters.put(entry.getKey(), variant.getCounter());
			
			StepIdentification lastStep = variant.getPreviousStep();
			setNextVariant(branchName, buildName, null, lastStep,
					variant.getFirstStep());
			setPreviousVariant(branchName, buildName, variant.getFirstStep(),
					lastStep);
		}
		
		dao.savePageVariants(branchName, buildName, new PageVariantsCounter(counters));
		
		dao.saveUseCaseScenariosList(branchName, buildName, useCaseScenariosList);
		
		dao.saveVersion(branchName, buildName, CURRENT_FILE_FORMAT_VERSION);
		
	}
	
	private UseCaseScenariosList calculateUseCaseScenariosList(final String branchName, final String buildName) {
		
		UseCaseScenariosList result = new UseCaseScenariosList();
		List<UseCaseScenarios> useCaseScenarios = new ArrayList<UseCaseScenarios>();
		List<UseCase> usecases = reader.loadUsecases(branchName, buildName);
		for (UseCase usecase : usecases) {
			UseCaseScenarios item = new UseCaseScenarios();
			List<Scenario> scenarios = reader.loadScenarios(branchName,
					buildName, usecase.getName());
			boolean atLeastOneScenarioFailed = false;
			for (Scenario scenario : scenarios) {
				if (StringUtils.equals(scenario.getStatus(), "failed")) {
					atLeastOneScenarioFailed = true;
					break;
				}
			}
			usecase.setStatus(atLeastOneScenarioFailed ? "failed" : "success");
			item.setScenarios(scenarios);
			item.setUseCase(usecase);
			useCaseScenarios.add(item);
		}
		result.setUseCaseScenarios(useCaseScenarios);
		return result;
	}
	
	private void calulateAggregatedDataForUseCase(final String branchName,
			final String buildName, final UseCaseScenarios useCaseScenarios) {
		LOGGER.info("    calculating aggregated data for use case : " + useCaseScenarios.getUseCase().getName());
		for (Scenario scenario : useCaseScenarios.getScenarios()) {
			try {
				calculateAggregatedDataForScenario(branchName, buildName, useCaseScenarios.getUseCase(), scenario);
			} catch (ResourceNotFoundException ex) {
				LOGGER.warn("could not load scenario " + scenario.getName()
						+ " in use case"
						+ useCaseScenarios.getUseCase().getName());
			}
		}
		dao.saveUseCaseScenarios(branchName, buildName, useCaseScenarios);
	}
	
	private void calculateAggregatedDataForScenario(final String branchName,
			final String buildName, final UseCase usecase,
			final Scenario scenario) {
		
		LOGGER.info("      calculating aggregated data for scenario : "
				+ scenario.getName());
		ScenarioPageSteps scenarioPageSteps = calculateScenarioPageSteps(
				branchName, buildName, usecase, scenario);
		
		dao.saveScenarioPageSteps(branchName, buildName, scenarioPageSteps);
	}
	
	private ScenarioPageSteps calculateScenarioPageSteps(
			final String branchName, final String buildName,
			final UseCase usecase, final Scenario scenario) {
		
		ScenarioPageSteps result = new ScenarioPageSteps();
		result.setUseCase(usecase);
		result.setScenario(scenario);
		
		// pages and steps
		List<Step> steps = reader.loadSteps(branchName, buildName,
				usecase.getName(), scenario.getName());
		int numberOfSteps = steps.size();
		List<PageSteps> pageStepsList = new ArrayList<PageSteps>();
		Page page = null;
		PageSteps pageSteps = null;
		int occurence = 0;
		int relativeIndex = 0;
		int index = 0;
		for (Step step : steps) {
			boolean isNewOccurence = page == null || step.getPage() == null
					|| !page.equals(step.getPage());
			if (isNewOccurence) {
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				pageStepsList.add(pageSteps);
				relativeIndex = 0;
				if (index > 0) {
					occurence++;
				}
			}
			StepDescription stepDescription = step.getStepDescription();
			stepDescription.setOccurence(occurence);
			stepDescription.setRelativeIndex(relativeIndex);
			pageSteps.getSteps().add(stepDescription);
			
			StepIdentification stepIdentification = new StepIdentification(
					usecase.getName(), scenario.getName(), page.getName(),
					index, occurence, relativeIndex);
			processStepVariant(branchName, buildName, pageStepsList, step,
					page.getName(), stepIdentification);
			
			index++;
			relativeIndex++;
		}
		result.setPagesAndSteps(pageStepsList);
		
		// scenario calculated data from pages and steps
		ScenarioCalculatedData calculatedData = new ScenarioCalculatedData();
		calculatedData.setNumberOfPages(occurence);
		calculatedData.setNumberOfSteps(numberOfSteps);
		scenario.setCalculatedData(calculatedData);
		
		return result;
	}
	
	private void processStepVariant(final String branchName,
			final String buildName, final List<PageSteps> pageStepsList,
			final Step step, final String pageName,
			final StepIdentification stepIdentification) {
		StepVariantState variant = null;
		if (mapOfStepVariant.containsKey(pageName)) {
			variant = mapOfStepVariant.get(pageName);
		} else {
			variant = new StepVariantState(stepIdentification);
			mapOfStepVariant.put(pageName, variant);
		}
		
		variant.increaseCounter();
		if (variant.getPreviousStep() != null) {
			StepDescription stepDescription = step.getStepDescription();
			stepDescription.setVariantIndex(variant.getCounter());
			stepDescription.setPreviousStepVariant(variant.getPreviousStep());
			setNextVariant(branchName, buildName, pageStepsList,
					variant.getPreviousStep(), stepIdentification);
		}
		variant.setPreviousStep(stepIdentification);
	}
	
	private void setNextVariant(final String branchName,
			final String buildName, final List<PageSteps> pageStepsList,
			final StepIdentification step,
			final StepIdentification nextStepVariant) {
		if (pageStepsList != null && isSameScenario(nextStepVariant, step)) {
			PageSteps page = pageStepsList.get(step.getOccurence());
			StepDescription stepDescription = page.getSteps().get(
					step.getRelativeIndex());
			stepDescription.setNextStepVariant(nextStepVariant);
		} else {
			ScenarioPageSteps pageSteps = dao.loadScenarioPageSteps(branchName,
					buildName, step.getUseCaseName(), step.getScenarioName());
			PageSteps page = pageSteps.getPagesAndSteps().get(
					step.getOccurence());
			StepDescription stepDescription = page.getSteps().get(
					step.getRelativeIndex());
			stepDescription.setNextStepVariant(nextStepVariant);
			dao.saveScenarioPageSteps(branchName, buildName, pageSteps);
		}
	}
	
	private void setPreviousVariant(final String branchName, final String buildName, final StepIdentification step,
			final StepIdentification previousStepVariant) {
		ScenarioPageSteps pageSteps = dao.loadScenarioPageSteps(branchName,
				buildName, step.getUseCaseName(), step.getScenarioName());
		PageSteps page = pageSteps.getPagesAndSteps().get(step.getOccurence());
		StepDescription stepDescription = page.getSteps().get(
				step.getRelativeIndex());
		stepDescription.setPreviousStepVariant(previousStepVariant);
		dao.saveScenarioPageSteps(branchName, buildName, pageSteps);
	}
	
	private boolean isSameScenario(final StepIdentification stepIdentification, final StepIdentification nextStepVariant) {
		return stepIdentification.getUseCaseName().equals(nextStepVariant.getUseCaseName())
				&& stepIdentification.getScenarioName().equals(nextStepVariant.getScenarioName());
	}
}
