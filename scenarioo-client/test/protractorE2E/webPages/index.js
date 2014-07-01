/**
 *  note: this collects all page-object in one module.
 *  If anyone (nodejs-guru) knows a better way, please advice..
 *
 *
 *  see https://code.google.com/p/selenium/wiki/PageObjects  for more info on the pageObjects pattern
 */
var HomePage = require('./homePage.js');
var UsecasePage = require('./usecasePage.js');
var ScenarioPage = require('./scenarioPage.js');
var StepPage = require('./stepPage.js');
var BranchAliasesPage = require('./branchAliasesPage.js');


module.exports = {
    homePage: HomePage,
    usecasePage: UsecasePage,
    scenarioPage: ScenarioPage,
    stepPage: StepPage,
    branchAliasesPage: BranchAliasesPage
};
