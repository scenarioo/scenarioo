

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController(FeatureService, $rootScope, $scope, $location, $http, $timeout, SelectedBranchAndBuildService){

    var dashboard = this;
    dashboard.milestones = [];

    dashboard.isCollapsed = false;


    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'milestone';

    dashboard.navsize = (localStorage.getItem('MAV_SIZE_LEFT') != undefined)? localStorage.getItem('MAV_SIZE_LEFT'):200;


    dashboard.clickFeature = function(subFeature, location){
        console.log('test');

        FeatureService.setFeature(subFeature);
        console.log('loc' + location);
        if (location && location !== ''){
            $location.path(location);
        }
    };

    load();

    $rootScope.$watch(FeatureService.getFeature, function (feature) {
        dashboard.feature = feature;
    });

    $rootScope.$watch(FeatureService.getRootFeature, function (feature) {
        dashboard.rootFeature = feature;
    });

    $rootScope.$watch(FeatureService.getMilestones, function (feature) {
        dashboard.milestones = feature;
    });

    dashboard.eq = function (feat1, feat2) {
        return feat1 === feat2;
    };

    function load() {
        dashboard.rootFeature = FeatureService.getRootFeature();
        dashboard.feature = FeatureService.getFeature();
    }

    dashboard.expandAll = function() {
        dashboard.feature.markdown.forEach(function (markdown) {markdown.isCollapsed=false;});
        dashboard.feature.features.forEach(function (feature) {feature.isCollapsed=false;});
    };
    dashboard.collapseAll = function () {
        dashboard.feature.markdown.forEach(function (markdown) {markdown.isCollapsed=true;});
        dashboard.feature.features.forEach(function (feature) {feature.isCollapsed=true;});
    };

    dashboard.contains = function (feature, field) {
        return feature[field] != null;
    }
}

