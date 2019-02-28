
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

angular.module('scenarioo.services')

    .factory('FullTextSearchService', function (ScenariooResource, $q) {
        var searchService = ScenariooResource('/branch/:branchName/build/:buildName/search?q=:q',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                q: '@q',
                includeHtml: '@includeHtml'
            }, {});

        searchService.search = getPromise($q, function (parameters, fnSuccess, fnError) {
            return searchService.get(parameters, fnSuccess, fnError);
        });

        return searchService;
    });
