package org.scenarioo.model.docu.aggregates.scenarios;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.scenarioo.model.docu.aggregates.steps.StepStatistics;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.StepDescription;

public class ScenarioPageStepsTest {
	
	private ScenarioPageSteps scenarioPageSteps;
	private int total;
	private StepStatistics stepStatistics;
	
	@Test
	public void getTotalNumberOfStepsInScenario_nullList() {
		givenScenarioPagesAndStepsWithNullList();
		
		whenGettingTotal();
		
		expectTotalNumberOfStepsEquals(0);
	}
	
	@Test
	public void getTotalNumberOfStepsInScenario_moreThanZeroSteps() {
		givenScenarioPagesAndSteps();
		
		whenGettingTotal();
		
		expectTotalNumberOfStepsEquals(7);
	}
	
	@Test
	public void getTotalNumberOfStepsInPageOccurrence() {
		givenScenarioPagesAndSteps();
		
		whenGettingTotalStepsForPage1SecondOccurrence();
		
		expectTotalNumberOfStepsEquals(4);
	}
	
	@Test
	public void getStepStatistics() {
		givenScenarioPagesAndSteps();
		
		whenGettingPageStatisticsForPage1SecondOccurrence();
		
		expectTotalNumberOfStepsInStatisticsEquals(7);
		expectTotalNumberOfStepsInPageOccurrenceInStatisticsEquals(4);
		expectTotalNumberOfPagesInScenarioInStatisticsEquals(3);
	}
	
	private void givenScenarioPagesAndStepsWithNullList() {
		scenarioPageSteps = new ScenarioPageSteps();
	}
	
	private void givenScenarioPagesAndSteps() {
		scenarioPageSteps = new ScenarioPageSteps();
		scenarioPageSteps.setPagesAndSteps(createPagesAndStepsList());
	}
	
	private List<PageSteps> createPagesAndStepsList() {
		List<PageSteps> pageSteps = new LinkedList<PageSteps>();
		pageSteps.add(createPageStepsWithNumberOfSteps(1, "page1"));
		pageSteps.add(createPageStepsWithNumberOfSteps(2, "page2"));
		pageSteps.add(createPageStepsWithNumberOfSteps(4, "page1"));
		return pageSteps;
	}
	
	private PageSteps createPageStepsWithNumberOfSteps(final int numberOfSteps, final String pageName) {
		PageSteps pageSteps = new PageSteps();
		pageSteps.setPage(createPage(pageName));
		pageSteps.setSteps(createStepsListWithNumberOfSteps(numberOfSteps));
		return pageSteps;
	}
	
	private Page createPage(final String pageName) {
		Page page = new Page();
		page.setName(pageName);
		return page;
	}
	
	private List<StepDescription> createStepsListWithNumberOfSteps(final int numberOfSteps) {
		List<StepDescription> stepDescription = new LinkedList<StepDescription>();
		for (int i = 0; i < numberOfSteps; i++) {
			stepDescription.add(createStep());
		}
		return stepDescription;
	}
	
	private StepDescription createStep() {
		return new StepDescription();
	}
	
	private void whenGettingTotal() {
		total = scenarioPageSteps.getTotalNumberOfStepsInScenario();
	}
	
	private void whenGettingTotalStepsForPage1SecondOccurrence() {
		total = scenarioPageSteps.getTotalNumberOfStepsInPageOccurrence("page1", 1);
	}
	
	private void whenGettingPageStatisticsForPage1SecondOccurrence() {
		stepStatistics = scenarioPageSteps.getStepStatistics("page1", 1);
	}
	
	private void expectTotalNumberOfStepsEquals(final int expectedTotal) {
		Assert.assertEquals(expectedTotal, total);
	}
	
	private void expectTotalNumberOfStepsInStatisticsEquals(final int i) {
		Assert.assertEquals(i, stepStatistics.getTotalNumberOfStepsInScenario());
	}
	
	private void expectTotalNumberOfStepsInPageOccurrenceInStatisticsEquals(final int i) {
		Assert.assertEquals(i, stepStatistics.getTotalNumberOfStepsInPageOccurrence());
	}
	
	private void expectTotalNumberOfPagesInScenarioInStatisticsEquals(final int i) {
		Assert.assertEquals(i, stepStatistics.getTotalNumberOfPagesInScenario());
	}
	
}
