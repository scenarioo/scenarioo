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
var CURRENT_FEATURE = 'currentFeature';
var SUCCESS = 'success';
var FAILED = 'failed';
var DEFAULT_VIEW = 'feature';


angular.module('scenarioo.services').service('FeatureService',
    function FeatureService ($rootScope, SelectedBranchAndBuildService, FeaturesResource, SelectedComparison,
                             BuildDiffInfoResource, FeatureDiffInfoResource, DiffInfoService,
                             ScenarioDiffInfosResource, $location, LocalStorageNameService) {
        var service = this;
        var branch = '';
        var build = '';
        var rootFeature = {
            name: 'Project',
            id: 'Project',
            features: []
        };

        var selectedFeature = rootFeature;

        function loadBackRefs(feature, backref) {
            if (!isDefined(feature)) return;
            feature.parentFeature = backref;
            GetArray(feature.features).forEach(function (subFeature) {
                loadBackRefs(subFeature, feature);
            });
        }

        service.getSelectedFeatureNames = function () {
            var featureString = getCurrentFeatures()[branch][build];
            var featuresArray = featureString.split('/');
            return featuresArray;
        };

        service.selectFromArray = function (array) {
            var view = localStorage.getItem(LocalStorageNameService.LATEST_VIEW_NAME);
            if (array.length===1){
                view=DEFAULT_VIEW;
                localStorage.setItem(LocalStorageNameService.LATEST_VIEW_NAME, view);
            }
            var featurePath = array.join('/');
            var url = '/' + view + '?feature=' + featurePath;
            $location.url(url);
        };

        service.loadFeatures = function (selected) {
            FeaturesResource.query(
                {'branchName': selected.branch, 'buildName': selected.build},
                function onSuccess(features) {
                    setInternalAfterLoad(features, selected);
                });
        };

        service.contains = function(feature, field) {
            return feature[field] != null;
        };

        service.equals = function(feat1, feat2) {
            return feat1 === feat2;
        };

        service.clickFeature = function(subFeature, location){
            service.setFeature(subFeature);
            if (location && location !== ''){
                $location.path(location);
            }
        };

        function getFeatureByArray(currentFeature, subfeatures, selectedFeature) {
            if (subfeatures.length === 0                ||
                !isDefined(currentFeature)                     ||
                subfeatures[0] !== currentFeature.name) {
                return selectedFeature;
            }
            selectedFeature = currentFeature;
            subfeatures.shift();

            GetArray(currentFeature.features).forEach(function (subFeature) {
                selectedFeature = getFeatureByArray(subFeature, subfeatures, selectedFeature)
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
                currentFeatures[branch][build] = params['feature'];
            }
            return currentFeatures;
        }

        service.setFeature = function(feature) {
            var currentFeatures = getCurrentFeatures();
            currentFeatures[branch][build] = getFeatureString(feature, feature.name);
            localStorage.setItem(CURRENT_FEATURE, JSON.stringify(currentFeatures));
            $location.search('feature', currentFeatures[branch][build]);
            loadFeature();
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

        function setInternalAfterLoad(features, selected) {
            rootFeature.features = features;
            rootFeature.name = 'Home';
            if(SelectedComparison.isDefined()) {
                loadScenariosDiffInfoInt(rootFeature, selected.branch, selected.build, SelectedComparison.selected());
            }
            loadBackRefs(rootFeature, null);
            loadFeature();
            getAllMilestones();
            calculateStatus();
        }

        function calculateStatus() {
            getStatus(rootFeature);
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
            if (isDefined(array)) return array;
            return [];
        }

        function getStatus(feature) {
            if (!isDefined(feature))return;

            getScenarioStatusFrom(feature);
            GetArray(feature.features).forEach(function (subFeature) {
                getStatus(subFeature);
            });
            GetArray(feature.features).forEach(function (subFeature) {
                feature.ignored += subFeature.ignored;
                feature.failed += subFeature.failed;
                feature.success += subFeature.success;
            });
            feature.status = getFeatureStatus(feature);
        }
        function getScenarioStatusFrom(feature){
            feature.ignored = 0;
            feature.failed = 0;
            feature.success = 0;
            GetArray(feature.scenarios).forEach(function (scenario) {
                if (!isDefined(scenario.pageSteps)) return;

                GetArray(scenario.pageSteps.pagesAndSteps).forEach(function (pageSteps) {

                    GetArray(pageSteps.steps).forEach(function (step) {

                        if (!isDefined(step.status)){
                            feature.ignored++;
                            return;
                        }
                        if (step.status === SUCCESS){
                            feature.success++;
                        } else if (step.status === FAILED){
                            feature.failed++;
                        } else {
                            feature.ignored++;
                        }
                    });
                });
            });
        }

        function isDefined(val) {
            if (val == null)
                return false;
            if (val == undefined)
                return false;
            return val !== 'undefinded';
        }

        function loadScenariosDiffInfoInt(feature, baseBranchName, baseBuildName, comparisonName) {
            if(!isDefined(feature)) return;
            if (isDefined(feature.id)) {
                getFeatureDiffInfo(feature, baseBranchName, baseBuildName, comparisonName)
            }
            GetArray(feature.features).forEach(function (subFeature) {
                loadScenariosDiffInfoInt(subFeature, baseBranchName, baseBuildName, comparisonName);
            });
        }

        function getFeatureDiffInfo(feature, baseBranchName, baseBuildName, comparisonName) {
            var queryConfig = {
                'baseBranchName': baseBranchName,
                'baseBuildName': baseBuildName,
                'comparisonName': comparisonName,
                'featureName': feature.id
            };
            FeatureDiffInfoResource.get(queryConfig, function onSuccess(featureDiffInfo) {
                feature.diffInfo = featureDiffInfo;
                if (isDefined(feature.scenarios)) {
                    ScenarioDiffInfosResource.get(queryConfig, function onSuccess(scenarioDiffInfos) {
                        feature.scenarios = DiffInfoService.getElementsWithDiffInfos(feature.scenarios,
                            featureDiffInfo.removedElements, scenarioDiffInfos, 'scenario.name');
                    });
                }
            });
        }

        service.reloadFromBranchBuild = function(selected) {
            branch = selected.branch;
            build = selected.build;
            service.loadFeatures(selected);
        };

        SelectedBranchAndBuildService.callOnSelectionChange(function(selected){
            service.reloadFromBranchBuild(selected);
        });

        $rootScope.$watch(function () {
            return SelectedComparison.selected();
        }, function () {
            service.reloadFromBranchBuild(SelectedBranchAndBuildService.selected());
        });


        var unique = function(m, i, a){ return i == a.indexOf(m) };
        var milestones = [];
        function getAllMilestones(){
            milestones = [];
            getAllOfInArray(rootFeature, 'milestone', milestones);
            if (milestones.length > 0) {
                milestones = milestones.filter(unique);
                milestones.sort();
            }
        }

        function getAllOfInArray(feature, property, array){
            if (!isDefined(feature)) return;
            if (isDefined(feature[property])){
                array.push(feature[property]);
            }
            feature.features.forEach(function(currentFeature){
                getAllOfInArray(currentFeature, property, array);
            });
        }



        return service;
    });
