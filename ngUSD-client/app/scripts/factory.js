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

    branchService.findAllBranches = function() {
        return branchService.query({}, function() {
        });
    };

    return branchService;
});

NgUsdClientApp.factory('UseCaseService', function($resource) {

    //var useCaseService = $resource(RestServerPath +'/branches/'+NgUsdClientApp.branchId+'/builds/'+NgUsdClientApp.buildId+'/usecases/', {
    var useCaseService = $resource(RestServerPath +'/branches/current/builds/current/usecases/', {
        port: RestServerPort
    }, {});

    useCaseService.findAllUseCases = function() {
        return useCaseService.query({}, function() {});
    };

    return useCaseService;
});