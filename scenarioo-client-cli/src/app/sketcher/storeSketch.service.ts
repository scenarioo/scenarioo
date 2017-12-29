/* scenarioo-client
 * Copyright (C) 2015, scenarioo.org Development Team
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

angular.module('scenarioo.services').service('StoreSketchService', StoreSketchService);

function StoreSketchService(LocalStorageService, SketcherContextService, StepSketchResource,
                            IssueResource, ScenarioSketchResource, $log, DrawingPadService) {

    var savingSketch = false,
        issueIdAfterSavingIssue,
        scenarioSketchIdAfterSavingScenarioSketch,
        stepSketchIdAfterSavingStepSketch,
        modeAfterSaving,
        inputData,
        addAlertCallback,
        successCallback;

    var AUTHOR_LOCAL_STORAGE_KEY = 'issue_author',
        MODE_CREATE = 'create',
        MODE_EDIT = 'edit';

    function saveIssueAndScenarioSketchAndStepSketch(data, _addAlertCallback, _successCallback) {
        // Do not save it again if saving it's already in progress
        if(savingSketch) {
            return;
        }

        addAlertCallback = _addAlertCallback;
        successCallback = _successCallback;
        savingSketch = true;
        inputData = data;
        modeAfterSaving = inputData.mode;
        resetIds();

        var issue = new IssueResource({
            branchName: inputData.branchName,
            name: inputData.issueName,
            description: inputData.issueDescription,
            author: inputData.issueAuthor
        });

        if (inputData.mode === MODE_CREATE) {
            issue.relatedStep = SketcherContextService.stepIdentifier;
        }

        if (inputData.issueId && inputData.issueId !== undefined) {
            issue.issueId = inputData.issueId;
        }

        issue.$save(function (savedIssue) {
            issueIdAfterSavingIssue = savedIssue.issueId;
            saveScenarioSketch();
        },
        function (error) {
            $log.error(error);
            sketchSavedWithError('Issue could not be saved');
        });
    }

    function resetIds() {
        issueIdAfterSavingIssue = undefined;
        scenarioSketchIdAfterSavingScenarioSketch = undefined;
        stepSketchIdAfterSavingStepSketch = undefined;
    }

    function saveScenarioSketch() {
        if(!issueIdAfterSavingIssue) {
            sketchSavedWithError('Issue could not be saved.')
            return;
        }

        var scenarioSketch: any = {
            branchName: inputData.branchName,
            author: inputData.issueAuthor,
            issueId: issueIdAfterSavingIssue
        };

        if (inputData.scenarioSketchId && inputData.scenarioSketchId !== undefined) {
            scenarioSketch.scenarioSketchId = inputData.scenarioSketchId;
        }

        ScenarioSketchResource.save(scenarioSketch,
            function (savedScenarioSketch) {
                scenarioSketchIdAfterSavingScenarioSketch = savedScenarioSketch.scenarioSketchId;
                saveStepSketch();
            },
            function (error) {
                sketchSavedWithError('Scenario sketch could not be saved.', error);
            });
    }

    function saveStepSketch() {
        if(!scenarioSketchIdAfterSavingScenarioSketch) {
            sketchSavedWithError('Scenario sketch could not be saved.');
            return;
        }

        var exportedSVG = DrawingPadService.exportDrawing();

        var stepSketch = new StepSketchResource({
            branchName: inputData.branchName,
            svgXmlString: exportedSVG,
            issueId: issueIdAfterSavingIssue,
            scenarioSketchId: scenarioSketchIdAfterSavingScenarioSketch
        }, {});

        if (inputData.mode === MODE_CREATE) {
            stepSketch.relatedStep = SketcherContextService.stepIdentifier;
        }

        if (inputData.stepSketchId && inputData.stepSketchId !== undefined) {
            stepSketch.stepSketchId = inputData.stepSketchId;
        }

        stepSketch.$save(function (savedStepSketch) {
            stepSketchIdAfterSavingStepSketch = savedStepSketch.stepSketchId;
            sketchSuccessfullySaved();
        },
        function (error) {
            $log.error(error);
            sketchSavedWithError('StepSketch could not be saved');
        });
    }

    function sketchSuccessfullySaved() {
        addAlertCallback('success', 'saveSketchSuccessfulMessage', 'Sketch successfully saved');
        storeAuthorInLocalStorage();
        modeAfterSaving = MODE_EDIT;

        successCallback({
            issueId: issueIdAfterSavingIssue,
            scenarioSketchId: scenarioSketchIdAfterSavingScenarioSketch,
            stepSketchId: stepSketchIdAfterSavingStepSketch,
            mode: modeAfterSaving
        });

        savingSketch = false;
    }

    function storeAuthorInLocalStorage() {
        LocalStorageService.set(AUTHOR_LOCAL_STORAGE_KEY, inputData.issueAuthor);
    }

    function sketchSavedWithError(errorMessage, error?) {
        addAlertCallback('danger', 'saveSketchFailedMessage', 'The sketch has not been saved. Reason: ' + errorMessage);
        $log.error(error);
        savingSketch = false;
    }


    return {

        saveIssueAndScenarioSketchAndStepSketch: saveIssueAndScenarioSketchAndStepSketch,

        isSavingSketchInProgress: function() {
            return savingSketch;
        }

    };

}
