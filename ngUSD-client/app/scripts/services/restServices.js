'use strict';

angular.module('ngUSDClientApp.services').constant('REST_API_URL', '/ngusd/rest');

angular.module('ngUSDClientApp.services').config(function ($httpProvider) {
    $httpProvider.defaults.headers.common.Accept = 'application/json';
});

angular.module('ngUSDClientApp.services').factory('ngUSDResource', function(ENV, $resource, $http) {
    return function(url, params, actions) {
        if(ENV === 'production') {
            return $resource('/ngusd/rest' + url, params, actions);
        } else if (ENV === 'development') {
            return $resource('http://localhost\\:8080/ngusd/rest' + url, params, actions);
        }
    }
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

angular.module('ngUSDClientApp.services').factory('BranchService', function (ngUSDResource, $q) {

    var branchService = ngUSDResource('/branches', {}, {});

    branchService.findAllBranches = getPromise($q, function (parameters, fnSuccess, fnError) {
        return branchService.query(parameters, fnSuccess, fnError);
    });

    return branchService;
});

angular.module('ngUSDClientApp.services').factory('UseCaseService', function (ngUSDResource, $q) {
    var useCaseService = ngUSDResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName',
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

angular.module('ngUSDClientApp.services').factory('PageVariantService', function (ngUSDResource, $q) {
    var pageVariantService = ngUSDResource('/branches/:branchName/builds/:buildName/search/pagevariants/',
        {   branchName: '@branchName',
            buildName: '@buildName'}, {});

    pageVariantService.getPageVariantCount = getPromise($q, function (parameters, fnSuccess, fnError) {
        return pageVariantService.query(parameters, fnSuccess, fnError);
    });
    return pageVariantService;
});


angular.module('ngUSDClientApp.services').factory('ScenarioService', function (ngUSDResource, $q) {
    var scenarioService = ngUSDResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName',
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

angular.module('ngUSDClientApp.services').factory('StepService', function (ngUSDResource, $q) {
    var stepService = ngUSDResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName/steps/:stepIndex',
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

angular.module('ngUSDClientApp.services').factory('AdminService', function (ngUSDResource, $q) {
    var adminService = ngUSDResource('/admin/update', {});

    adminService.updateData = getPromise($q, function (parameters, fnSuccess, fnError) {
        return adminService.get(parameters, fnSuccess, fnError);
    });

    return adminService;
});

angular.module('ngUSDClientApp.services').factory('ConfigResource', function (ngUSDResource) {
    return ngUSDResource('/configuration', {} );
});