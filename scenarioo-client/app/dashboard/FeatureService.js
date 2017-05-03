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

    service.loadUseCases = function loadUseCases(selected) {
        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(useCases) {
                rootFeature.features = useCases;
                rootFeature.name = selected.branch+ ' '+ selected.build;
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

    loadFeature();//TODO load from local storage... on page load

    SelectedBranchAndBuildService.callOnSelectionChange(function(selected){
        service.loadUseCases(selected);
    });
});
