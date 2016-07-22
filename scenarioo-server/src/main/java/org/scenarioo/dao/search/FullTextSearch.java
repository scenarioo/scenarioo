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
import org.scenarioo.dao.search.dao.*;
import org.scenarioo.dao.search.elasticsearch.ElasticSearchAdapter;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;

public class FullTextSearch {

	public static final String STEP = "step";
	public static final String SCENARIO = "scenario";
	public static final String USECASE = "usecase";
	private final static Logger LOGGER = Logger.getLogger(FullTextSearch.class);

	private final SearchAdapter searchAdapter;

	public FullTextSearch() {
		this(new ElasticSearchAdapter());
	}

	FullTextSearch(SearchAdapter search) {
		this.searchAdapter = search;
	}

	public boolean isEngineRunning() {
		return searchAdapter.isEngineRunning();
	}

	public SearchTree search(String q, BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			return SearchTree.empty();
		}

		List<SearchDao> searchResults = searchAdapter.searchData(buildIdentifier, q);

		return new SearchTree(searchResults, q);
	}

	public void indexUseCases(final UseCaseScenariosList useCaseScenariosList, final BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			return;
		}

		searchAdapter.setupNewBuild(buildIdentifier);
		searchAdapter.indexUseCases(useCaseScenariosList, buildIdentifier);

		LOGGER.info("Indexed use cases for build " + buildIdentifier);
	}

	public void indexSteps(List<Step> steps, List<StepLink> stepLinkList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier) {
		if(!searchAdapter.isEngineRunning()) {
			return;
		}

		searchAdapter.indexSteps(steps, stepLinkList, scenario, usecase, buildIdentifier);

		LOGGER.debug("Indexed steps for use case " + usecase.getName());
	}

	public void updateAvailableBuilds(List<BuildImportSummary> availableBuilds) {
		if(!searchAdapter.isEngineRunning()) {
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
