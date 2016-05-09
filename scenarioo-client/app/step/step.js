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

angular.module('scenarioo.controllers').controller('StepCtrl', function ($scope, $routeParams, $location, $q, $window, Config, ScenarioResource, StepResource, HostnameAndPort,
                                                                         SelectedBranchAndBuild, $filter, ScApplicationInfoPopup, GlobalHotkeysService, LabelConfigurationsResource, SharePageService,
                                                                         ContextService, RelatedIssueResource, SketchIdsResource) {

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    var selectedBranchAndBuild = {};
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    $scope.pageName = $routeParams.pageName;
    $scope.pageOccurrence = parseInt($routeParams.pageOccurrence, 10);
    $scope.stepInPageOccurrence = parseInt($routeParams.stepInPageOccurrence, 10);
    var labels = $location.search().labels;

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.getPageNameUrlEncoded = function() {
        return encodeURIComponent($scope.pageName);
    };

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function (labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    // FIXME this code is duplicated. How can we extract it into a service?
    $scope.getLabelStyle = function (labelName) {
        if ($scope.labelConfigurations) {
            var labelConfig = $scope.labelConfigurations[labelName];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
    };

    $scope.showApplicationInfoPopup = function (tab) {
        ScApplicationInfoPopup.showApplicationInfoPopup(tab);
    };

    SelectedBranchAndBuild.callOnSelectionChange(loadStep);

    function loadStep(selected) {
        selectedBranchAndBuild = selected;
        bindStepNavigation();
        loadStepFromServer(selected);
    }

    function loadStepFromServer(selected) {
        StepResource.get(
            {
                'branchName': selected.branch,
                'buildName': selected.build,
                'usecaseName': useCaseName,
                'scenarioName': scenarioName,
                'pageName': $scope.pageName,
                'pageOccurrence': $scope.pageOccurrence,
                'stepInPageOccurrence': $scope.stepInPageOccurrence,
                'labels': labels
            },
            function success(result) {

                $scope.stepIdentifier = result.stepIdentifier;
                $scope.fallback = result.fallback;
                $scope.step = result.step;
                $scope.metadataTree = transformMetadataToTreeArray(result.step.metadata.details);
                $scope.stepInformationTree = createStepInformationTree(result.step);
                $scope.pageTree = transformMetadataToTree(result.step.page);
                $scope.stepNavigation = result.stepNavigation;
                $scope.stepStatistics = result.stepStatistics;
                $scope.stepIndex = result.stepNavigation.stepIndex;
                $scope.useCaseLabels = result.useCaseLabels;
                $scope.scenarioLabels = result.scenarioLabels;
                loadRelatedIssues();

                $scope.hasAnyLabels = function () {
                    var hasAnyUseCaseLabels = $scope.useCaseLabels.labels.length > 0;
                    var hasAnyScenarioLabels = $scope.scenarioLabels.labels.length > 0;
                    var hasAnyStepLabels = $scope.step.stepDescription.labels.labels.length > 0;
                    var hasAnyPageLabels = $scope.step.page.labels.labels.length > 0;

                    return hasAnyUseCaseLabels || hasAnyScenarioLabels || hasAnyStepLabels || hasAnyPageLabels;
                };

                SharePageService.setPageUrl($scope.getCurrentUrlForSharing());
                SharePageService.setImageUrl($scope.getScreenshotUrlForSharing());

                updateContextService();
            },
            function error(result) {
                $scope.stepNotFound = true;
                $scope.httpResponse = {
                    status: result.status,
                    method: result.config.method,
                    url: result.config.url,
                    data: result.data
                };
            }
        );
    }

    function updateContextService() {
        ContextService.stepIdentifier = {
            branchName: selectedBranchAndBuild.branch,
            buildName: selectedBranchAndBuild.build,
            usecaseName: useCaseName,
            scenarioName: scenarioName,
            pageName: $scope.pageName,
            pageOccurrence: $scope.pageOccurrence,
            stepInPageOccurrence: $scope.stepInPageOccurrence
        };

        ContextService.screenshotURL = $scope.getScreenShotUrl();
    }

    function createStepInformationTree(result) {
        var stepDescription = result.stepDescription;

        var stepInformation = {};

        if (angular.isDefined(stepDescription.title)) {
            stepInformation['Step title'] = stepDescription.title;
        }

        if (angular.isDefined(result.page)) {
            var pageToRender = angular.copy(result.page);
            // Will be displayed separately
            delete pageToRender.labels;
            stepInformation['Page name'] = pageToRender;
        }

        if (angular.isDefined(stepDescription.details)) {
            angular.forEach(stepDescription.details, function (value, key) {
                stepInformation[key] = value;
            });
        }

        if (angular.isDefined(stepDescription.status)) {
            stepInformation['Build status'] = stepDescription.status;
        }

        return transformMetadataToTree(stepInformation);
    }

    function bindStepNavigation() {

        GlobalHotkeysService.registerPageHotkeyCode(37, function () {
            // left arrow
            $scope.goToPreviousStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode(39, function () {
            // right arrow
            $scope.goToNextStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+37', function () {
            // control + left arrow
            $scope.goToPreviousPage();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+39', function () {
            // control + right arrow
            $scope.goToNextPage();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+36', function () {
            // control + HOME
            $scope.goToFirstStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+35', function () {
            // control + END
            $scope.goToLastStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+38', function () {
            // control + up arrow
            $scope.goToPreviousVariant();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+40', function () {
            // control + down arrow
            $scope.goToNextVariant();
        });

        $scope.isFirstStep = function () {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === 0;
        };

        $scope.isLastStep = function () {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === $scope.stepStatistics.totalNumberOfStepsInScenario - 1;
        };

        $scope.isFirstPage = function () {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === 0;
        };

        $scope.isLastPage = function () {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === $scope.stepStatistics.totalNumberOfPagesInScenario - 1;
        };

        $scope.goToPreviousStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousStep) {
                return;
            }
            $scope.go($scope.stepNavigation.previousStep);
        };

        $scope.goToNextStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextStep) {
                return;
            }
            $scope.go($scope.stepNavigation.nextStep);
        };

        $scope.goToPreviousPage = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousPage) {
                return;
            }
            $scope.go($scope.stepNavigation.previousPage);
        };

        $scope.goToNextPage = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextPage) {
                return;
            }
            $scope.go($scope.stepNavigation.nextPage);
        };

        $scope.goToFirstStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.firstStep) {
                return;
            }
            $scope.go($scope.stepNavigation.firstStep);
        };

        $scope.goToLastStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.lastStep) {
                return;
            }
            $scope.go($scope.stepNavigation.lastStep);
        };

        $scope.isFirstPageVariantStep = function () {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.previousStepVariant === null;
        };

        $scope.goToPreviousVariant = function () {
            if($scope.isFirstPageVariantStep()) {
                return;
            }
            $scope.go($scope.stepNavigation.previousStepVariant);
        };

        $scope.isLastPageVariantStep = function () {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.nextStepVariant === null;
        };

        $scope.goToNextVariant = function () {
            if($scope.isLastPageVariantStep()) {
                return;
            }
            $scope.go($scope.stepNavigation.nextStepVariant);
        };

        $scope.getCurrentStepIndexForDisplay = function () {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.stepIndex + 1;
        };

        $scope.getCurrentPageIndexForDisplay = function () {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.pageIndex + 1;
        };

        $scope.getStepIndexInCurrentPageForDisplay = function () {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.stepInPageOccurrence + 1;
        };

        $scope.getNumberOfStepsInCurrentPageForDisplay = function () {
            if (angular.isUndefined($scope.stepStatistics)) {
                return '?';
            }
            return $scope.stepStatistics.totalNumberOfStepsInPageOccurrence;
        };
    }

    // This URL is only used internally, not for sharing
    $scope.getScreenShotUrl = function () {
        if (angular.isUndefined($scope.step)) {
            return undefined;
        }

        var imageName = $scope.step.stepDescription.screenshotFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();
        return HostnameAndPort.forLink() + 'rest/branch/' + selected.branch + '/build/' + selected.build + '/usecase/' + $scope.stepIdentifier.usecaseName + '/scenario/' + $scope.stepIdentifier.scenarioName + '/image/' + imageName;
    };

    $scope.go = function (step) {
        $location.path('/step/' + (step.useCaseName || useCaseName) + '/' + (step.scenarioName || scenarioName) + '/' + step.pageName + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    $scope.getCurrentUrlForSharing = function () {
        return $location.absUrl() + createLabelUrl('&', getAllLabels());
    };

    $scope.getCurrentUrl = function () {
        return $location.absUrl();
    };

    $scope.getScreenshotUrlForSharing = function () {
        if (SelectedBranchAndBuild.isDefined() !== true) {
            return undefined;
        }

        return HostnameAndPort.forLinkAbsolute() + 'rest/branch/' + SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY] +
            '/build/' + SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY] +
            '/usecase/' + encodeURIComponent(useCaseName) +
            '/scenario/' + encodeURIComponent(scenarioName) +
            '/pageName/' + encodeURIComponent($scope.pageName) +
            '/pageOccurrence/' + $scope.pageOccurrence +
            '/stepInPageOccurrence/' + $scope.stepInPageOccurrence + '/image.' + getImageFileExtension() + createLabelUrl('?', getAllLabels());
    };

    var getImageFileExtension = function () {
        if (angular.isUndefined($scope.step)) {
            return '';
        }

        var imageFileName = $scope.step.stepDescription.screenshotFileName;

        if (!angular.isString(imageFileName)) {
            return '';
        }

        var fileNameParts = imageFileName.split('.');
        return fileNameParts[fileNameParts.length - 1];
    };

    var getAllLabels = function () {
        var allLabels = [];
        if ($scope.useCaseLabels && $scope.scenarioLabels && $scope.step) {
            allLabels = allLabels.concat($scope.useCaseLabels.labels).concat($scope.scenarioLabels.labels).concat($scope.step.stepDescription.labels.labels).concat($scope.step.page.labels.labels);
        }
        return allLabels;
    };

    var createLabelUrl = function (prefix, labelsForUrl) {
        if (angular.isUndefined(labelsForUrl) || !angular.isArray(labelsForUrl) || labelsForUrl.length === 0) {
            return '';
        }

        return prefix + 'labels=' + labelsForUrl.map(encodeURIComponent).join();
    };

    $scope.$on('$destroy', function () {
        SharePageService.invalidateUrls();
    });

    // Used in breadcrumbs.html
    $scope.showCreateOrEditSketchLink = true;

    // Called from breadcrumbs.html
    $scope.getSketchButtonTitle = function () {
        return 'Create Sketch';
    };

    // Called from breadcrumbs.html
    $scope.createOrEditSketch = function () {
        $location.path('/editor/').search('mode', 'create');
    };

    function loadRelatedIssues() {
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuild.selected().branch,
            buildName: SelectedBranchAndBuild.selected().build,
            useCaseName: useCaseName,
            scenarioName: scenarioName,
            pageName: $scope.pageName,
            pageOccurence: $scope.pageOccurrence,
            stepInPageOccurrence: $scope.stepInPageOccurrence
        }, function(result){
            $scope.relatedIssues = result;
            $scope.hasAnyRelatedIssues = function(){
                return $scope.relatedIssues.length > 0;
            };
            $scope.goToIssue = goToIssue;
        });
    }

    function goToIssue(issue) {
        var selectedBranch = SelectedBranchAndBuild.selected().branch;
        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

});
