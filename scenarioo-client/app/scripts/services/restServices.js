'use strict';

angular.module('scenarioo.services').constant('REST_API_URL', '/scenarioo/rest');

angular.module('scenarioo.services').config(function ($httpProvider) {
    $httpProvider.defaults.headers.common.Accept = 'application/json';
});

angular.module('scenarioo.services').factory('HostnameAndPort', function (ENV) {
    var hostAndPort;

    if (ENV === 'production') {
        hostAndPort = '';
    } else if (ENV === 'development') {
        hostAndPort = 'http://localhost:8080';
    }

    return {
        forNgResource: function () {
            return hostAndPort.replace(/(:[0-9])/, '\\$1');
        },

        forLink: function () {
            return hostAndPort;
        }
    };
});

angular.module('scenarioo.services').factory('ScenariooResource', function (HostnameAndPort, $resource) {
    return function (url, paramDefaults, actions) {
        return $resource(HostnameAndPort.forNgResource() + '/scenarioo/rest' + url, paramDefaults, actions);
    };
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

angular.module('scenarioo.services').factory('UseCaseService', function (ScenariooResource, $q) {
    var useCaseService = ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName',
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

angular.module('scenarioo.services').factory('PageVariantService', function (ScenariooResource, $q) {
    var pageVariantService = ScenariooResource('/branches/:branchName/builds/:buildName/search/pagevariants/',
        {   branchName: '@branchName',
            buildName: '@buildName'}, {});

    pageVariantService.getPageVariantCount = getPromise($q, function (parameters, fnSuccess, fnError) {
        return pageVariantService.query(parameters, fnSuccess, fnError);
    });
    return pageVariantService;
});


angular.module('scenarioo.services').factory('ScenarioService', function (ScenariooResource, $q) {
    var scenarioService = ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName',
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

angular.module('scenarioo.services').factory('StepService', function (ScenariooResource, $q) {
    var stepService = ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName/steps/:stepIndex',
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

angular.module('scenarioo.services').factory('AdminService', function (ScenariooResource, $q) {
    var adminService = ScenariooResource('/admin/update', {});

    adminService.updateData = getPromise($q, function (parameters, fnSuccess, fnError) {
        return adminService.get(parameters, fnSuccess, fnError);
    });

    return adminService;
});

angular.module('scenarioo.services').factory('ConfigResource', function (ScenariooResource) {
    return ScenariooResource('/configuration', {});
});

angular.module('scenarioo.services').factory('BranchesResource', function (ScenariooResource) {
    return ScenariooResource('/branches', {}, {});
});

angular.module('scenarioo.services').factory('UseCasesResource', function (ScenariooResource) {
    return ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName',
        {
            branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName'
        }, {});
});