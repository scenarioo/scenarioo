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

    var milestones = [];


    function getAllMilestones(){
        milestones = getMilestone(rootFeature);
        sortMilestone();
    }

    function pushMilestoneString(str) {
    }

    function getMilestone(feature){
        var milestones = [];
        console.log("getMilestone", feature, feature.features, feature.features.length);
        feature.features.forEach(function(currentFeature){
            if (currentFeature == null)return;
            if(currentFeature.milestone != null && milestones.indexOf(currentFeature.milestone) === -1){
                milestones.push(currentFeature.milestone);
            }
            console.log("push milestone", currentFeature.milestone);
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



    service.loadUseCases = function loadUseCases(selected) {
        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(useCases) {
                rootFeature.features = useCases;
                rootFeature.name = selected.branch+ ' '+ selected.build;
                getAllMilestones();
                //service.setFeature(rootFeature); //TODO remove when nav ready
            });
    };

    function loadFeature(){
        var featureString = localStorage.getItem(CURRENT_FEATURE);
        if (featureString !== 'undefined'){
            selectedFeature = JSON.parse(featureString);
        }else{
            selectedFeature = rootFeature;
        }
    }

    service.setFeature = function setFeature(feature) {
        localStorage.setItem(CURRENT_FEATURE, JSON.stringify(feature));
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

    loadFeature();//TODO load from local storage... on page load

    SelectedBranchAndBuildService.callOnSelectionChange(function(selected){
        service.loadUseCases(selected);
    });
});
