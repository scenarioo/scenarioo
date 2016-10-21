package org.scenarioo.dao.search;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.scenarioo.dao.search.dao.SearchDao;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;


public class FullTextSearchTest {

    private FullTextSearch fullTextSearch;

    @Test
    public void indexUseCaseWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.indexUseCases(new UseCaseScenariosList(), new BuildIdentifier("testBranch", "testBuild"));
        thenJustReturns();
    }

	@Test(expected = SearchEngineNotRunningException.class)
    public void searchWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.search("hi", new BuildIdentifier("testBranch", "testBuild"));
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
        SearchTree result = fullTextSearch.search("IDONOTEXIST", new BuildIdentifier("testBranch", "testBuild"));
		assertEquals(0, result.buildObjectTree().getChildren().size());
    }

    private void givenNoRunningEngine() {
        fullTextSearch = new FullTextSearch(new SearchEngine(false));
    }

    private void givenRunningEngineWithSearchResults() {
        fullTextSearch = new FullTextSearch(new SearchEngine(true));
    }

    private void thenJustReturns() {
		// for test
    }

    private class SearchEngine implements SearchAdapter {

		private final boolean isRunning;

		SearchEngine(final boolean isRunning) {
			this.isRunning = isRunning;
		}

        @Override
        public boolean isEngineRunning() {
            return isRunning;
        }

        @Override
        public List<SearchDao> searchData(final BuildIdentifier buildIdentifier, final String q) {
			assertTrue("Should not be reachable", isRunning);

            return Collections.emptyList();
        }

        @Override
        public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);        }

        @Override
        public void updateAvailableBuilds(final List<BuildIdentifier> existingBuilds) {
			assertTrue("Should not be reachable", isRunning);
		}

		@Override
		public void indexSteps(final List<Step> steps, final List<StepLink> page, final Scenario scenario, final UseCase usecase, final BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);
		}

		@Override
        public void setupNewBuild(final BuildIdentifier buildIdentifier) {
			assertTrue("Should not be reachable", isRunning);
		}

		@Override
		public String getEndpoint() {
			return "localhost:1234";
		}

		@Override
		public boolean isSearchEndpointConfigured() {
			return true;
		}
    }

}
