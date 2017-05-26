angular.module('scenarioo').component('navTree', {
    templateUrl: 'dashboard/comp/nav-tree/nav-tree.html',
    controllerAs: 'navTree',
    bindings: {
        feature:'=',
        currentFeature:'=',
    },
    controller: function (FeatureService) {
        var navTree = this;
        var color = 'lightskyblue';          //'#0e90d2'
        navTree.clickFeature = FeatureService.clickFeature;

        navTree.getBGColor = function () {
            if(navTree.currentFeature.id===navTree.feature.id && ((navTree.currentFeature.parentFeature == null) || navTree.currentFeature.parentFeature.id===navTree.feature.parentFeature.id)){
                return color;
            }
            return 'white';
        };

    }
});
