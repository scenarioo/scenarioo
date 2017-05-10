var CURRENT_FEATURE = 'currentFeature';

angular.module('scenarioo').service('FeatureService',
    function FeatureService (SelectedBranchAndBuildService, UseCasesResource) {
    var service = this;

    var rootFeature = {
        name: 'Project',
        features: [],
        markdown: []
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

    service.loadUseCases = function loadUseCases(selected) {
        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(useCases) {
                rootFeature.features = useCases;
                rootFeature.name = selected.branch+ ' '+ selected.build;
                loadBackRefs(rootFeature, null);
                loadFeature();
                getAllMilestones();
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
        return getFeatureString(feature.parentFeature, feature.parentFeature.name+"/"+featureString);
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
        return currentFeatures;
    }

    service.setFeature = function setFeature(feature) {
        currentFeatures = getCurrentFeatures();
        currentFeatures[branch][build] = getFeatureString(feature, feature.name);
        localStorage.setItem(CURRENT_FEATURE, JSON.stringify(currentFeatures));
        loadFeature();
        //selectedFeature = feature;
    };

    service.getFeature = function () {
        return selectedFeature;
    };

    service.getRootFeature = function () {
        return rootFeature;
    };

    service.getMilestones = function () {
        return milestones;
    };

    var branch = "";
    var build = "";

    SelectedBranchAndBuildService.callOnSelectionChange(function(selected){
        branch = selected.branch;
        build = selected.build;
        service.loadUseCases(selected);
    });



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
