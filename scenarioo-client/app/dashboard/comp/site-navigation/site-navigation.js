function SiteNavigationController($location){
    var site = this;
    site.links = [
        {
            name: 'feature',
            short: 'Feature'
        },{
            name: 'dashboard',
            short: 'Dashboard'
        },{
            name: 'detailNav',
            short: 'Doku'
        },{
            name: 'testScenarios',
            short: 'Scenarios'
        },
    ];

    site.setView = function(viewName) {

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
