/* scenarioo-client
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

angular.module('scenarioo.services')

    .factory('ComparisonConfigurationResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/comparisonConfiguration/comparisonName/:comparisonName',
            {
                comparisonName: '@comparisonName'
            }, {});
    })


    .factory('BuildDiffInfosResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/buildDiffInfos', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName'
        }, {});
    })


    .factory('BuildDiffInfoResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/buildDiffInfo', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName'
        }, {});
    })

    .factory('UseCaseDiffInfoResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/useCaseDiffInfo', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName',
            useCaseName: '@useCaseName'
        }, {});
    })

    .factory('UseCaseDiffInfosResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseDiffInfos', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName'
        }, {});
    })

    .factory('ScenarioDiffInfoResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/scenarioDiffInfo', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName'
        }, {});
    })

    .factory('ScenarioDiffInfosResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioDiffInfos', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName',
            useCaseName: '@useCaseName'
        }, {});
    })

    .factory('StepDiffInfoResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/stepIndex/:stepIndex/stepDiffInfo',
            {
                baseBranchName: '@baseBranchName',
                baseBuildName: '@baseBuildName',
                comparisonName: '@comparisonName',
                useCaseName: '@useCaseName',
                scenarioName: '@scenarioName',
                stepIndex: '@stepIndex'
            }, {});
    })

    .factory('StepDiffInfosResource', function (ScenariooResource) {
        return ScenariooResource('/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/stepDiffInfos', {
            baseBranchName: '@baseBranchName',
            baseBuildName: '@baseBuildName',
            comparisonName: '@comparisonName',
            useCaseName: '@useCaseName',
            scenarioName: '@scenarioName'
        }, {});
    });
