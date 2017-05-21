var CURRENT_FEATURE = 'currentFeature';

var SUCCESS = 'success';
var FAILED = 'failed';

angular.module('scenarioo').service('FeatureService',
    function FeatureService ($rootScope, SelectedBranchAndBuildService, UseCasesResource, SelectedComparison, BuildDiffInfoResource, UseCaseDiffInfosResource, UseCaseDiffInfoResource, DiffInfoService, ScenarioDiffInfosResource, $location) {
    var service = this;

    var rootFeature = {
        name: 'Project',
        features: []
    };

    var selectedFeature = rootFeature;

    function loadBackRefs(feature, backref) {
        if (feature == null || feature == undefined) return;
        feature.parentFeature = backref;
        if (feature.features != undefined && feature.features != null){
            for (var i = 0; i < feature.features.length; i++){
                loadBackRefs(feature.features[i], feature);
            }
        }
    }

    service.getSelectedFeatureNames = function () {
        var featureString = getCurrentFeatures()[branch][build];
        var featuresArr = featureString.split('/');
        return featuresArr;
    };

    service.selectFromArray = function (array) {
        var str = array.join('/');
        $location.search('feature', str);
    };

    service.loadUseCases = function loadUseCases(selected) {
        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(useCases) {
                if(SelectedComparison.isDefined()) {
                    loadDiffInfoData(useCases, selected.branch, selected.build, SelectedComparison.selected());
                } else {
                    setInternalAfterLoad(useCases, selected.branch, selected.build);
                }
            });
    };

    function getFeatureByArray(features, featuresArray, selectedFeature) {
        if (features == undefined) return selectedFeature;
        if (featuresArray.length > 0 && features.length > 0) {
            var current = featuresArray[0];
            featuresArray.shift();
            for (var i = 0; i < features.length; i++){
                if (features[i] == null) continue;
                if (features[i].name === current){
                    selectedFeature = getFeatureByArray(features[i].features, featuresArray, features[i]);
                }
            }
        }
        return selectedFeature;
    }

    function loadFeature(){
        var featureString = getCurrentFeatures()[branch][build];

        var featuresArr = featureString.split('/');
        var selectFeature = rootFeature;
        if (rootFeature.name === featuresArr[0]){
            featuresArr.shift();
            selectFeature = getFeatureByArray(rootFeature.features, featuresArr, selectFeature);
        }

        selectedFeature = selectFeature;
    }

    function getFeatureString(feature, featureString){
        if (feature == undefined || feature.parentFeature == null || feature.parentFeature == undefined){
            return featureString;
        }
        return getFeatureString(feature.parentFeature, feature.parentFeature.name+'/'+featureString);
    }

    function getCurrentFeatures() {
        var currentFeaturesString = localStorage.getItem(CURRENT_FEATURE);
        var currentFeatures = undefined;
        if (currentFeaturesString != 'undefined'){
            var currentFeatures = JSON.parse(currentFeaturesString);
        }
        if (currentFeatures == undefined) {
            currentFeatures = {};
        }
        if (currentFeatures[branch] == undefined) {
            currentFeatures[branch] = {};
            currentFeatures[branch][build] = '';
            localStorage.setItem(CURRENT_FEATURE, JSON.stringify(currentFeatures));
        }
        if (currentFeatures[branch][build] == undefined){
            currentFeatures[branch][build] = '';
            localStorage.setItem(CURRENT_FEATURE, JSON.stringify(currentFeatures));
        }

        var params = $location.search();
        if (params !== null && angular.isDefined(params['feature'])) {
            value = params['feature'];
            currentFeatures[branch][build] = value;
        }

        return currentFeatures;
    }

    service.setFeature = function setFeature(feature) {
        var currentFeatures = getCurrentFeatures();
        currentFeatures[branch][build] = getFeatureString(feature, feature.name);
        localStorage.setItem(CURRENT_FEATURE, JSON.stringify(currentFeatures));
        $location.search('feature', currentFeatures[branch][build]);
        loadFeature();
        //selectedFeature = feature;
    };

        $rootScope.$watch(function () {
            return $location.search()['feature'];
        }, function () {
            loadFeature();
        });

    service.getFeature = function () {
        return selectedFeature;
    };

    service.getRootFeature = function () {
        return rootFeature;
    };

    service.getMilestones = function () {
        return milestones;
    };

    var branch = '';
    var build = '';

    function setInternalAfterLoad(features, baseBranchName, baseBuildName) {
        rootFeature.features = features;
        rootFeature.name = 'Home';
        loadBackRefs(rootFeature, null);
        loadFeature();
        getAllMilestones();
        calcStati();
    }

    function calcStati() {
        getStati(rootFeature);
    }

        function getFeatureStatus(feature) {
            var status = 'none';
            if(feature.success > 0){
                status = 'success';
            }
            if(feature.ignored > 0){
                status = 'ignored';
            }
            if(feature.failed > 0){
                status = 'failed';
            }
            return status;
        }

        function getStati(feature) {
        if (!def(feature))return;

        var ignored = 0;
        var failed = 0;
        var success = 0;
        var i = 0;
        if (def(feature.scenarios)){
            for (i = 0; i < feature.scenarios.length; i++){
                if (!def(feature.scenarios[i].pageSteps) || !def(feature.scenarios[i].pageSteps.pagesAndSteps)) {
                    continue;
                }
                for (var j = 0; j < feature.scenarios[i].pageSteps.pagesAndSteps.length; j++){
                    if (!def(feature.scenarios[i].pageSteps.pagesAndSteps[j].steps)) {
                        continue;
                    }
                    for (var k = 0; k < feature.scenarios[i].pageSteps.pagesAndSteps[j].steps.length; k++){
                        if (!def(feature.scenarios[i].pageSteps.pagesAndSteps[j].steps[k].status)){
                            ignored++;
                            continue;
                        }
                        if (feature.scenarios[i].pageSteps.pagesAndSteps[j].steps[k].status === SUCCESS){
                            success++;
                        } else if (feature.scenarios[i].pageSteps.pagesAndSteps[j].steps[k].status === FAILED){
                            failed++;
                        } else {
                            ignored++;
                        }
                    }
                }
            }
        }

        if (def(feature.features)){
            for (i = 0; i < feature.features.length; i++){
                getStati(feature.features[i]);
            }

            for (i = 0; i < feature.features.length; i++){
                ignored += feature.features[i].ignored;
                failed += feature.features[i].failed;
                success += feature.features[i].success;
            }
        }
        feature.ignored = ignored;
        feature.failed = failed;
        feature.success = success;

        feature.status = getFeatureStatus(feature);

    }

        function def(val) {
            if (val == null)
                return false;
            if (val == undefined)
                return false;
            return val !== 'undefinded';
        }

        function loadScenariosDiffInfo(useCases, baseBranchName, baseBuildName, comparisonName, func) {
            var retUseCases = [];
            for(var i = 0; i < useCases.length; i++){
                loadScenariosDiffInfoInt(useCases[i], baseBranchName, baseBuildName, comparisonName, function (useCase) {
                    retUseCases.push(useCase);
                    if (retUseCases.length == useCases.length){
                        func(retUseCases);
                    }
                });
            }
        }

        function loadScenariosDiffInfoInt(feature, baseBranchName, baseBuildName, comparisonName, func) {
            if(!def(feature)) return;
            var featuresToAdd = [];
            var subsReady = false;
            var selfReady = false;
            var featureDiffReady = false;

            UseCaseDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'useCaseName': feature.folderName},
                function onSuccess(useCaseDiffInfo) {
                    feature.diffInfo = useCaseDiffInfo;

                    if (def(feature.scenarios)){
                        ScenarioDiffInfosResource.get(
                            {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'useCaseName': feature.folderName},
                            function onSuccess(scenarioDiffInfos) {
                                feature.scenarios = DiffInfoService.getElementsWithDiffInfos(feature.scenarios, useCaseDiffInfo.removedElements, scenarioDiffInfos, 'scenario.name');
                                selfReady = true;
                                ready();
                            }, function onFailure() {
                                selfReady = true;
                                ready();
                            }
                        );
                    }

                    featureDiffReady = true;
                    ready();
                }, function onFailure() {
                    featureDiffReady = true;
                    ready();
                }
            );



            if (def(feature.features)){
                for (var i = 0; i < feature.features.length; i++){

                    loadScenariosDiffInfoInt(feature.features[i], baseBranchName, baseBuildName, comparisonName, function (returnFeture) {
                        featuresToAdd.push(returnFeture);
                        if (featuresToAdd.length === feature.features.length){
                            subsReady = true;
                            ready();
                        }
                    });
                }
                if (feature.features.length === 0){
                    subsReady = true;
                    ready();
                }
            } else {
                subsReady = true;
                ready();
            }

            function ready() {
                if (subsReady && selfReady && featureDiffReady){
                    feature.features = featuresToAdd;
                    func(feature);
                }
            }
        }

        function loadDiffInfoData(useCases, baseBranchName, baseBuildName, comparisonName) {
            if(useCases && baseBranchName && baseBuildName) {
                loadScenariosDiffInfo(useCases, baseBranchName, baseBuildName, comparisonName, function (useCasesToAdd) {
                    setInternalAfterLoad(useCasesToAdd, baseBranchName, baseBuildName);
                });
            }
        }

    SelectedBranchAndBuildService.callOnSelectionChange(function(selected){
        reloadFromBranchBuild(selected);
    });

    $rootScope.$watch(function () {
        return SelectedComparison.selected();
    }, function () {
        reloadFromBranchBuild(SelectedBranchAndBuildService.selected());
    });

    function reloadFromBranchBuild(selected) {
        branch = selected.branch;
        build = selected.build;
        service.loadUseCases(selected);
    }



        var milestones = [];


        function getAllMilestones(){
            milestones = getMilestone(rootFeature);
            sortMilestone();
        }

        function getMilestone(feature){
            var milestones = [];
            feature.features.forEach(function(currentFeature){
                if (currentFeature == null)return;
                if(currentFeature.milestone != null && milestones.indexOf(currentFeature.milestone) === -1){
                    milestones.push(currentFeature.milestone);
                }
                if(currentFeature.features!=null){
                    var childMilestones = getMilestone(currentFeature);
                    for (var i = 0; i < childMilestones.length; i++){
                        if(milestones.indexOf(childMilestones[i]) === -1){
                            milestones.push(childMilestones[i]);
                        }
                    }
                }
            });
            return milestones;
        }
        function sortMilestone(){
            milestones.sort(function(a, b) {
                if (typeof a === 'string' && typeof b === 'string'){
                    return a.localeCompare(b);
                }
                return 0;
            });
        }

    });
