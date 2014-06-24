'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Manage', function () {
    scenarioo.describeScenario('Manage build aliases', function () {
        var homePage = new pages.homePage();
        var branchAliasesPage = new pages.branchAliasesPage();

        browser.get('#/manage?tab=branchAliases');

        branchAliasesPage.assertNumberOfAliases(0);
        branchAliasesPage.enterAlias('Test Alias 1', 0, 'my description 1');
        branchAliasesPage.enterAlias('Test Alias 2', 0, 'my description 2');
        branchAliasesPage.save();
        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(2);

        branchAliasesPage.enterAlias('Test', '', 'my description');
        branchAliasesPage.assertSaveNotPossible();

        branchAliasesPage.reset();
        branchAliasesPage.deleteAlias(0);
        branchAliasesPage.save();
        branchAliasesPage.assertNumberOfAliases(1);
        branchAliasesPage.reset();
        branchAliasesPage.assertNumberOfAliases(1);

        branchAliasesPage.updateAlias(0, 'updated alias', 0, 'updated description');
        branchAliasesPage.save();
        browser.get('#/manage?tab=branchAliases');
        branchAliasesPage.assertAlias(0, 'updated alias', 0, 'updated description');

        branchAliasesPage.enterAlias('updated alias', 0, 'duplicate alias name');
        branchAliasesPage.save();
        branchAliasesPage.assertDuplicateAliasError();
    });
});