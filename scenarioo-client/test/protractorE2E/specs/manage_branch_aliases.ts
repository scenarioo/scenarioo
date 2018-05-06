'use strict';
import {scenario, step, useCase} from "scenarioo-js";

var pages = require('./../webPages');

var NUMBER_OF_ALIASES_IN_CONFIG = 2;
var FIRST_TEST_ALIAS_INDEX = NUMBER_OF_ALIASES_IN_CONFIG;

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

                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG);
                branchAliasesPage.enterAlias('Test Alias 1', 'wikipedia-docu-example', 'my description 1');
                branchAliasesPage.enterAlias('Test Alias 2', 'wikipedia-docu-example', 'my description 2');
                branchAliasesPage.saveAndAssertSuccessMessage();
                step('saved build aliases');

                branchAliasesPage.reset();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG + 2);
                branchAliasesPage.openBranchSelectionMenu();
                step('open branch menu with aliases');
                branchAliasesPage.assertAliasesAreShownFirstInTheNavigationMenu();

                branchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                branchAliasesPage.saveAndAssertSuccessMessage();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG + 1);
                branchAliasesPage.reset();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG + 1);
                step('removed first test alias');

                branchAliasesPage.updateAlias(FIRST_TEST_ALIAS_INDEX, 'updated alias', 'wikipedia-docu-example', 'updated description');
                branchAliasesPage.saveAndAssertSuccessMessage();
                step('updated first test alias');

                branchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                branchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                branchAliasesPage.saveAndAssertSuccessMessage();
                step('all test aliases removed');
            });

        scenario('Validation')
            .description('Saving is not possible if referenced branch is not selected')
            .it(function () {
                branchAliasesPage.goToPage();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG);
                branchAliasesPage.enterAlias('Test', '', 'my description');
                branchAliasesPage.assertSaveNotPossible();
                step('saving not possible because referenced branch is not selected');
            });

        scenario('Unique aliases')
            .description('Alias names have to be unique')
            .it(function () {
                branchAliasesPage.goToPage();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG);
                branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
                branchAliasesPage.saveAndAssertSuccessMessage();
                branchAliasesPage.assertNumberOfAliases(NUMBER_OF_ALIASES_IN_CONFIG + 1);
                branchAliasesPage.enterAlias('duplicate', 'wikipedia-docu-example', 'duplicate alias name');
                branchAliasesPage.save();
                branchAliasesPage.assertDuplicateAliasError();
                step('duplicate aliases are not allowed');

                branchAliasesPage.reset();
                branchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                branchAliasesPage.saveAndAssertSuccessMessage();
            });

    });
