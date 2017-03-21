

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);

function DashboardController($rootScope, $scope, $location, $http){

    var dashboard = this;


    dashboard.features = [
        {
            name: 'feature 1',
        },{
            name: 'feature 2',
        },{
            name: 'feature 3',
        },{
            name: 'feature 4',
        },{
            name: 'feature 5',
        },{
            name: 'feature 6',
        },
    ]

}
