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

angular.module('scenarioo.services').service('ContextService', function () {

    var issueName, issueId, issueDescription, scenarioSketchName, scenarioSketchId, sketchStepIndex;
    var usecaseLink, scenarioLink, stepLink;
    var usecaseName, scenarioName, stepName;
    var sketchBtn;

    function initializeContext(){
        issueId = null;
        issueName = null;
        issueDescription = null;
        scenarioSketchName = null;
        scenarioSketchId = null;
        sketchStepIndex = null;

        usecaseName = null;
        scenarioName = null;
        stepName = null;

        usecaseLink = null;
        scenarioLink = null;
        stepLink = null;

        sketchBtn = null;
    }

    function setUseCase(originalUsecaseName){
        this.usecaseName = prettifyName(originalUsecaseName);
        this.usecaseLink = originalUsecaseName;
    }

    function setScenario(originalScenarioName){
        this.scenarioName = prettifyName(originalScenarioName);
        this.scenarioLink = originalScenarioName;
    }

    return {
        issueId: issueId,
        issueName: issueName,
        issueDescription: issueDescription,
        scenarioSketchId: scenarioSketchId,
        scenarioSketchName: scenarioSketchName,
        sketchStepIndex: sketchStepIndex,
        usecaseName: usecaseName,
        scenarioName: scenarioName,
        stepName: stepName,
        usecaseLink: usecaseLink,
        scenarioLink: scenarioLink,
        stepLink: stepLink,
        sketchButton: sketchBtn,

        initialize: initializeContext,
        setUseCase: setUseCase,
        setScenario: setScenario
    };

    function prettifyName(name){
        var wordsInName = name.split('_');
        wordsInName[0] = wordsInName[0].charAt(0).toUpperCase() + wordsInName[0].slice(1);
        return wordsInName.join(' ');
    }

});
