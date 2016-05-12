package org.scenarioo.dao.search;

import org.junit.Test;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


public class FullTextSearchTest {

    private FullTextSearch fullTextSearch;

    @Test
    public void indexUseCaseWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.indexUseCases(new UseCaseScenariosList(), new BuildIdentifier("testBranch", "testBuild"));
        thenJustReturns();
    }

    @Test
    public void searchWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.search(new BuildIdentifier("testBranch", "testBuild"), "hi");
        thenJustReturns();
    }

    @Test
    public void updateAvailableBuildsWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.updateAvailableBuilds(Collections.<BuildImportSummary>emptyList());
        thenJustReturns();
    }

    @Test
    public void searchWithNoResults() {
        givenRunningEngineWithSearchResults();
        List<String> result = fullTextSearch.search(new BuildIdentifier("testBranch", "testBuild"), "IDONOTEXIST");
        assertEquals("No result found.", result.get(0));
    }

    private void givenNoRunningEngine() {
        fullTextSearch = new FullTextSearch(null, new SearchEngine(false));
    }

    private void givenRunningEngineWithSearchResults() {
        fullTextSearch = new FullTextSearch(null, new SearchEngine(true));
    }

    private void thenJustReturns() {
    }

    private class SearchEngine implements SearchAdapter {

		private boolean isRunning;

		SearchEngine(boolean isRunning) {
			this.isRunning = isRunning;
		}

        @Override
        public boolean isEngineRunning() {
            return isRunning;
        }

        @Override
        public List<String> searchData(BuildIdentifier buildIdentifier, String q) {
			assertTrue("Should not be reachable", isRunning);

            return Collections.emptyList();
        }

        @Override
        public void indexUseCases(UseCaseScenariosList useCaseScenariosList, BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);        }

        @Override
        public void updateAvailableBuilds(List<BuildIdentifier> existingBuilds) {
			assertTrue("Should not be reachable", isRunning);
		}

        @Override
        public void indexPages(List<PageSteps> pageStepsList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);        }

		@Override
		public void indexSteps(List<Step> steps, Page page, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);
		}

		@Override
        public void setupNewBuild(BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);
		}
    }

}
