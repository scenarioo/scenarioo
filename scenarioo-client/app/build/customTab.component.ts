// TODO: bindings don't work yet
angular.module('scenarioo.directives')
    .component('scCustomTab', {
        bindings: {
            tabTitle: '<',
            tabColumns: '<',
        },
        template: require('./customTab.html'),
        controller: CustomTabController,
    });

function CustomTabController(BranchesAndBuildsService, $location, CustomTabContentResource,
                             SelectedBranchAndBuildService, TreeNodeService) {

    const ctrl = this;
    ctrl.searchField = '';
    ctrl.treemodel = [];

    // Determines if the tree has expanded / collapsed rootnodes initially
    ctrl.rootIsCollapsed = true;
    ctrl.toggleLabel = 'expand';
    ctrl.tabContentTree = [];
    ctrl.branchesAndBuilds = [];
    ctrl.selectedBranchAndBuild = {};
    ctrl.selectedTab = undefined;

    ctrl.goToReferenceTree = goToReferenceTree;
    ctrl.expandAndCollapseTree = expandAndCollapseTree;
    ctrl.resetSearchField = resetSearchField;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange((selected) => {
            // Initialization on registration of this listener and on all changes to the build selection:
            ctrl.selectedBranchAndBuild = selected;
            ctrl.selectedTab = getSelectedTabFromUrl();
            // console.log(ctrl.selectedTab);
            loadContent();
        });
    }

    function goToReferenceTree(nodeElement) {
        $location.path('/object/' + nodeElement.type + '/' + nodeElement.name);
    }

    function expandAndCollapseTree(treemodel) {
        TreeNodeService.expandAndCollapseTree(treemodel, this);
    }

    function resetSearchField() {
        ctrl.searchField = '';
    }

    function getSelectedTabFromUrl() {
        const params = $location.search();
        let selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
        }

        return selectedTabId;
    }

    function loadContent() {

        BranchesAndBuildsService.getBranchesAndBuilds()
            .then((branchesAndBuilds) => {
                ctrl.branchesAndBuilds = branchesAndBuilds;

                CustomTabContentResource
                    .get({
                        branchName: ctrl.selectedBranchAndBuild.branch,
                        buildName: ctrl.selectedBranchAndBuild.build,
                    }, ctrl.selectedTab)
                    .subscribe((result) => {
                        ctrl.tabContentTree = result.tree;
                    });
            });

    }

}
