angular.module('scenarioo').component('navTree', {
    templateUrl: 'dashboard/comp/nav-tree/nav-tree.html',
    controllerAs: 'navTree',
    bindings: {
        feature:'=',
        currentFeature:'=',
        clickFeature:'=',
    },
    controller: function () {
        var navTree = this;
        var color = 'lightskyblue';          //'#0e90d2'
/*
        navTree.getColor = function(){
            if(navTree.currentFeature.id===navTree.feature.id)
                return 'white';
            return 'black';
        };*/
        navTree.getBGColor = function () {
            if(navTree.currentFeature.id===navTree.feature.id && navTree.currentFeature.parentFeature.id===navTree.feature.parentFeature.id)
                return color;
            return 'white';
        };

    }
});
