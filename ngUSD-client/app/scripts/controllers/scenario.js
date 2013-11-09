'use strict';

angular.module('ngUSDClientApp.controllers').controller('ScenarioCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioService, Config, HostnameAndPort) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;

    $scope.showingSteps = [];

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.$watch(function () {
            return Config.isLoaded();
        },
        function () {
            loadScenario();
        }
    );

    function loadScenario() {
        var selectedBranch = Config.selectedBranch();
        var selectedBuild = Config.selectedBuild();

        var pagesAndScenarios = ScenarioService.getScenario({'branchName': selectedBranch, 'buildName': selectedBuild,
            'usecaseName': useCaseName, 'scenarioName': scenarioName});
        pagesAndScenarios.then(function (result) {
            // Add page to the step to allow search for step- as well as page-properties
            populatePageAndSteps(result);
        });

        $scope.getScreenShotUrl = function (imgName) {
            return HostnameAndPort.forLink() + '/ngusd/rest/branches/' + selectedBranch + '/builds/' + selectedBuild +
                '/usecases/' + useCaseName + '/scenarios/' + scenarioName + '/image/' + imgName;
        };
    };


    function populatePageAndSteps(pagesAndScenarios) {
        for (var indexPage = 0; indexPage < pagesAndScenarios.pagesAndSteps.length; indexPage++) {
            var page = pagesAndScenarios.pagesAndSteps[indexPage];
            page.page.index = indexPage + 1;
            for (var indexStep = 0; indexStep < page.steps.length; indexStep++) {
                var step = page.steps[indexStep];
                step.page = page.page;
                step.index = indexStep;
                step.number = (indexStep === 0) ? page.page.index : page.page.index + '.' + indexStep;
                if (!step.title) {
                    step.title = 'undefined';
                }
            }
        }

        $scope.scenario = pagesAndScenarios.scenario;
        $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
    }

    $scope.goToStep = function (pageSteps, pageIndex, stepIndex) {
        var pageName = pageSteps.page.name;
        $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) +
            '/' + pageIndex + '/' + stepIndex);
    };

    $scope.resetSearchField = function () {
        $scope.searchFieldText = '';
    };

    $scope.openScreenshotModal = function () {
        $scope.showingScreenshotModal = true;
    };

    $scope.closeScreenshotModal = function () {
        $scope.showingScreenshotModal = false;
    };
});
