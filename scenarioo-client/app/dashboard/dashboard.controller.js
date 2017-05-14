

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
            feature.features.isCollapsed=val;
            for(var i = 0; i < feature.features.length; i++){
                collapseAll(feature.features[i], val);
            }
        }
    }

    dashboard.contains = function (feature, field) {
        return feature[field] != null;
    };

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

