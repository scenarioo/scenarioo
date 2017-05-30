angular.module('scenarioo').component('navTree', {
    templateUrl: 'dashboard/comp/navTree/navTree.html',
    controllerAs: 'navTree',
    bindings: {
        feature:'=',
        currentFeature:'=',
    },
    controller: function (FeatureService) {
        var navTree = this;
        var color = 'lightskyblue';
        navTree.clickFeature = FeatureService.clickFeature;
        navTree.contains = FeatureService.contains;

        navTree.getBGColor = function () {
            if(navTree.currentFeature.id===navTree.feature.id && ((navTree.currentFeature.parentFeature == null) || navTree.currentFeature.parentFeature.id===navTree.feature.parentFeature.id)){
                return color;
            }
            return 'white';
        };

        navTree.hasSubfeature =function(){
            console.log(contains(navTree.feature, 'features'));
            return contains(navTree.feature, 'features');
        };

    }
});
