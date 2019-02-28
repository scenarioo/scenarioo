angular.module('scenarioo.services')
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
