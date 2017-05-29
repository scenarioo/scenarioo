angular.module('scenarioo').service('SiteNavigationLinkService', function () {
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

function SiteNavigationController($location, LocalStorageNameService, SiteNavigationLinkService){
    var site = this;

    site.links = SiteNavigationLinkService.links;

    site.setView = function(viewName) {
        localStorage.setItem(LocalStorageNameService.LATEST_VIEW_NAME, viewName);
        console.log('change view to '+viewName);
        $location.path('/'+viewName+'/');
    };

}


angular.module('scenarioo').component('siteNavigation', {
    templateUrl: 'dashboard/comp/site-navigation/site-navigation.html',
    bindings: {
        current: '@'
    },
    controller: SiteNavigationController
});
