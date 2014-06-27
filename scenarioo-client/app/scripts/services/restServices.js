/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('scenarioo.services').config(function ($httpProvider) {
    $httpProvider.defaults.headers.common.Accept = 'application/json';
});

angular.module('scenarioo.services').factory('HostnameAndPort', function (ENV) {
    var hostAndPort;

    if (ENV === 'production') {
        hostAndPort = '';
    } else if (ENV === 'development') {
        hostAndPort = 'http://localhost:8080/scenarioo/';
    }

    return {
        forNgResource: function () {
            return hostAndPort.replace(/(:[0-9])/, '\\$1');
        },
        forTest: function () {
            return hostAndPort;
        },
        forLink: function () {
            return hostAndPort;
        }
    };
});

angular.module('scenarioo.services').factory('ScenariooResource', function (HostnameAndPort, $resource) {
    return function (url, paramDefaults, actions) {
        return $resource(HostnameAndPort.forNgResource() + 'rest' + url, paramDefaults, actions);
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


angular.module('scenarioo.services').factory('BranchesResource', function (ScenariooResource) {
    return ScenariooResource('/branches', {}, {});
});

angular.module('scenarioo.services').factory('BuildImportStatesResource', function (ScenariooResource) {
    return ScenariooResource('/builds/buildImportSummaries', {}, {});
});

angular.module('scenarioo.services').factory('BuildImportLogResource', function (HostnameAndPort, $http) {
    return {
        get: function(branchName, buildName, onSuccess, onError) {
            var callURL = HostnameAndPort.forLink() + 'rest/builds/importLogs/' + encodeURIComponent(branchName) + '/' + encodeURIComponent(buildName);
            $http({method: 'GET', url: callURL}).success(onSuccess).error(onError);
        }
    };

});

angular.module('scenarioo.services').factory('BuildImportService', function (ScenariooResource, $q) {
    var buildImportService = ScenariooResource('/builds/updateAndImport', {});
    buildImportService.updateData = getPromise($q, function (parameters, fnSuccess, fnError) {
        return buildImportService.get(parameters, fnSuccess, fnError);
    });
    return buildImportService;
});

angular.module('scenarioo.services').factory('BuildReimportResource', function (ScenariooResource) {
    return ScenariooResource('/builds/reimportBuild/:branchName/:buildName',
        {   branchName: '@branchName',
            buildName: '@buildName'}, {});
});

angular.module('scenarioo.services').factory('UseCaseService', function (ScenariooResource, $q) {
    var useCaseService = ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName',
        {   branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName'}, {});

    useCaseService.getUseCase = getPromise($q, function (parameters, fnSuccess, fnError) {
        return useCaseService.get(parameters, fnSuccess, fnError);
    });
    return useCaseService;
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

angular.module('scenarioo.services').factory('ConfigResource', function (ScenariooResource) {
    return ScenariooResource('/configuration', {});
});

angular.module('scenarioo.services').factory('UseCasesResource', function (ScenariooResource) {
    return ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName',
        {
            branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName'
        }, {});
});

angular.module('scenarioo.services').factory('ScenarioResource', function (ScenariooResource) {
    return ScenariooResource('/branches/:branchName/builds/:buildName/usecases/:usecaseName/scenarios/:scenarioName',
        {
            branchName: '@branchName',
            buildName: '@buildName',
            usecaseName: '@usecaseName',
            scenarioName: '@scenarioName'
        }, {});
});

angular.module('scenarioo.services').factory('ObjectsForTypeResource', function (ScenariooResource) {
    return ScenariooResource('/branches/:branchName/builds/:buildName/objects/service',
        {
            branchName: '@branchName',
            buildName: '@buildName'
        }, {});
});

angular.module('scenarioo.services').factory('VersionResource', function (ScenariooResource) {
    return ScenariooResource('/version', {}, {});
});