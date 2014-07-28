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

angular.module('scenarioo.directives').directive('scShowHideDetails', function($window, localStorageService) {

    function toggleClassesOnPanels(elem, showingMetaData) {
        var childs = elem.children();
        var mainAndDetailPanelRow = childs[0];
        var panelChildren = mainAndDetailPanelRow.children;
        var mainPanel = panelChildren[0];
        var detailPanel = panelChildren[1];
        if (showingMetaData) {
            mainPanel.setAttribute('class', 'col-lg-8');
            detailPanel.setAttribute('class', 'col-lg-4 hero-unit meta-data');
            detailPanel.style.display = 'block';
        } else {
            mainPanel.setAttribute('class', 'col-lg-12');
            detailPanel.setAttribute('class', 'hero-unit meta-data');
            detailPanel.style.display = 'none';
        }
    }

    return {
        restrict: 'E',
        transclude: true,
        scope: {
            linkingVariable: '='
        },
        templateUrl: 'views/showHideDetails.html',
        controller: function($scope, $element) {
            $scope.$watch('linkingVariable', function() {
                toggleClassesOnPanels($element, $scope.linkingVariable);
            });
        }
    };

});