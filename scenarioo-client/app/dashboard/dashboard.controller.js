

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController(FeatureService, $rootScope, $scope, $location, $http, $timeout, SelectedBranchAndBuildService,
                             $filter, BranchesAndBuildsService,SelectedComparison, DiffInfoService, UseCasesResource,
                             LabelConfigurationsResource, BuildDiffInfoResource, UseCaseDiffInfosResource){

    var dashboard = this;
    var transformMetadataToTree = $filter('scMetadataTreeCreator');
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var dateTimeFormatter = $filter('scDateTime');
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
    activate();

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

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(loadUseCases);

        LabelConfigurationsResource.query({}, function (labelConfiguratins) {
            dashboard.labelConfigurations = labelConfiguratins;
        });
    }
    function loadUseCases(selected) {
        BranchesAndBuildsService.getBranchesAndBuilds()
            .then(function onSuccess(branchesAndBuilds) {
                dashboard.branchesAndBuilds = branchesAndBuilds;

                UseCasesResource.query(
                    {'branchName': selected.branch, 'buildName': selected.build},
                    function onSuccess(useCases) {
                        if(SelectedComparison.isDefined()) {
                            loadDiffInfoData(useCases, selected.branch, selected.build, SelectedComparison.selected());
                        } else {
                            dashboard.useCases = useCases;
                        }

                        var branch = dashboard.branchesAndBuilds.selectedBranch.branch;
                        var build = dashboard.branchesAndBuilds.selectedBuild.build;
                        dashboard.branchInformationTree = createBranchInformationTree(branch);
                        dashboard.buildInformationTree = createBuildInformationTree(build);
                        dashboard.metadataTreeBranches = transformMetadataToTreeArray(branch.details);
                        dashboard.metadataTreeBuilds = transformMetadataToTreeArray(build.details);
                    });
            });
    }
    function loadDiffInfoData(useCases, baseBranchName, baseBuildName, comparisonName) {
        if(useCases && baseBranchName && baseBuildName) {
            BuildDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                function onSuccess(buildDiffInfo) {
                    UseCaseDiffInfosResource.get(
                        {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                        function onSuccess(useCaseDiffInfos) {
                            dashboard.useCases = DiffInfoService.getElementsWithDiffInfos(useCases, buildDiffInfo.removedElements, useCaseDiffInfos, 'name');
                        }
                    );
                }, function onFailure(error){
                    throw error;
                }
            );
        }
    }
    function createBranchInformationTree(branch) {
        var branchInformationTree = {};
        branchInformationTree.Description = branch.description;
        return transformMetadataToTree(branchInformationTree);
    }
    function createBuildInformationTree(build) {
        var buildInformationTree = {};
        buildInformationTree.Date = dateTimeFormatter(build.date);
        buildInformationTree.Revision = build.revision;
        buildInformationTree.Status = build.status;
        return transformMetadataToTree(buildInformationTree);
    }
}

