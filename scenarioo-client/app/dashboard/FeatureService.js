var CURRENT_FEATURE = 'currentFeature';
var SUCCESS = 'success';
var FAILED = 'failed';

angular.module('scenarioo').service('FeatureService',
    function FeatureService ($rootScope, SelectedBranchAndBuildService, UseCasesResource, SelectedComparison,
                             BuildDiffInfoResource, UseCaseDiffInfosResource, UseCaseDiffInfoResource, DiffInfoService,
                             ScenarioDiffInfosResource, $location) {
        var service = this;

        var rootFeature = {
            name: 'Project',
            features: []
        };

        var selectedFeature = rootFeature;

        function loadBackRefs(feature, backref) {
            if (!def(feature)) return;
            feature.parentFeature = backref;
            GetArray(feature.features).forEach(function (subFeature) {
                loadBackRefs(subFeature, feature);
            });
        }

        service.getSelectedFeatureNames = function () {
            var featureString = getCurrentFeatures()[branch][build];
            var featuresArr = featureString.split('/');
            return featuresArr;
        };

        service.selectFromArray = function (array) {
            var loc = localStorage.getItem('latestView');
            if (array.length===1){
                loc='feature';
            }
            var str = array.join('/');
            var pos = '/' + loc + '?feature=' + str;
            $location.url(pos);
        };

        service.loadUseCases = function (selected) {
            UseCasesResource.query(
                {'branchName': selected.branch, 'buildName': selected.build},
                function onSuccess(useCases) {
                    setInternalAfterLoad(useCases, selected);
                });
        };

        service.contains = function contains(feature, field) {
            return feature[field] != null;
        };

        service.equals = function equals (feat1, feat2) {
            return feat1 === feat2;
        };

        service.clickFeature = function clickFeature(subFeature, location){
            setFeature(subFeature);
            if (location && location !== ''){
                $location.path(location);
            }
        };

        function getFeatureByArray(currentFeature, featureNames, selectedFeature) {
            if (featureNames.length === 0                ||
                !def(currentFeature)                     ||
                featureNames[0] !== currentFeature.name) {
                return selectedFeature;
            }
            selectedFeature = currentFeature;
            featureNames.shift();

            GetArray(currentFeature.features).forEach(function (subFeature) {
                selectedFeature = getFeatureByArray(subFeature, featureNames, selectedFeature)
            });
            return selectedFeature;
        }

        function loadFeature(){
            var featureString = getCurrentFeatures()[branch][build];
            var featuresArr = featureString.split('/');
            selectedFeature = getFeatureByArray(rootFeature, featuresArr, rootFeature);
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
                currentFeatures = JSON.parse(currentFeaturesString);
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

        function setFeature(feature) {
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

        function setInternalAfterLoad(features, selected) {
            rootFeature.features = features;
            rootFeature.name = 'Home';
            if(SelectedComparison.isDefined()) {
                loadScenariosDiffInfoInt(rootFeature, selected.branch, selected.build, SelectedComparison.selected());
            }
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

        function GetArray(array) {
            if (def(array)) return array;
            return [];
        }

        function getStati(feature) {
            if (!def(feature))return;

            var ignored = 0;
            var failed = 0;
            var success = 0;
            GetArray(feature.scenarios).forEach(function (scenario) {
                if (!def(scenario.pageSteps)) return;

                GetArray(scenario.pageSteps.pagesAndSteps).forEach(function (pageSteps) {

                    GetArray(pageSteps.steps).forEach(function (step) {

                        if (!def(step.status)){
                            ignored++;
                            return;
                        }
                        if (step.status === SUCCESS){
                            success++;
                        } else if (step.status === FAILED){
                            failed++;
                        } else {
                            ignored++;
                        }
                    });
                });
            });

            GetArray(feature.features).forEach(function (subFeature) {
                getStati(subFeature);
            });
            GetArray(feature.features).forEach(function (subFeature) {
                ignored += subFeature.ignored;
                failed += subFeature.failed;
                success += subFeature.success;
            });
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

        function loadScenariosDiffInfoInt(feature, baseBranchName, baseBuildName, comparisonName) {
            if(!def(feature)) return;
            if (def(feature.id)) {
                var queryConfig = {
                    'baseBranchName': baseBranchName,
                    'baseBuildName': baseBuildName,
                    'comparisonName': comparisonName,
                    'useCaseName': feature.id
                };
                UseCaseDiffInfoResource.get(queryConfig, function onSuccess(useCaseDiffInfo) {
                    feature.diffInfo = useCaseDiffInfo;
                    if (def(feature.scenarios)) {
                        ScenarioDiffInfosResource.get(queryConfig, function onSuccess(scenarioDiffInfos) {
                            feature.scenarios = DiffInfoService.getElementsWithDiffInfos(feature.scenarios, useCaseDiffInfo.removedElements, scenarioDiffInfos, 'scenario.name');
                        });
                    }
                });
            }
            GetArray(feature.features).forEach(function (subFeature) {
                loadScenariosDiffInfoInt(subFeature, baseBranchName, baseBuildName, comparisonName);
            });
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
            milestones = [];
            getAllOfInArray(rootFeature, 'milestone', milestones);
            milestones = milestones.filter(uniq);
            milestones.sort();
        }

        function getAllOfInArray(feature, property, array){
            if (!def(feature)) return;
            if (def(feature[property])){
                array.push(feature[property]);
            }
            feature.features.forEach(function(currentFeature){
                getAllOfInArray(currentFeature, property, array);
            });
        }

        var uniq = function(m, i, a){ return i == a.indexOf(m) };
    });
