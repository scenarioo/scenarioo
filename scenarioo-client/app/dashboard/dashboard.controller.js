

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController(FeatureService, $rootScope,
                             $filter,SelectedComparison,
                             LabelConfigurationsResource, $location, $scope){

    var dashboard = this;

    dashboard.table = {
        search: {searchTerm: ''},
        sort: {column: 'name', reverse: false}
    };
    $scope.table = dashboard.table;

    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var dateTimeFormatter = $filter('scDateTime');
    dashboard.milestones = [];
    dashboard.isCollapsed = false;
    dashboard.isExplorerCollapsed = false;

    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'milestone';

    dashboard.navsize = (localStorage.getItem('MAV_SIZE_LEFT') != undefined)? localStorage.getItem('MAV_SIZE_LEFT'):200;

    dashboard.clickFeature = function(subFeature, location){
        FeatureService.setFeature(subFeature);
        if (location && location !== ''){
            $location.path(location);
        }
    };

    load();
    activate();

    dashboard.comparisonInfo = SelectedComparison.info;

    $rootScope.$watch(FeatureService.getFeature, function (feature) {
        dashboard.feature = feature;
        dashboard.comparisonInfo = SelectedComparison.info;
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

    function def(val) {
        if (val == null)
            return false;
        if (val == undefined)
            return false;
        return val !== 'undefinded';
    }

    dashboard.expandAll = function() {
        collapseAll(dashboard.feature, false);
    };

    dashboard.collapseAll = function () {
        collapseAll(dashboard.feature, true);
    };

    function collapseAll(feature, val) {
        if( !def(feature) ) return;

        if (def(feature.markdown))
            feature.markdown.isCollapsed=val;
        if (def(feature.specification))
            feature.specification.isCollapsed=val;
        if (def(feature.features)){
            feature.isCollapsed=val;
            for(var i = 0; i < feature.features.length; i++){
                collapseAll(feature.features[i], val);
            }
        }
    }

    dashboard.contains = function (feature, field) {
        return feature[field] != null;
    };

    function activate() {
        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            dashboard.labelConfigurations = labelConfiguratins;
        });
    }

    dashboard.handleClick = function(feature, scenarioSummary) {
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
                console.log(feature.features[i].features);
                return true;
            }
        }
        return false;
    }
}

