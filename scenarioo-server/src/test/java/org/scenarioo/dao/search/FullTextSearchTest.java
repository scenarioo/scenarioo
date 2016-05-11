package org.scenarioo.dao.search;

import org.junit.Test;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
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
        fullTextSearch = new FullTextSearch(new NonRunningEngine());
    }

    private void givenRunningEngineWithSearchResults() {
        fullTextSearch = new FullTextSearch(new RunningEngine());
    }

    private void thenJustReturns() {
    }

    private class NonRunningEngine implements SearchAdapter {

        @Override
        public boolean isEngineRunning() {
            return false;
        }

        @Override
        public List<String> searchData(BuildIdentifier buildIdentifier, String q) {
            fail("Should not be reachable");

            return null;
        }

        @Override
        public void indexUseCases(UseCaseScenariosList useCaseScenariosList, BuildIdentifier buildIdentifier) {
            fail("Should not be reachable");
        }

        @Override
        public void updateAvailableBuilds(List<BuildIdentifier> existingBuilds) {
            fail("Should not be reachable");
        }

        @Override
        public void indexPages(List<PageSteps> pageStepsList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
            fail("Should not be reachable");
        }

        @Override
        public void setupNewBuild(BuildIdentifier buildIdentifier) {
            fail("Should not be reachable");
        }
    }

    private class RunningEngine implements SearchAdapter {

        @Override
        public boolean isEngineRunning() {
            return true;
        }

        @Override
        public List<String> searchData(BuildIdentifier buildIdentifier, String q) {
            return Collections.emptyList();
        }

        @Override
        public void indexUseCases(UseCaseScenariosList useCaseScenariosList, BuildIdentifier buildIdentifier) {
            // nothing to do
        }

        @Override
        public void updateAvailableBuilds(List<BuildIdentifier> existingBuilds) {
            // nothing to do
        }

        @Override
        public void indexPages(List<PageSteps> pageStepsList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
            // nothing to do
        }

        @Override
        public void setupNewBuild(BuildIdentifier buildIdentifier) {
            // nothing to do
        }
    }

}