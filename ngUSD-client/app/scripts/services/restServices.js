'use strict';

angular.module('ngUSDClientApp.services').constant('REST_API_URL', '/ngusd/rest');

angular.module('ngUSDClientApp.services').config(function ($httpProvider) {
    $httpProvider.defaults.headers.common.Accept = 'application/json';
});

function getPromise($q, fn) {
    return function (parameters) {
        var deferred = $q.defer();
        fn(parameters, function (result) {
            deferred.resolve(result);
        }, function (error) {
            deferred.reject(error);
        });
        return deferred.promise;
    };
}

angular.module('ngUSDClientApp.services').factory('BranchService', function ($resource, $q, REST_API_URL) {

    var branchService = $resource(REST_API_URL + '/branches', {}, {});

    branchService.findAllBranches = getPromise($q, function (parameters, fnSuccess, fnError) {
        return branchService.query(parameters, fnSuccess, fnError);
    });

    return branchService;
});

angular.module('ngUSDClientApp.services').factory('UseCaseService', function ($resource, $q, REST_API_URL) {
    var useCaseService = $resource(REST_API_URL + '/branches/:branchName/builds/:buildName/usecases/:usecaseName',
        {   branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName'}, {});

    useCaseService.findAllUseCases = getPromise($q, function (parameters, fnSuccess, fnError) {
        return useCaseService.query(parameters, fnSuccess, fnError);
    });
    useCaseService.getUseCase = getPromise($q, function (parameters, fnSuccess, fnError) {
        return useCaseService.get(parameters, fnSuccess, fnError);
    });
    return useCaseService;
});

angular.module('ngUSDClientApp.services').factory('PageVariantService', function ($resource, $q, REST_API_URL) {
    var pageVariantService = $resource(REST_API_URL + '/branches/:branchName/builds/:buildName/search/pagevariants/',
        {   branchName: '@branchName',
            buildName: '@buildName'}, {});

    pageVariantService.getPageVariantCount = getPromise($q, function (parameters, fnSuccess, fnError) {
        return pageVariantService.query(parameters, fnSuccess, fnError);
    });
    return pageVariantService;
});


angular.module('ngUSDClientApp.services').factory('ScenarioService', function ($resource, $q, REST_API_URL) {
    var scenarioService = $resource(REST_API_URL + '/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName',
        {   branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            scenarioName: '@scenarioName'}, {});

    scenarioService.findAllScenarios = getPromise($q, function (parameters, fnSuccess, fnError) {
        return scenarioService.get(parameters, fnSuccess, fnError);
    });

    scenarioService.getScenario = getPromise($q, function (parameters, fnSuccess, fnError) {
        return scenarioService.get(parameters, fnSuccess, fnError);
    });

    return scenarioService;

});

angular.module('ngUSDClientApp.services').factory('StepService', function ($resource, $q, REST_API_URL) {
    var stepService = $resource(REST_API_URL + '/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName/steps/:stepIndex',
        {branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            scenarioName: '@scenarioName',
            stepIndex: '@stepIndex'}, {});

    stepService.getStep = getPromise($q, function (parameters, fnSuccess, fnError) {
        return stepService.get(parameters, fnSuccess, fnError);
    });

    return stepService;
});

angular.module('ngUSDClientApp.services').factory('BuildStateService', function ($resource, $q, REST_API_URL) {
    var buildStateService = $resource(REST_API_URL + '/configuration/buildstates', {}, {});

    buildStateService.ListBuildStates = getPromise($q, function (parameters, fnSuccess, fnError) {
        return buildStateService.get(parameters, fnSuccess, fnError);
    });
    return buildStateService;
});

angular.module('ngUSDClientApp.services').factory('AdminService', function ($resource, $q, REST_API_URL) {
    var adminService = $resource(REST_API_URL + '/admin/update', {});

    adminService.updateData = getPromise($q, function (parameters, fnSuccess, fnError) {
        return adminService.get(parameters, fnSuccess, fnError);
    });

    return adminService;
});


angular.module('ngUSDClientApp.services').factory('ConfigResource', function ($resource, REST_API_URL) {
    return $resource(REST_API_URL + '/configuration', {} );
});