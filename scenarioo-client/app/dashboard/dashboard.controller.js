

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);

function DashboardController($rootScope, $scope, $location, $http){

    var dashboard = this;


    var date = new Date();

    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'releaseDate';
;
    var features = [
        {
            name: 'feature 1',
            storyOrderNumber: 2,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+1)
        },{
            name: 'feature 2',
            storyOrderNumber: 1,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+5)
        },{
            name: 'feature 3',
            storyOrderNumber: 0,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+3)
        },{
            name: 'feature 4',
            storyOrderNumber: 3,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+6)
        },{
            name: 'feature 5',
            storyOrderNumber: 4,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+2)
        },{
            name: 'feature 6',
            storyOrderNumber: 5,
            releaseDate: new Date().setFullYear(date.getFullYear(), date.getMonth(), date.getDay()+4)
        },
    ];

    dashboard.features = features;
    dashboard.features[0].features = features;
    dashboard.features[1].features = features;
    dashboard.features[1].features[1].features = features;

}
