function SiteNavigationController($location){
    var site = this;
    site.links = [
        {
            name: 'detailNav',
            short: 'Doku'
        },{
            name: 'dashboard',
            short: 'Dashboard'
        },{
            name: 'list',
            short: 'Scenarios'
        },{
            name: 'tree',
            short: 'Feature-Tree'
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
