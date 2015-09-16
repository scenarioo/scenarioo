'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Create a new Issue', function () {

    var homePage = new pages.homePage();
    var usecasePage = new pages.usecasePage();
    var scenarioPage = new pages.scenarioPage();
    var stepPage = new pages.stepPage();
    var editorPage = new pages.editorPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    scenarioo.ddescribeScenario('New issue success', 'Create a new issue successfully', function () {
        homePage.goToPage();

        scenarioo.docuWriter.saveStep('select a use case from the list');
        homePage.selectUseCase(1);

        scenarioo.docuWriter.saveStep('select a scenario from the list');
        usecasePage.selectScenario(1);

        scenarioo.docuWriter.saveStep('select a step from the scenario');
        scenarioPage.openStepByName('Step 1: Wikipedia Suche');

        scenarioo.docuWriter.saveStep('click "Create Sketch" button');
        stepPage.clickCreateSketchButton();

        // TODO #181 Try to draw something if this is easily possible
        //var drawingPad = element(by.id('drawingPad'));
        /*var drawingPadLocation;
        drawingPad.getLocation().then(function (location){
            drawingPadLocation = location;
        });*/

        //Try to draw
        /*
        element(by.id('Note Tool')).click();
        browser.actions()
            .mouseMove(drawingPad, {x: 100, y: 100}) // 100px from left, 100 px from top of drawingPad
            .mouseDown()
            .mouseMove({x: 200, y: 200})// 200px to the right of current location, 200px down
            .perform();
        scenarioo.docuWriter.saveStep('Draw on the sketch');
        */

        scenarioo.docuWriter.saveStep('Enter information about the step');
        editorPage.enterSketchInformation();
        editorPage.assertSaveSketchSuccessfulMessageIsNotPresent();

        scenarioo.docuWriter.saveStep('Save issue');
        editorPage.clickSaveButton();
        editorPage.assertSaveSketchSuccessfulMessageIsDisplayed();

        // TODO #181 Assert that there's one new sketch in the list of sketches
        /*
        element(by.css('table')).findElements(by.css('tbody tr')).then(function (elements) {
            expect(elements.length).toBe(this.numberOfIssues + 1);
        });
        */
    });

    scenarioo.ddescribeScenario('New issue fail', 'Fail to create an issue because insufficient information was entered', function () {
        homePage.goToPage();
        homePage.selectUseCase(1);
        usecasePage.selectScenario(1);
        scenarioPage.openStepByName('Step 1: Wikipedia Suche');

        scenarioo.docuWriter.saveStep('click "Create Sketch" button');
        stepPage.clickCreateSketchButton();
        editorPage.assertPageIsDisplayed();

        scenarioo.docuWriter.saveStep('Sketch can not be saved. Save button is inactive because not all required fields are filled in.');
        editorPage.assertSaveButtonIsDisabled();
    });

});
