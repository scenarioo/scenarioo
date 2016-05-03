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

angular.module('scenarioo.services')
    .config(function ($httpProvider) {
        $httpProvider.defaults.headers.common.Accept = 'application/json';
        $httpProvider.defaults.stripTrailingSlashes = false;
    })

    .factory('HostnameAndPort', function (ENV, BASE_URL, $location) {
        var baseUrl = BASE_URL;

        var getBaseUrl = function () {
            var url = $location.absUrl();
            var urlParts = url.split('#');
            return urlParts[0];
        };

        return {
            forNgResource: function () {
                return baseUrl.replace(/(:[0-9])/, '\\$1');
            },
            forTest: function () {
                return baseUrl;
            },
            forLink: function () {
                return baseUrl;
            },
            forLinkAbsolute: function () {
                if (ENV === 'production') {
                    return getBaseUrl();
                } else if (ENV === 'development') {
                    return baseUrl;
                }
            }
        };
    })


    /**
     * All resources in Scenarioo must be based on this ScenariooResource.
     */
    .factory('ScenariooResource', function (HostnameAndPort, $resource) {
        return function (url, paramDefaults, actions) {
            return $resource(HostnameAndPort.forNgResource() + 'rest' + url, paramDefaults, actions);
        };
    })


    .factory('BranchesResource', function (ScenariooResource) {
        return ScenariooResource('/branches', {}, {});
    })

    .factory('BuildImportStatesResource', function (ScenariooResource) {
        return ScenariooResource('/builds/buildImportSummaries', {}, {});
    })

    .factory('BuildImportLogResource', function (HostnameAndPort, $http) {
        return {
            get: function (branchName, buildName, onSuccess, onError) {
                var callURL = HostnameAndPort.forLink() + 'rest/builds/importLogs/' + encodeURIComponent(branchName) + '/' + encodeURIComponent(buildName);
                $http({method: 'GET', url: callURL}).success(onSuccess).error(onError);
            }
        };
    })

    .factory('BuildReimportResource', function (ScenariooResource) {
        return ScenariooResource('/builds/reimportBuild/:branchName/:buildName',
            {
                branchName: '@branchName',
                buildName: '@buildName'
            }, {});
    })

    .factory('BuildImportService', function (ScenariooResource, $q) {
        var buildImportService = ScenariooResource('/builds/updateAndImport', {});
        buildImportService.updateData = getPromise($q, function (parameters, fnSuccess, fnError) {
            return buildImportService.get(parameters, fnSuccess, fnError);
        });
        return buildImportService;
    })

    .factory('PageVariantService', function (ScenariooResource, $q) {
        var pageVariantService = ScenariooResource('/branch/:branchName/build/:buildName/search/pagevariants/',
            {
                branchName: '@branchName',
                buildName: '@buildName'
            }, {});

        pageVariantService.getPageVariantCount = getPromise($q, function (parameters, fnSuccess, fnError) {
            return pageVariantService.get(parameters, fnSuccess, fnError);
        });
        return pageVariantService;
    })

    .factory('UseCaseService', function (ScenariooResource, $q) {
        var useCaseService = ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName'
            }, {});

        useCaseService.getUseCase = getPromise($q, function (parameters, fnSuccess, fnError) {
            return useCaseService.get(parameters, fnSuccess, fnError);
        });
        return useCaseService;
    })

    .factory('FullTextSearchService', function (ScenariooResource, $q) {
        var searchService = ScenariooResource('/branch/:branchName/build/:buildName/search/:q',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                q: '@q'
            }, {});

        searchService.search = getPromise($q, function (parameters, fnSuccess, fnError) {
            return searchService.query(parameters, fnSuccess, fnError);
        });
        return searchService;
    })

    .factory('StepResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName/scenario/:scenarioName/pageName/:pageName/pageOccurrence/:pageOccurrence/stepInPageOccurrence/:stepInPageOccurrence',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName',
                pageName: '@pageName',
                pageOccurrence: '@pageOccurrence',
                stepInPageOccurrence: '@stepInPageOccurrence',
                labels: '@labels'
            }, {});
    })

    .factory('ConfigResource', function (ScenariooResource) {
        return ScenariooResource('/configuration', {});
    })

    .factory('UseCasesResource', function (ScenariooResource) {
        return ScenariooResource('/branche/:branchName/build/:buildName/usecase/:usecaseName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName'
            }, {});
    })

    .factory('ScenarioResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName/scenario/:scenarioName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName'
            }, {});
    })

    .factory('ObjectsForTypeResource', function (ScenariooResource) {
        return ScenariooResource('/branches/:branchName/builds/:buildName/objects/service',
            {
                branchName: '@branchName',
                buildName: '@buildName'
            }, {});
    })

    .factory('VersionResource', function (ScenariooResource) {
        return ScenariooResource('/version', {}, {});
    })

    .factory('BranchAliasesResource', function (ScenariooResource) {
        return ScenariooResource('/branchaliases', {}, {});
    })

    .factory('CustomTabContentResource', function (ScenariooResource) {
        return ScenariooResource('/branches/:branchName/builds/:buildName/customTabObjects/:tabId',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                tabId: '@tabId'
            }, {});
    })

    .factory('ObjectListResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/object/:objectType',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                objectType: '@objectType'
            }, {});
    })

    .factory('ObjectIndexListResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/object/:objectType/:objectName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                objectType: '@objectType',
                objectName: '@objectName'
            }, {});
    })

    .factory('UseCasesResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName'
            }, {});
    })

    .factory('ScenarioResource', function (ScenariooResource) {
        return ScenariooResource('/branch/:branchName/build/:buildName/usecase/:usecaseName/scenario/:scenarioName',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                usecaseName: '@usecaseName',
                scenarioName: '@scenarioName'
            }, {});
    })

    .factory('ObjectsForTypeResource', function (ScenariooResource) {
        return ScenariooResource('/branches/:branchName/builds/:buildName/objects/service',
            {
                branchName: '@branchName',
                buildName: '@buildName'
            }, {});
    })

    .factory('VersionResource', function (ScenariooResource) {
        return ScenariooResource('/version', {}, {});
    })

    .factory('LabelConfigurationsListResource', function (ScenariooResource) {
        return ScenariooResource('/labelconfigurations/list', {}, {});
    })

    .factory('LabelConfigurationsResource', function (ScenariooResource) {
        return ScenariooResource('/labelconfigurations', {}, {'query': {isArray: false}});
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
