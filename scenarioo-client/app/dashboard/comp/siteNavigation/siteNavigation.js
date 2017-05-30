angular.module('scenarioo.services').service('SiteNavigationLinkService', function () {
    var service = this;
    service.links = [
        {
            name: 'feature',
            short: 'Feature',
            template: 'dashboard/featureView.html'
        },{
            name: 'dashboard',
            short: 'Map',
            template: 'dashboard/dashboard.html'
        },{
            name: 'detailNav',
            short: 'Docs',
            template: 'dashboard/documentationView.html'
        },{
            name: 'testScenarios',
            short: 'Scenarios',
            template: 'dashboard/scenarioView.html'
        },
    ];
});

angular.module('scenarioo').component('siteNavigation', {
    templateUrl: 'dashboard/comp/siteNavigation/siteNavigation.html',
    controllerAs: 'siteNaviagtion',
    bindings: {
        current: '@'
    },
    controller: function ($location, LocalStorageNameService, SiteNavigationLinkService) {
        var siteNaviagtion = this;

        siteNaviagtion.links = SiteNavigationLinkService.links;

        siteNaviagtion.setView = function (viewName) {
            localStorage.setItem(LocalStorageNameService.LATEST_VIEW_NAME, viewName);
            $location.path('/' + viewName + '/');
        };
    }
});
