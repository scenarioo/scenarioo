/* scenarioo-client
 * Copyright (C) 2015, scenarioo.org Development Team
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

angular.module('scenarioo.services').service('ContextService', function () {

    var issueName, issueId, scenarioSketchName, scenarioSketchId, sketchStepIndex;
    var usecase, scenario, step;

    function initializeContext(){
        issueId = null;
        issueName = null;
        scenarioSketchName = null;
        scenarioSketchId = null;
        sketchStepIndex = null;

        usecase = null;
        scenario = null;
        step = null;
    }

    return {
        issueId: issueId,
        issueName: issueName,
        scenarioSketchId: scenarioSketchId,
        scenarioSketchName: scenarioSketchName,
        sketchStepIndex: sketchStepIndex,
        usecase: usecase,
        scenario: scenario,
        step: step,

        initialize: initializeContext
    };

});
