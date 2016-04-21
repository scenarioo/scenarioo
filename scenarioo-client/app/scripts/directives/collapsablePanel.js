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

angular.module('scenarioo.directives').directive('scCollapsablePanel', function (scLocalStorage) {

    var MAIN_METADATA_SECTION_EXPANDED = 'scenarioo-panelExpanded-';

    function initMetadataVisibilityAndExpandedSections(key) {

        // Set special metadata to expanded by default.
        var majorStepPropertiesExpanded = scLocalStorage.get(MAIN_METADATA_SECTION_EXPANDED + key);
        var isMajorStepPropertiesExpandedSetToFalse = majorStepPropertiesExpanded === 'false';
        if (!isMajorStepPropertiesExpandedSetToFalse) {
            scLocalStorage.set(MAIN_METADATA_SECTION_EXPANDED + key, 'true');
        }
    }

    return {
        restrict: 'E',
        transclude: true,
        templateUrl: 'views/collapsablePanel.html',
        scope: {
            title: '@',
            key: '@',
            initiallyExpanded: '@',
            panelIndex: '@'
        },
        link: function (scope) {
            if (scope.initiallyExpanded === 'true') {
                initMetadataVisibilityAndExpandedSections(scope.key);
            }
        },
        controller: function ($scope) {
            $scope.isMetadataExpanded = function (type) {
                var metadataExpanded = scLocalStorage.get(MAIN_METADATA_SECTION_EXPANDED + type);
                if (metadataExpanded === 'true') {
                    return true;
                } else {
                    return false;
                }
            };

            $scope.toggleMetadataExpanded = function (type) {
                var metadataExpanded = !$scope.isMetadataExpanded(type);
                scLocalStorage.set(MAIN_METADATA_SECTION_EXPANDED + type, '' + metadataExpanded);
            };

            $scope.isMetadataCollapsed = function (type) {
                return !$scope.isMetadataExpanded(type);
            };

        }
    };
});
