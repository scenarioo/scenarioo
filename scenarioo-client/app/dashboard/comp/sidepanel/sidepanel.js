
angular.module('scenarioo').component('sidePanel', {
    templateUrl: 'dashboard/comp/sidepanel/sidepanel.html',
    controllerAs: 'sidepanel',
    transclude: {
        header: '?header',
        content: '?content'
    },
    bindings: {

    }
});
