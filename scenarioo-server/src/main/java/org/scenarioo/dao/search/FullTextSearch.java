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

package org.scenarioo.dao.search;

import java.util.*;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.search.dao.*;
import org.scenarioo.dao.search.elasticsearch.ElasticSearchAdapter;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

public class FullTextSearch {

	private final static Logger LOGGER = Logger.getLogger(FullTextSearch.class);

	private final ScenarioDocuReader scenarioDocuReader;
	private final SearchAdapter searchAdapter;

	public FullTextSearch() {
		this(new ScenarioDocuReader(
			RepositoryLocator.INSTANCE.getConfigurationRepository().getDocumentationDataDirectory()));
	}

	public FullTextSearch(ScenarioDocuReader scenarioDocuReader) {
		this(scenarioDocuReader, new ElasticSearchAdapter());
	}

	FullTextSearch(ScenarioDocuReader scenarioDocuReader, SearchAdapter search) {
		this.scenarioDocuReader = scenarioDocuReader;
		this.searchAdapter = search;
	}

	public boolean isEngineRunning() {
		return searchAdapter.isEngineRunning();
	}

	public SearchTree search(String q, BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			LOGGER.info("No search engine running.");
			return SearchTree.empty();
		}

		List<SearchDao> searchResults = searchAdapter.searchData(buildIdentifier, q);

		return buildObjectTree(searchResults, q);
	}


	public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			LOGGER.info("No search engine running.");
			return;
		}

		searchAdapter.setupNewBuild(buildIdentifier);
		searchAdapter.indexUseCases(useCaseScenariosList, buildIdentifier);

		LOGGER.info("Indexed build " + buildIdentifier);
	}


	public void indexPages(final List<PageSteps> pageStepsList, Scenario scenario, UseCase usecase, final BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			LOGGER.info("No search engine running.");
			return;
		}

		searchAdapter.indexPages(pageStepsList, scenario, usecase, buildIdentifier);

		LOGGER.info("Indexed pages " + buildIdentifier);
	}

	public void indexSteps(List<StepLink> stepLinkList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			LOGGER.info("No search engine running.");
			return;
		}

		List<Step> resolvedSteps = new ArrayList<Step>();
		for(StepLink stepLink : stepLinkList) {
				resolvedSteps.add(resolveStep(stepLink, scenario, usecase, buildIdentifier));

		}
		searchAdapter.indexSteps(resolvedSteps, stepLinkList, scenario, usecase, buildIdentifier);

		LOGGER.info("Indexed steps " + buildIdentifier);
	}

	private SearchTree buildObjectTree(List<SearchDao> searchResults, final String q) {
		if(searchResults.isEmpty()) {
			return SearchTree.empty();
		}

		return new SearchTree(searchResults, q);
	}

	private Step resolveStep(StepLink stepLink, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
		return scenarioDocuReader.loadStep(buildIdentifier.getBranchName(), buildIdentifier.getBuildName(),
			usecase.getName(), scenario.getName(), stepLink.getStepIndex());
	}

	public void updateAvailableBuilds(List<BuildImportSummary> availableBuilds) {
		if(!searchAdapter.isEngineRunning()) {
			LOGGER.info("No search engine running.");
			return;
		}

		List<BuildIdentifier> existingBuilds = getAvailableBuildIdentifiers(availableBuilds);

		searchAdapter.updateAvailableBuilds(existingBuilds);
		LOGGER.info("Updated available builds.");
	}

	private List<BuildIdentifier> getAvailableBuildIdentifiers(List<BuildImportSummary> availableBuilds) {
		List<BuildIdentifier> identifierList = new ArrayList<BuildIdentifier>();
		for(BuildImportSummary buildSummary : availableBuilds) {
			BuildIdentifier identifier = buildSummary.getIdentifier();

			identifierList.add(identifier);
		}

		return identifierList;
	}
}
