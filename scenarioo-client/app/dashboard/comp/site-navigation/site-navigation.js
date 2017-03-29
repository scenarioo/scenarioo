function SiteNavigationController($location){
    var site = this;
    site.links = [
        {
            name: 'detailNav',
            short: 'D'
        },{
            name: 'dashboard',
            short: 'S'
        },{
            name: 'accordion',
            short: 'A'
        },{
            name: 'list',
            short: 'L'
        },{
            name: 'tree',
            short: 'T'
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
