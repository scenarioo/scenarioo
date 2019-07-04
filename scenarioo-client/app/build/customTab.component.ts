angular.module('scenarioo.directives')
    .component('scCustomTab',{
        template: require('./customTab.html'),
        controller: CustomTabController,
    });

function CustomTabController(BranchesAndBuildsService, $location, CustomTabContentResource,
                             SelectedBranchAndBuildService, TreeNodeService) {

    var vm = this;
    vm.searchField = '';
    vm.treemodel = [];

    // Determines if the tree has expanded / collapsed rootnodes initially
    vm.rootIsCollapsed = true;
    vm.toggleLabel = 'expand';
    vm.tabContentTree = [];
    vm.branchesAndBuilds = [];
    vm.selectedBranchAndBuild = {};
    vm.selectedTab = undefined;

    vm.goToReferenceTree = goToReferenceTree;
    vm.expandAndCollapseTree = expandAndCollapseTree;
    vm.resetSearchField = resetSearchField;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(selected => {
            // Initialization on registration of this listener and on all changes to the build selection:
            vm.selectedBranchAndBuild = selected;
            vm.selectedTab = getSelectedTabFromUrl();
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
        vm.searchField = '';
    }


    function getSelectedTabFromUrl() {
        var params = $location.search();
        var selectedTabId = 'undefined';
        if (params !== null && angular.isDefined(params.tab)) {
            selectedTabId = params.tab;
        }

        return selectedTabId;
    }

    function loadContent() {

        BranchesAndBuildsService.getBranchesAndBuilds()
            .then(branchesAndBuilds => {
                vm.branchesAndBuilds = branchesAndBuilds;

                CustomTabContentResource
                    .get({
                        branchName: vm.selectedBranchAndBuild.branch,
                        buildName: vm.selectedBranchAndBuild.build
                    }, vm.selectedTab)
                    .subscribe(result => {
                        vm.tabContentTree = result.tree;
                    });
            });

    }

}
