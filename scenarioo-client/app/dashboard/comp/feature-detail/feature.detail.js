angular.module('scenarioo').component('featureDetail', {
    templateUrl: 'dashboard/comp/feature-detail/feature.detail.html',
    controllerAs: 'card',
    bindings: {
        cardColor: '@',
        feature: '=',
        click: '=',
    }
});
