'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Manage', function () {
    scenarioo.describeScenario('Manage branch aliases', function () {
        var homePage = new pages.homePage();
        var branchAliasesPage = new pages.branchAliasesPage();

        browser.get('#/manage?tab=branchAliases');
        scenarioo.docuWriter.saveStep('display the manage branch aliases page');

        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test Alias 1', 0, 'my description 1');
        branchAliasesPage.enterAlias('Test Alias 2', 0, 'my description 2');
        branchAliasesPage.save();
        scenarioo.docuWriter.saveStep('add build aliases');
        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(2);

        branchAliasesPage.enterAlias('Test', '', 'my description');
        branchAliasesPage.assertSaveNotPossible();

        branchAliasesPage.reset();
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        console.log('before first');
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.reset();
        console.log('before second');
        branchAliasesPage.assertNumberOfAliases(1);

        branchAliasesPage.updateAlias(0, 'updated alias', 0, 'updated description');
        branchAliasesPage.save();
        scenarioo.docuWriter.saveStep('update aliases');
        browser.get('#/manage?tab=branchAliases');
        branchAliasesPage.assertAlias(0, 'updated alias', 0, 'updated description');

        branchAliasesPage.enterAlias('updated alias', 0, 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertDuplicateAliasError();
        scenarioo.docuWriter.saveStep('duplicate aliases are not allowed');
    });
});