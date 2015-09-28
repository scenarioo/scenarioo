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

    .factory('IssuesResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue',
            {
                branchName: '@branchName'
            }, {});
    })

    .factory('IssueResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue/:issueId',
            {
                branchName: '@branchName',
                issueId: '@issueId'
            },
            {
                'query': {method: 'GET', isArray: false, params: {issueId: '@issueId'}},
                'update': {method: 'PUT', params: {issueId: '@issueId'}},
                'delete': {method: 'DELETE', params: {issueId: '@issueId'}}
            });
    })

    .factory('RelatedIssueResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue/related/',
            {
                branchName: '@branchName',
                objectName: '@objectName'
            },
            {
                'query': {method: 'GET', isArray: true, params: {objectName: 'null', type: 'usecase'}}
            });
    })

    .factory('NewIssueResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue/:issueName',
            {
                branchName: '@branchName',
                issueId: '@issueName'
            }, {});
    })

    .factory('ScenarioSketchResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue/:issueId/scenariosketch/:scenarioSketchId',
            {
                branchName: '@branchName',
                issueId: '@issueId',
                scenarioSketchId: '@scenarioSketchId'
            }, {});
    })

    .factory('SketchStepResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/issue/:issueId/scenariosketch/:scenarioSketchId/sketchstep/:sketchStepName',
            {
                branchName: '@branchName',
                issueId: '@issueId',
                scenarioSketchId: '@scenarioSketchId',
                sketchStepName: '@sketchStepName'
            }, {});
    });
