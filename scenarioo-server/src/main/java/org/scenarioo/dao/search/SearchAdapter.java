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

import java.util.List;

import org.scenarioo.dao.search.model.SearchResults;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.search.SearchRequest;

public interface SearchAdapter {

	boolean isSearchEndpointConfigured();

    boolean isEngineRunning();

	String getEndpoint();

    SearchResults searchData(SearchRequest searchRequest);

    void indexUseCases(UseCaseScenariosList useCaseScenariosList, BuildIdentifier buildIdentifier);

    void updateAvailableBuilds(List<BuildIdentifier> existingBuilds);

	void indexSteps(List<Step> steps, List<StepLink> page, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier);

    void setupNewBuild(BuildIdentifier buildIdentifier);
}
