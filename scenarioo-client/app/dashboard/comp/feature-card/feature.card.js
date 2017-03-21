angular.module('scenarioo').component('featureCard', {
    templateUrl: 'dashboard/comp/feature-card/feature.card.html',
    controllerAs: 'card',
    bindings: {
        cardColor: '@',
        feature: '=',
    }
});
