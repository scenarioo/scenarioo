angular.module('scenarioo').component('treeNav', {
    templateUrl: 'dashboard/treeNav/treeNav.html',
    controllerAs: 'treeNav',
    bindings: {
        rootFeature:'=',
        currentFeature:'=',
        clickFeature:'=',
    },
    controller: function () {
        var treeNav=this;
        treeNav.showNav=true;
    }
});
