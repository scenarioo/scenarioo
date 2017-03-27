

angular.module('scenarioo.controllers').controller('DashboardController', DashboardController);


function DashboardController($rootScope, $scope, $location, $http){

    var dashboard = this;


    var date = new Date();
    var date2 = new Date().setFullYear(date.getFullYear(), date.getMonth()+3, date.getDay());

    dashboard.firstOrder = 'storyOrderNumber';
    dashboard.secondOrder = 'releaseDate';
    dashboard.setView=setView;
    
    var features = [
        {
            name: 'Maintain product Catalogue',
            storyOrderNumber: 1,
            releaseDate: date,
            features: [
                {
                    name: 'CRUD of Products',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Maintain Tag Hierarchy',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
            ]
        },{
            name: 'Browse Products',
            storyOrderNumber: 2,
            releaseDate: date,
            features: [
                {
                    name: 'View Product Detail',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Navigate by Tags',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Feature Product',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Advertise Specials',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Search Sauce',
                    storyOrderNumber: 1,
                    releaseDate: date2
                },
            ]
        },{
            name: 'Shopping Card',
            storyOrderNumber: 3,
            releaseDate: date2,
            features: [
                {
                    name: 'Maintain Cart',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Store Cart',
                    storyOrderNumber: 1,
                    releaseDate: date2
                },
            ]
        },{
            name: 'Checkout & Pay',
            storyOrderNumber: 4,
            releaseDate: date,
            features: [
                {
                    name: 'Complete Order',
                    storyOrderNumber: 1,
                    releaseDate: date
                },{
                    name: 'Confirm by Email',
                    storyOrderNumber: 1,
                    releaseDate: date
                },{
                    name: 'Order by Email',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Validate with Credit Card',
                    storyOrderNumber: 1,
                    releaseDate: date2
                },
            ]
        },{
            name: 'Process Order',
            storyOrderNumber: 5,
            releaseDate: date2
        },{
            name: 'Stock and Delivery',
            storyOrderNumber: 6,
            releaseDate: date
        },{
            name: 'Register Customer',
            storyOrderNumber: 7,
            releaseDate: date2,
            features: [
                {
                    name: 'Register Customer',
                    storyOrderNumber: 1,
                    releaseDate: date
                },{
                    name: 'Recover PW',
                    storyOrderNumber: 1,
                    releaseDate: date
                },{
                    name: 'Delete Customer',
                    storyOrderNumber: 1,
                    releaseDate: date
                },
                {
                    name: 'Blacklist Customer',
                    storyOrderNumber: 1,
                    releaseDate: date2
                },
            ]
        },{
            name: 'Gift Cards',
            storyOrderNumber: 8,
            releaseDate: date
        },
    ];

    dashboard.features = features;

    function setView(viewName) {
        console.log("change view");
        $location.path('/'+viewName+'/');
    }
}
