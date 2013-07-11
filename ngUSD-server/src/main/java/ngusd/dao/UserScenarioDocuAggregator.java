package ngusd.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ngusd.model.docu.aggregates.scenarios.PageSteps;
import ngusd.model.docu.aggregates.scenarios.ScenarioPageSteps;
import ngusd.model.docu.aggregates.usecases.UseCaseScenarios;
import ngusd.model.docu.aggregates.usecases.UseCaseScenariosList;
import ngusd.model.docu.entities.Page;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.ScenarioCalculatedData;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.StepDescription;
import ngusd.model.docu.entities.UseCase;

public class UserScenarioDocuAggregator {
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	public boolean containsAggregatedDataForBuild(final String branchName, final String buildName) {
		File file = filesystem.filePath(branchName, buildName, "usecases.xml");
		return file.exists();
	}
	
	public void calculateAggregatedDataForBuild(final String branchName, final String buildName) {
		UseCaseScenariosList useCaseScenariosList = calculateUseCaseScenariosList(branchName, buildName);
		for (UseCaseScenarios scenarios : useCaseScenariosList.getUseCaseScenarios()) {
			calulateAggregatedDataForUseCase(branchName, buildName, scenarios);
		}
		
		// Write usecases
		File file = filesystem.filePath(branchName, buildName, "usecases.xml");
		XMLFileUtil.marshal(useCaseScenariosList, file, UseCaseScenariosList.class);
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
		
		// TODO: Calculate aggregate data for all steps and pages
		
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
		
		// pages and steps
		List<Step> steps = filesystem.loadSteps(branchName, buildName, usecase.getName(),
				scenario.getName());
		int numberOfSteps = steps.size();
		List<PageSteps> pageStepsList = new ArrayList<PageSteps>();
		Page page = null;
		PageSteps pageSteps = null;
		int numberOfPages = 0;
		for (Step step : steps) {
			if (page == null || step.getPage() == null || !page.equals(step.getPage())) {
				numberOfPages++;
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				pageStepsList.add(pageSteps);
			}
			pageSteps.getSteps().add(step.getStep());
		}
		result.setPagesAndSteps(pageStepsList);
		
		// scenario calculated data from pages and steps
		ScenarioCalculatedData calculatedData = new ScenarioCalculatedData();
		calculatedData.setNumberOfPages(numberOfPages);
		calculatedData.setNumberOfSteps(numberOfSteps);
		scenario.setCalculatedData(calculatedData);
		
		return result;
	}
}
