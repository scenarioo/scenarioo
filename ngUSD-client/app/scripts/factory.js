'use strict';

var RestServerPath = "http://localhost:port/ngUSD-server/rest";
var RestServerPort = ":8050";

NgUsdClientApp.config(function($httpProvider){
        $httpProvider.defaults.headers.common['Accept'] = 'application/json';
});

NgUsdClientApp.factory('BranchService', function($resource) {

    var branchService = $resource(RestServerPath +'/branches', {
        port: RestServerPort
    }, {});

    branchService.findAllBranches = function(fn) {
        return branchService.query(fn);
    };

    return branchService;
});