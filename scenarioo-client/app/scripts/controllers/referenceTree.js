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

'use strict';

angular.module('scenarioo.controllers').controller('ReferenceTreeCtrl', function ($rootScope, $scope, $routeParams, ObjectIndexListResource, PagesAndSteps, SelectedBranchAndBuild, ScenarioResource, $location) {

    //var objectType = $routeParams.objectType;
    //var objectName = $routeParams.objectName;
    var selectedBranchAndBuild;

    $scope.referenceTree = [];
    $scope.locationPath;

    SelectedBranchAndBuild.callOnSelectionChange(loadReferenceTree);

    function loadReferenceTree(selected) {
    selectedBranchAndBuild = selected;
    
    ObjectIndexListResource.get(
        {
            branchName: selected.branch,
            buildName: selected.build,
            objectType: 'page',             // objectType,
            objectName: 'searchResults.jsp' // objectName
        },
        function(result) {
            $scope.referenceTree = result;
        });
	}

    $scope.onTreeNodeClick = function(nodeElement) {
        ScenarioResource.get(
        {
            branchName: selectedBranchAndBuild.branch,
            buildName: selectedBranchAndBuild.build,
            usecaseName: 'Find Page',
            scenarioName: 'find_page_no_result'
        },
        function(result) {
            $scope.locationPath = '/' + nodeElement.type + '/*';
            var pagesAndSteps;
            var pageStepIndexes;

            switch(nodeElement.type) {
                case "case":
                    concatLocationPath(nodeElement);
                    break;
                case "scenario":
                    concatLocationPath(nodeElement);
                    break;
                case "page":
                case "step":
                    $scope.locationPath = '/step/*';                
                    concatLocationPath(nodeElement);
                    pagesAndSteps = $rootScope.populatePageAndSteps(result);
                    pageStepIndexes = getPageAndStepIndex('searchResults.jsp', pagesAndSteps) ;
                    $scope.locationPath += '/' + (pageStepIndexes[0].page -1 )+ '/' + pageStepIndexes[0].step
                    break;
            }

        $scope.locationPath = $scope.locationPath.replace('/*', '');
        $location.path($scope.locationPath);
        })
    };

    function getPageAndStepIndex(pageName, pagesAndSteps) {
        var result = [];

        angular.forEach(pagesAndSteps.pagesAndSteps, function (value, index) {
           if (value.page.name == pageName) {
                result = [{page: value.page.index, step: value.steps[0].index}];
            }
        });
        
        return result;
    }

    function concatLocationPath(nodeElement) {
        if (angular.isDefined(nodeElement) && nodeElement.level != 0) {
            $scope.locationPath = $scope.locationPath.replace('*', '*/' + encodeURIComponent(nodeElement.name));
            concatLocationPath(nodeElement.parent, $scope.locationPath);
        }
    };
});	