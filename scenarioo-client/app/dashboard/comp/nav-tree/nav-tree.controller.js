angular.module('scenarioo').component('navTree', {
    templateUrl: 'dashboard/comp/nav-tree/nav-tree.html',
    controllerAs: 'navTree',
    bindings: {
        feature:'=',
        currentFeature:'=',
        clickFeature:'=',
    }
});
