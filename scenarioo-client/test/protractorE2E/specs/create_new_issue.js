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

    scenarioo.describeScenario('New issue success', 'Create a new issue successfully', function () {
        homePage.goToPage();

        scenarioo.docuWriter.saveStep('Select a use case from the list');
        homePage.selectUseCase(1);

        scenarioo.docuWriter.saveStep('Select a scenario from the list');
        usecasePage.selectScenario(1);

        scenarioo.docuWriter.saveStep('Select a step from the scenario');
        scenarioPage.openStepByName('Step 1: Wikipedia Suche');

        scenarioo.docuWriter.saveStep('Click "Create Sketch" button');
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

    scenarioo.describeScenario('New issue fail', 'Fail to create an issue because insufficient information was entered', function () {
        stepPage.goToPage('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');

        scenarioo.docuWriter.saveStep('click "Create Sketch" button');
        stepPage.clickCreateSketchButton();
        editorPage.assertPageIsDisplayed();

        scenarioo.docuWriter.saveStep('Sketch can not be saved. Save button is inactive because not all required fields are filled in.');
        editorPage.assertSaveButtonIsDisabled();
    });

    scenarioo.describeScenario('Remember author', 'The author is remembered and automatically filled in for future issues', function() {
        stepPage.goToPage('/step/Donate/find_donate_page/donate.jsp/0/0');

        scenarioo.docuWriter.saveStep('Click "Create Sketch" button');
        stepPage.clickCreateSketchButton();

        scenarioo.docuWriter.saveStep('Enter information about the step');
        editorPage.assertAuthorFieldIsEmpty();
        editorPage.enterSketchInformation();

        scenarioo.docuWriter.saveStep('Save issue');
        editorPage.clickSaveButton();

        stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');

        scenarioo.docuWriter.saveStep('Go to a different step and create a sketch');
        stepPage.clickCreateSketchButton();

        scenarioo.docuWriter.saveStep('Author information is now already filled in');
        editorPage.assertAuthorFieldIsFilledAlready();
    });

});
