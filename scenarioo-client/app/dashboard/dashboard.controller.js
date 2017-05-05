

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController(FeatureService, $rootScope, $scope, $location, $http, $timeout, SelectedBranchAndBuildService){

    var dashboard = this;
    dashboard.milestones = new Array();

    dashboard.isCollapsed = false;

    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'milestone';

    dashboard.clickFeature = function(subFeature, location){
        console.log('test');

        FeatureService.setFeature(subFeature);
        console.log('loc' + location);
        $location.path(location);
    };

    load();
    getAllMilestones();

    $rootScope.$watch(FeatureService.getFeature, function (feature) {
        dashboard.feature = feature;
    });

    $rootScope.$watch(FeatureService.getRootFeature, function (feature) {
        dashboard.rootFeature = feature;
        dashboard.milestones=[];
        console.log("getRootFeature called!");
        getAllMilestones();
    });

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

    function getAllMilestones(){
        getMilestone(dashboard.rootFeature);
        sortMilestone();
    }
    function getMilestone(feature){

        console.log("getMilestone", feature, feature.features, feature.features.length);
        feature.features.forEach(function(x){
            dashboard.milestones.push(x.milestone);
            console.log("push milestone", x.milestone);
           if(x.features!=null){
               getMilestone(x);
           }
        });
    }
    function sortMilestone(){
        dashboard.milestones.sort(function(a, b) {
            return a.localeCompare(b);
        });
    }
}

