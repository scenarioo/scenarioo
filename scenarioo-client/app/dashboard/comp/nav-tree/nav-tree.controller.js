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
            if(navTree.currentFeature.folderName===navTree.feature.folderName)
                return 'white';
            return 'black';
        };*/
        navTree.getBGColor = function () {
            if(navTree.currentFeature.folderName===navTree.feature.folderName)
                return color;
            return 'white';
        };

    }
});
