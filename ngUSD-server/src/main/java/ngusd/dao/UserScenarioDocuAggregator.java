package ngusd.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ngusd.model.docu.aggregates.scenarios.PageSteps;
import ngusd.model.docu.aggregates.scenarios.ScenarioPageSteps;
import ngusd.model.docu.aggregates.usecases.PageVariantsMap;
import ngusd.model.docu.aggregates.usecases.UseCaseScenarios;
import ngusd.model.docu.aggregates.usecases.UseCaseScenariosList;
import ngusd.model.docu.entities.Page;
import ngusd.model.docu.entities.PageVariants;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.ScenarioCalculatedData;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.StepDescription;
import ngusd.model.docu.entities.StepIdentification;
import ngusd.model.docu.entities.UseCase;

public class UserScenarioDocuAggregator {
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	public boolean containsAggregatedDataForBuild(final String branchName, final String buildName) {
		File fileUsecases = filesystem.filePath(branchName, buildName, "usecases.xml");
		File filePageVariants = filesystem.filePath(branchName, buildName, "pagevariants.xml");
		return fileUsecases.exists() && filePageVariants.exists();
	}
	
	public void calculateAggregatedDataForBuild(final String branchName, final String buildName) {
		UseCaseScenariosList useCaseScenariosList = calculateUseCaseScenariosList(branchName, buildName);
		for (UseCaseScenarios scenarios : useCaseScenariosList.getUseCaseScenarios()) {
			calulateAggregatedDataForUseCase(branchName, buildName, scenarios);
		}
		
		// Write usecases
		File file = filesystem.filePath(branchName, buildName, "usecases.xml");
		XMLFileUtil.marshal(useCaseScenariosList, file, UseCaseScenariosList.class);
		
		PageVariantsMap pageVariants = calculatePageVariants(useCaseScenariosList);
		
		// Write usecases
		file = filesystem.filePath(branchName, buildName, "pagevariants.xml");
		XMLFileUtil.marshal(pageVariants, file, PageVariantsMap.class);
	}
	
	private PageVariantsMap calculatePageVariants(final UseCaseScenariosList useCaseScenariosList) {
		Map<String, PageVariants> mapPageToOccurences = new HashMap<String, PageVariants>();
		Map<String, PageVariants> mapPageToSteps = new HashMap<String, PageVariants>();
		for (UseCaseScenarios useCase : useCaseScenariosList.getUseCaseScenarios()) {
			for (Scenario scenario : useCase.getScenarios()) {
				mapPageToOccurences.putAll(scenario.getCalculatedData().getMapPageToOccurences());
				mapPageToSteps.putAll(scenario.getCalculatedData().getMapPageToSteps());
			}
		}
		return new PageVariantsMap(mapPageToOccurences, mapPageToSteps);
	}
	
	private UseCaseScenariosList calculateUseCaseScenariosList(final String branchName, final String buildName) {
		
		UseCaseScenariosList result = new UseCaseScenariosList();
		List<UseCaseScenarios> useCaseScenarios = new ArrayList<UseCaseScenarios>();
		List<UseCase> usecases = filesystem.loadUsecases(branchName, buildName);
		for (UseCase usecase : usecases) {
			UseCaseScenarios item = new UseCaseScenarios();
			item.setUseCase(usecase);
			item.setScenarios(filesystem.loadScenarios(branchName, buildName, usecase.getName()));
			useCaseScenarios.add(item);
		}
		result.setUseCaseScenarios(useCaseScenarios);
		return result;
	}
	
	private void calulateAggregatedDataForUseCase(final String branchName, final String buildName,
			final UseCaseScenarios useCaseScenarios) {
		for (Scenario scenario : useCaseScenarios.getScenarios()) {
			calculateAggregatedDataForScenario(branchName, buildName, useCaseScenarios.getUseCase(), scenario);
		}
		writeUseCaseScenarios(branchName, buildName, useCaseScenarios);
	}
	
	private void writeUseCaseScenarios(final String branchName, final String buildName,
			final UseCaseScenarios useCaseScenarios) {
		File scenariosFile = filesystem.filePath(branchName, buildName, useCaseScenarios.getUseCase().getName(),
				"scenarios.xml");
		XMLFileUtil.marshal(useCaseScenarios, scenariosFile, UseCaseScenarios.class);
	}
	
	private void calculateAggregatedDataForScenario(final String branchName, final String buildName,
			final UseCase usecase, final Scenario scenario) {
		ScenarioPageSteps scenarioPageSteps = calculateScenarioPageSteps(branchName, buildName, usecase, scenario);
		
		// Write scenarioPageSteps.
		File file = filesystem.filePath(branchName, buildName, usecase.getName(),
				scenario.getName(), "scenarioPageSteps.xml");
		XMLFileUtil.marshal(scenarioPageSteps, file, ScenarioPageSteps.class);
	}
	
	private ScenarioPageSteps calculateScenarioPageSteps(final String branchName, final String buildName,
			final UseCase usecase,
			final Scenario scenario) {
		
		ScenarioPageSteps result = new ScenarioPageSteps();
		result.setUseCase(usecase);
		result.setScenario(scenario);
		
		// maps of page occurences and steps
		Map<String, PageVariants> mapPageToOccurences = new HashMap<String, PageVariants>();
		Map<String, PageVariants> mapPageToSteps = new HashMap<String, PageVariants>();
		
		// pages and steps
		List<Step> steps = filesystem.loadSteps(branchName, buildName, usecase.getName(),
				scenario.getName());
		int numberOfSteps = steps.size();
		List<PageSteps> pageStepsList = new ArrayList<PageSteps>();
		Page page = null;
		Page previousPage = null;
		PageSteps pageSteps = null;
		int occurence = 0;
		int relativeIndex = 0;
		int index = 0;
		for (Step step : steps) {
			if (page == null || step.getPage() == null || !page.equals(step.getPage())) {
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				pageStepsList.add(pageSteps);
			}
			relativeIndex++;
			StepDescription stepDescription = step.getStep();
			stepDescription.setOccurence(occurence);
			stepDescription.setRelativeIndex(relativeIndex);
			pageSteps.getSteps().add(stepDescription);
			
			// Aggregate meta data
			StepIdentification stepIdentification = new StepIdentification(usecase.getName(), scenario.getName(),
					page.getName(), index, occurence, relativeIndex);
			if (!page.equals(previousPage)) {
				relativeIndex = 0;
				occurence++;
				addToMap(mapPageToOccurences, page.getName(), stepIdentification);
			}
			addToMap(mapPageToSteps, page.getName(), stepIdentification);
			previousPage = page;
			index++;
		}
		result.setPagesAndSteps(pageStepsList);
		
		// scenario calculated data from pages and steps
		ScenarioCalculatedData calculatedData = new ScenarioCalculatedData();
		calculatedData.setNumberOfPages(occurence);
		calculatedData.setNumberOfSteps(numberOfSteps);
		calculatedData.setMapPageToOccurences(mapPageToOccurences);
		calculatedData.setMapPageToSteps(mapPageToSteps);
		scenario.setCalculatedData(calculatedData);
		
		return result;
	}
	
	private void addToMap(final Map<String, PageVariants> map, final String key,
			final StepIdentification value) {
		if (key == null || key.isEmpty()) {
			return;
		}
		PageVariants values = map.get(key);
		if (values == null) {
			values = new PageVariants();
			map.put(key, values);
		}
		values.getVariants().add(value);
	}
}
