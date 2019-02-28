
angular.module('scenarioo.services')
    .factory('ComparisonCreateResource', function (ScenariooResource) {
        return ScenariooResource('/builds/:branchName/:buildName/comparisons/:comparisonName/calculate',
            {
                branchName: '@branchName',
                buildName: '@buildName',
                comparisonName: '@comparisonName'
            },
            {
                post: {
                    method: 'POST'
                }
            });
    });
