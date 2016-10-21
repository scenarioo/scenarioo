/**
 *  note: this collects all page-object in one module.
 *  If anyone (nodejs-guru) knows a better way, please advise..
 *
 *  see https://code.google.com/p/selenium/wiki/PageObjects  for more info on the pageObjects pattern
 */
var HomePage = require('./homePage.js');
var NavigationPage = require('./navigationPage.js');
var UsecasePage = require('./usecasePage.js');
var ScenarioPage = require('./scenarioPage.js');
var StepPage = require('./stepPage.js');
var EditorPage = require('./editorPage.js');
var GeneralSettingsPage = require('./generalSettingsPage.js');
var BranchAliasesPage = require('./branchAliasesPage.js');
var ObjectDetailsPage = require('./objectDetailsPage.js');
var LabelConfigurationsPage = require('./labelConfigurationsPage.js');
var BreadcrumbsPage = require('./breadcrumbsPage.js');
var SearchResultsPage = require('./searchResultsPage.js');

module.exports = {
    homePage: HomePage,
    navigationPage: NavigationPage,
    usecasePage: UsecasePage,
    scenarioPage: ScenarioPage,
    stepPage: StepPage,
    editorPage: EditorPage,
    generalSettingsPage : GeneralSettingsPage,
    branchAliasesPage: BranchAliasesPage,
    objectDetailsPage: ObjectDetailsPage,
    labelConfigurationsPage: LabelConfigurationsPage,
    breadcrumbsPage: BreadcrumbsPage,
    searchResultsPage: SearchResultsPage
};
