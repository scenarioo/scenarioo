angular.module('scenarioo').component('treeNav', {
    templateUrl: 'dashboard/comp/treeNav/treeNav.html',
    controllerAs: 'treeNav',
    bindings: {
        rootFeature:'=',
        currentFeature:'=',
        clickFeature:'=',
    },
    controller: function ($scope) {
        var treeNav=this;
        treeNav.showNav=true;

        treeNav.openNav = function () {
            if (!treeNav.showNav)
                treeNav.showNav = true;
        };

        treeNav.closeNav = function ($event) {
            console.log('test');
            if (treeNav.showNav)
                treeNav.showNav = false;
            $event.stopPropagation();

        };
    }
});
