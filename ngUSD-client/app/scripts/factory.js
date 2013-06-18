'use strict';

var RestServerPath = "http://localhost:port/ngusd-web/rest";
var RestServerPort = ":8080";

NgUsdClientApp.config(function($httpProvider){
        $httpProvider.defaults.headers.common['Accept'] = 'application/json';

        // Only needed for cross domain
        // $httpProvider.defaults.useXDomain = true;
        // delete $httpProvider.defaults.headers.common['X-Requested-With'];
});

NgUsdClientApp.factory('BranchService', function($resource) {

    var branchService = $resource(RestServerPath +'/branches', {
        port: RestServerPort
    }, {});

    branchService.findAllBranches = function() {
        return branchService.query({}, function() {
            alert("juhu");
        });
    };

    return branchService;
});