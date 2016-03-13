'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

describeUseCaseE('Manage branch aliases', {
    description: 'Define new branch aliases, edit existing ones and delete them.'
}, function () {

    var branchAliasesPage = new pages.branchAliasesPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    describeScenarioE('Add and remove', {
        description: 'Branch aliases can be added and removed'
    }, function () {
        branchAliasesPage.goToPage();
        scenarioo.saveStep('display the manage branch aliases page');

        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test Alias 1', 'wikipedia-docu-example', 'my description 1');
        branchAliasesPage.enterAlias('Test Alias 2', 'wikipedia-docu-example', 'my description 2');
        branchAliasesPage.save();
        // TODO: we should better wait and check for the success message of the save here (which does not yet appear immediately)! In general our tests do not assert much
        scenarioo.saveStep('saved build aliases');

        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(2);
        branchAliasesPage.openBranchSelectionMenu();
        scenarioo.saveStep('open branch menu with aliases');
        branchAliasesPage.assertAliasesAreShownFirstInTheNavigationMenu();

        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(1);
        scenarioo.saveStep('removed first alias');

        branchAliasesPage.updateAlias(0, 'updated alias', 'wikipedia-docu-example', 'updated description');
        branchAliasesPage.save();
        scenarioo.saveStep('updated first alias');

        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        scenarioo.saveStep('all aliases removed');

    });

    describeScenarioE('Validation', {
        description: 'Saving is not possible if referenced branch is not selected'
    }, function() {
        branchAliasesPage.goToPage();
        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test', '', 'my description');
        branchAliasesPage.assertSaveNotPossible();
        scenarioo.saveStep('saving not possible because referenced branch is not selected');
    });

    describeScenarioE('Unique aliases', {
        description: 'Alias names have to be unique'
    }, function() {
        branchAliasesPage.goToPage();
        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertDuplicateAliasError();
        scenarioo.saveStep('duplicate aliases are not allowed');

        branchAliasesPage.reset();
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
    });

});
