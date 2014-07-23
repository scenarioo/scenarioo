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

angular.module('scenarioo.directives').directive('scShowHideDetails', function(localStorageService) {

    var STEP_METADATA_VISIBLE = 'scenarioo-stepMetadataVisible';
    function toggleClassesOnPanels(elem, showingMetaData) {
        console.log(showingMetaData);
        var childs = elem.children();
        var buttonPanel = childs[0];
        var hideButton = buttonPanel.querySelector('#sc-showHideDetailsButton-hide');
        var showButton = buttonPanel.querySelector('#sc-showHideDetailsButton-show');
        var panel = childs[1];
        var panelChildren = panel.children;
        var mainPanel = panelChildren[0];
        var detailPanel = panelChildren[1];
        if (showingMetaData) {
            mainPanel.setAttribute('class', 'col-lg-8');
            detailPanel.setAttribute('class', 'col-lg-4 hero-unit meta-data');
            detailPanel.style.display = "block";
            hideButton.style.display = "block";
            showButton.style.display = "none";
        } else {
            mainPanel.setAttribute('class', 'col-lg-12');
            detailPanel.setAttribute('class', 'hero-unit meta-data');
            detailPanel.style.display = "none";
            hideButton.style.display = "none";
            showButton.style.display = "block";
        }
    }
    return {
        restrict: 'E',
        transclude: true,
        templateUrl: 'views/showHideDetails.html',
        link: function (scope, element, attributes ) {
            var showingMetaData = localStorageService.get(STEP_METADATA_VISIBLE);
            toggleClassesOnPanels(element, showingMetaData === 'true');
        },
        controller: function($scope, $element) {

            $scope.showingMetaData = localStorageService.get(STEP_METADATA_VISIBLE) === 'true';
            $scope.toggleShowingMetadata = function() {
                $scope.showingMetaData = !$scope.showingMetaData;
                localStorageService.set(STEP_METADATA_VISIBLE, '' + $scope.showingMetaData);
                toggleClassesOnPanels($element, $scope.showingMetaData);
            };


        }
    };

});