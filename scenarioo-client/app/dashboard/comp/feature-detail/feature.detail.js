angular.module('scenarioo').component('featureDetail', {
    templateUrl: 'dashboard/comp/feature-detail/feature.detail.html',
    controllerAs: 'card',
    bindings: {
        feature: '=',
    },
    controller:function (FeatureService) {
        var card = this;
        card.clickFeature = FeatureService.clickFeature;
    }
});
