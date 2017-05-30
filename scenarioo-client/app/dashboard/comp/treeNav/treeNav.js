angular.module('scenarioo').component('treeNav', {
    templateUrl: 'dashboard/comp/treeNav/treeNav.html',
    controllerAs: 'treeNav',
    bindings: {
        rootFeature:'=',
        currentFeature:'=',
    },
    controller: function () {
        var treeNav = this;
        treeNav.showNav = true;

        treeNav.openNav = function () {
            if (!treeNav.showNav) {
                treeNav.showNav = true;
            }
        };

        treeNav.closeNav = function ($event) {
            if (treeNav.showNav){
                treeNav.showNav = false;
            }
            $event.stopPropagation();

        };
    }
});
