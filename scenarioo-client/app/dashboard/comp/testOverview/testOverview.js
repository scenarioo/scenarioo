angular.module('scenarioo').component('testOverview', {
    templateUrl: 'dashboard/comp/testOverview/testOverview.html',
    controllerAs: 'overview',
    bindings: {
        feature: '=',
    }
});
