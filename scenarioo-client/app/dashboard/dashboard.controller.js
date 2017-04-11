

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController(FeatureService, $rootScope, $scope, $location, $http){

    var dashboard = this;

    dashboard.isCollapsed = false;

    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'releaseDate';

    dashboard.clickFeature = function(subFeature, location){
        console.log('test');

        FeatureService.setFeature(subFeature);
        dashboard.feature = subFeature;
        console.log('loc' + location);
        $location.path(location);
    };

    dashboard.computeDown = function(feature, field) {
        var calc = 0;
        if (feature.hasOwnProperty(field)) {
            calc = feature[field];
        }
        if (feature.hasOwnProperty('features')) {
            for (var i = 0; i < feature.features.length; i++) {
                calc += dashboard.computeDown(feature.features[i], field);
            }
        }
        return calc;
    };

    dashboard.rootFeature = FeatureService.getRootFeature();
    dashboard.feature = FeatureService.getFeature();

    dashboard.expandAll = function() {
        dashboard.feature.markdown.forEach(function (markdown) {markdown.isCollapsed=false;});
        dashboard.feature.features.forEach(function (feature) {feature.isCollapsed=false;});
    };
    dashboard.collapseAll = function () {
        dashboard.feature.markdown.forEach(function (markdown) {markdown.isCollapsed=true;});
        dashboard.feature.features.forEach(function (feature) {feature.isCollapsed=true;});
    };
}
