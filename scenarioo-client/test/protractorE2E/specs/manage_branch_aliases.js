'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Manage branch aliases')
    .description('Define new branch aliases, edit existing ones and delete them.')
    .describe(function () {

        var branchAliasesPage = new pages.branchAliasesPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Add and remove')
            .description('Branch aliases can be added and removed')
            .it(function () {
                branchAliasesPage.goToPage();
                step('display the manage branch aliases page');

                branchAliasesPage.assertNumberOfAliases(0);
                branchAliasesPage.enterAlias('Test Alias 1', 'wikipedia-docu-example', 'my description 1');
                branchAliasesPage.enterAlias('Test Alias 2', 'wikipedia-docu-example', 'my description 2');
                branchAliasesPage.save();
                // TODO: we should better wait and check for the success message of the save here (which does not yet appear immediately)! In general our tests do not assert much
                step('saved build aliases');

                branchAliasesPage.reset();
                branchAliasesPage.assertNumberOfAliases(2);
                branchAliasesPage.openBranchSelectionMenu();
                step('open branch menu with aliases');
                branchAliasesPage.assertAliasesAreShownFirstInTheNavigationMenu();

                branchAliasesPage.deleteAlias(0);
                branchAliasesPage.save();
                branchAliasesPage.assertNumberOfAliases(1);
                branchAliasesPage.reset();
                branchAliasesPage.assertNumberOfAliases(1);
                step('removed first alias');

                branchAliasesPage.updateAlias(0, 'updated alias', 'wikipedia-docu-example', 'updated description');
                branchAliasesPage.save();
                step('updated first alias');

                branchAliasesPage.deleteAlias(0);
                branchAliasesPage.deleteAlias(0);
                branchAliasesPage.save();
                step('all aliases removed');

            });

        scenario('Validation')
            .description('Saving is not possible if referenced branch is not selected')
            .it(function () {
                branchAliasesPage.goToPage();
                branchAliasesPage.assertNumberOfAliases(0);
                branchAliasesPage.enterAlias('Test', '', 'my description');
                branchAliasesPage.assertSaveNotPossible();
                step('saving not possible because referenced branch is not selected');
            });

        scenario('Unique aliases')
            .description('Alias names have to be unique')
            .it(function () {
                branchAliasesPage.goToPage();
                branchAliasesPage.assertNumberOfAliases(0);
                branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
                branchAliasesPage.save();
                branchAliasesPage.assertNumberOfAliases(1);
                branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
                branchAliasesPage.save();
                branchAliasesPage.assertDuplicateAliasError();
                step('duplicate aliases are not allowed');

                branchAliasesPage.reset();
                branchAliasesPage.deleteAlias(0);
                branchAliasesPage.save();
            });

    });
