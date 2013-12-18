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