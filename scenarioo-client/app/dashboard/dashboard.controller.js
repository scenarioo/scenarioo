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
angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);

function DashboardController(FeatureService, $rootScope, SelectedComparison,
                             LabelConfigurationsResource, $location, $scope){

    var dashboard = this;

    dashboard.table = {
        search: {searchTerm: ''},
        sort: {column: 'name', reverse: false}
    };
    $scope.table = dashboard.table;
    dashboard.labelConfigurations = undefined;
    dashboard.milestones = [];
    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'milestone';

    dashboard.setOrder = function (order, type) {
        dashboard[order] = type;
    };

    dashboard.clickFeature = FeatureService.clickFeature;
    dashboard.equals = FeatureService.equals;
    dashboard.contains = FeatureService.contains;
    dashboard.getLabelStyle = getLabelStyle;

    activate();
    dashboard.comparisonInfo = SelectedComparison.info;

    $rootScope.$watch(FeatureService.getFeature, function (feature) {
        dashboard.feature = feature;
    });

    $rootScope.$watch(FeatureService.getRootFeature, function (rootFeature) {
        dashboard.rootFeature = rootFeature;
    });

    $rootScope.$watch(FeatureService.getMilestones, function (milestone) {
        dashboard.milestones = milestone;
    });

    dashboard.mapFilter = FeatureService.milestoneFilter;

    function isDefined(val) {
        if (val == null)
            return false;
        if (val == undefined)
            return false;
        return val !== 'undefinded';
    }

    dashboard.expandAll = function() {
        setCollapseState(dashboard.feature, false);
    };

    dashboard.collapseAll = function () {
        setCollapseState(dashboard.feature, true);
    };

    function setCollapseState(feature, val) {
        if( !isDefined(feature) ) return;

        if (isDefined(feature.markdown)) feature.markdown.isCollapsed=val;

        if (isDefined(feature.specification)) feature.specification.isCollapsed=val;

        if (isDefined(feature.features)){
            feature.isCollapsed=val;
            for(var i = 0; i < feature.features.length; i++){
                setCollapseState(feature.features[i], val);
            }
        }
    }

    function activate() {
        dashboard.rootFeature = FeatureService.getRootFeature();
        dashboard.feature = FeatureService.getFeature();

        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            dashboard.labelConfigurations = labelConfiguratins;
        });
    }

    dashboard.clickScenario = function(feature, scenarioSummary) {
        if(!scenarioSummary.diffInfo || !scenarioSummary.diffInfo.isRemoved){
            goToScenario(feature, scenarioSummary.scenario.name);
        }
    };

    function goToScenario(feature, scenarioName) {
        $location.path('/scenario/' + feature + '/' + scenarioName);
    }

    dashboard.containsSubsub = function containsSubsub(feature){
        if (!angular.isDefined(feature.features)){
            return false;
        }
        for (var i = 0; i < feature.features.length ; i++){
            if (angular.isDefined(feature.features[i].features) && feature.features[i].features.length > 0){
                return true;
            }
        }
        return false;
    };

    function getLabelStyle(labelName) {
        if (dashboard.labelConfigurations) {
            var labelConfig = dashboard.labelConfigurations[labelName];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
    }
}

