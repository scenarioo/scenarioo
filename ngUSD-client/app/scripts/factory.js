'use strict';

var RestServerPath = "http://localhost:port/ngusd/rest";
var RestServerPort = ":8050";

NgUsdClientApp.config(function ($httpProvider) {
    $httpProvider.defaults.headers.common['Accept'] = 'application/json';
});

NgUsdClientApp.factory('BranchService', function ($resource) {

    var branchService = $resource(RestServerPath + '/branches', {
        port: RestServerPort
    }, {});

    branchService.findAllBranches = function (fn) {
        return branchService.query(fn);
    };

    return branchService;
});

NgUsdClientApp.factory('UseCaseService', function ($resource) {
    var useCaseService = $resource(RestServerPath + '/branches/:branchName/builds/:buildName/usecases/:usecaseName',
        {branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            port: RestServerPort}, {});

    useCaseService.findAllUseCases = function (branchName, buildName) {
        return useCaseService.query({'branchName': branchName, 'buildName': buildName});
    }
    useCaseService.getUseCase = function (branchName, buildName, usecaseName, fn, error) {
        return useCaseService.get({'branchName': branchName, 'buildName': buildName, 'usecaseName': usecaseName}, fn, function (response) {

        });
    }
    return useCaseService;

});


NgUsdClientApp.factory('ScenarioService', function ($resource) {
    var scenarioService = $resource(RestServerPath + '/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName',
        {
            branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            scenarioName: '@scenarioName',
            port: RestServerPort}, {});

    scenarioService.findAllScenarios = function (branchName, buildName, usecaseName, fn) {
        return scenarioService.get({'branchName': branchName, 'buildName': buildName, 'usecaseName': usecaseName}, fn);
    }

    scenarioService.getScenario = function(branchName, buildName, usecaseName, scenarioName, fn) {
        return scenarioService.get({'branchName': branchName, 'buildName': buildName, 'usecaseName': usecaseName, 'scenarioName': scenarioName}, fn)
    }

    return scenarioService;

});

NgUsdClientApp.factory('PagesService', function ($resource) {
    var pagesService = $resource(RestServerPath + '/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName/pages/:pageName',
        {branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            scenarioName: '@scenarioName',
            pageName: '@pageName',
            port: RestServerPort}, {});

    pagesService.listAllPages = function(branchName, buildName, usecaseName, scenarioName, fn) {
        return pagesService.get({'branchName': branchName, 'buildName': buildName, 'usecaseName': usecaseName, 'scenarioName': scenarioName}, fn);
    }

    return pagesService;

});

NgUsdClientApp.factory('BuildStateService', function ($resource) {
    var buildStateService = $resource(RestServerPath + '/configuration/buildstates/:state',
        {state: '@state',
            port: RestServerPort},
        {
            ListBuildStates: { method: "GET", params: {} }
        });

    return buildStateService;

});

NgUsdClientApp.factory('AdminService', function ($resource) {
    var buildStateService = $resource(RestServerPath + '/admin/update',
        {
            port: RestServerPort},
        {
            UpdateData: { method: "GET", params: {} }
        });

    return buildStateService;

});

NgUsdClientApp.factory('ConfigService', function ($resource) {
    var buildStateService = $resource(RestServerPath + '/configuration/',
        {
            port: RestServerPort},
        {
            UpdateConfiguration: { method: "PUT", params: {} },
            GetConfiguration: { method: "GET", params: {} }
        });

    return buildStateService;

});