angular.module('scenarioo.services')
    .factory('ComparisonLogResource', function (ScenariooResource) {
        return ScenariooResource('/builds/:branchName/:buildName/comparisons/:comparisonName/log',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                comparisonName: '@comparisonName'
            }, {
                get: {
                    method: 'GET',
                    headers: {'Accept': 'text/plain'},
                    transformResponse: function (data) {
                        return {content: data};
                    }
                }
            });
    });
