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


angular.module('scenarioo.controllers').controller('LabelConfigurationsCtrl', function ($scope, $location, $route, $modal, LabelConfigurationsResource, LabelConfigurationsListResource) {

    loadLabelConfigurations();

    function createEmptyLabelConfiguration() {
        return {'name': '', 'backgroundColor': '', 'foregroundColor': ''};
    }

    $scope.deleteEntry = function (labelName) {
        if (labelName !== '') {
            var index;
            for (index = 0; index < $scope.labelConfigurations.length; index++) {
                var labelConfiguration = $scope.labelConfigurations[index];
                if (labelConfiguration.name === labelName) {
                    $scope.labelConfigurations.splice(index, 1);
                    break;
                }
            }
        }
    };

    $scope.labelNameChanged = function () {
        var labelName = $scope.labelConfigurations[$scope.labelConfigurations.length - 1].name;
        if (labelName !== '') {
            $scope.labelConfigurations.push(createEmptyLabelConfiguration());
        }
    };


    $scope.reset = function () {
        loadLabelConfigurations();
    };

    $scope.save = function () {
        var labelConfigurationsAsMap = {};

        angular.forEach($scope.labelConfigurations, function(value) {
            if(value.name !== '') {
                labelConfigurationsAsMap[value.name] = {'backgroundColor': value.backgroundColor, 'foregroundColor': value.foregroundColor};
            }
        });

        LabelConfigurationsResource.save(labelConfigurationsAsMap);
    };

    function loadLabelConfigurations() {
        LabelConfigurationsListResource.query({}, function (labelConfigurations) {
            labelConfigurations.push(createEmptyLabelConfiguration());
            $scope.labelConfigurations = labelConfigurations;
        });
    }
});


