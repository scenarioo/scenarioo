angular.module('scenarioo').component('featureCard', {
    templateUrl: 'dashboard/comp/featureCard/featureCard.html',
    controllerAs: 'card',
    bindings: {
        cardColor: '@',
        feature: '=',
    }
});
