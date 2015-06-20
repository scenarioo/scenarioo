'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Manage branch aliases', function () {

    var branchAliasesPage = new pages.branchAliasesPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('Branch aliases can be added and removed', function () {
        branchAliasesPage.goToPage();
        scenarioo.docuWriter.saveStep('display the manage branch aliases page');

        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test Alias 1', 'wikipedia-docu-example', 'my description 1');
        branchAliasesPage.enterAlias('Test Alias 2', 'wikipedia-docu-example', 'my description 2');
        branchAliasesPage.save();
        // TODO: we should better wait and check for the success message of the save here (which does not yet appear immediately)! In general our tests do not assert much
        scenarioo.docuWriter.saveStep('saved build aliases');

        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(2);
        branchAliasesPage.openBranchSelectionMenu();
        scenarioo.docuWriter.saveStep('open branch menu with aliases');
        branchAliasesPage.assertAliasesAreShownFirstInTheNavigationMenu();

        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(1);
        scenarioo.docuWriter.saveStep('removed first alias');

        branchAliasesPage.updateAlias(0, 'updated alias', 'wikipedia-docu-example', 'updated description');
        branchAliasesPage.save();
        scenarioo.docuWriter.saveStep('updated first alias');

        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        scenarioo.docuWriter.saveStep('all aliases removed');

    });

    scenarioo.describeScenario('Saving is not possible if referenced branch is not selected', function() {
        branchAliasesPage.goToPage();
        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test', '', 'my description');
        branchAliasesPage.assertSaveNotPossible();
        scenarioo.docuWriter.saveStep('saving not possible because referenced branch is not selected');
    });

    scenarioo.describeScenario('Alias names have to be unique', function() {
        branchAliasesPage.goToPage();
        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertDuplicateAliasError();
        scenarioo.docuWriter.saveStep('duplicate aliases are not allowed');

        branchAliasesPage.reset();
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
    });

});
