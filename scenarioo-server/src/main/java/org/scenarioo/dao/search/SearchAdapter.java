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

import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenariosList;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.rest.base.BuildIdentifier;

import java.util.List;

public interface SearchAdapter {

    boolean isEngineRunning();
    List<String> searchData(BuildIdentifier buildIdentifier, String q);
    void indexUseCases(UseCaseScenariosList useCaseScenariosList, BuildIdentifier buildIdentifier);
    void updateAvailableBuilds(List<BuildIdentifier> existingBuilds);

    void indexPages(List<PageSteps> pageStepsList, Scenario scenario, UseCase usecase, BuildIdentifier buildIdentifier);

    void setupNewBuild(BuildIdentifier buildIdentifier);
}
