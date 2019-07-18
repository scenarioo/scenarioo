package org.scenarioo.dao.search;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.scenarioo.dao.search.model.SearchResults;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.search.SearchRequest;

import static org.junit.jupiter.api.Assertions.*;

class FullTextSearchTest {

    private FullTextSearch fullTextSearch;

    @Test
    void indexUseCaseWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.indexUseCases(new UseCaseScenariosList(), new BuildIdentifier("testBranch", "testBuild"));
        thenJustReturns();
    }

	@Test
    void searchWithoutRunningEngine() {
        givenNoRunningEngine();
        assertThrows(SearchEngineNotRunningException.class, ()->fullTextSearch.search(new SearchRequest(new BuildIdentifier("testBranch", "testBuild"), "hi", true)));
    }

    @Test
    void updateAvailableBuildsWithoutRunningEngine() {
        givenNoRunningEngine();
        fullTextSearch.updateAvailableBuilds(Collections.emptyList());
        thenJustReturns();
    }

    @Test
    void searchWithNoResults() {
        givenRunningEngineWithSearchResults();
        SearchTree result = fullTextSearch.search(new SearchRequest(new BuildIdentifier("testBranch", "testBuild"), "IDONOTEXIST", true));
		assertEquals(0, result.getResults().getChildren().size());
		assertEquals(0, result.getTotalHits());
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
		public SearchResults searchData(final SearchRequest searchRequest) {
			assertTrue(isRunning, "Should not be reachable");

            return SearchResults.noHits();
        }

        @Override
		public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
			assertTrue(isRunning, "Should not be reachable");        }

        @Override
		public void updateAvailableBuilds(final List<BuildIdentifier> existingBuilds) {
			assertTrue(isRunning, "Should not be reachable");
		}

		@Override
		public void indexSteps(final List<Step> steps, final List<StepLink> page, final Scenario scenario, final UseCase usecase, final BuildIdentifier buildIdentifier) {
			assertTrue(isRunning, "Should not be reachable");
		}

		@Override
		public void setupNewBuild(final BuildIdentifier buildIdentifier) {
			assertTrue(isRunning, "Should not be reachable");
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
