/**
 *  note: this collects all page-object in one module.
 *  If anyone (nodejs-guru) knows a better way, please advise..
 *
 *  see https://code.google.com/p/selenium/wiki/PageObjects  for more info on the pageObjects pattern
 */
var HomePage = require('./homePage');
var NavigationPage = require('./navigationPage');
var UsecasePage = require('./usecasePage');
var ScenarioPage = require('./scenarioPage');
var StepPage = require('./stepPage');
var EditorPage = require('./editorPage');
var GeneralSettingsPage = require('./generalSettingsPage');
var BranchAliasesPage = require('./branchAliasesPage');
var ObjectDetailsPage = require('./objectDetailsPage');
var LabelConfigurationsPage = require('./labelConfigurationsPage');
var BreadcrumbsPage = require('./breadcrumbsPage');
var SearchResultsPage = require('./searchResultsPage');

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
