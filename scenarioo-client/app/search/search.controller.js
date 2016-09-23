/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('scenarioo.controllers').controller('SearchController', function ($routeParams, $location, FullTextSearchService,
                                                                                 SelectedBranchAndBuildService, ReferenceTreeNavigationService) {
    var vm = this;

    vm.results = {resultSet: []};
    vm.searchTerm = $routeParams.searchTerm;
    vm.treemodel = [];
    vm.showSearchFailed = false;
    vm.goToRelatedView = goToRelatedView;

    doSearch();

    function doSearch() {
        var selected = SelectedBranchAndBuildService.selected();

        FullTextSearchService.search({
                q: vm.searchTerm,
                buildName: selected.build,
                branchName: selected.branch
            }
        ).then(function onSuccess(result) {
                vm.results.resultSet = result;
            },
            function onError(error) {
                vm.showSearchFailed = true;
            });
    }

    // Entry point when a tree entry is clicked
    function goToRelatedView(nodeElement) {
        $location.path(ReferenceTreeNavigationService.goToRelatedView(nodeElement, vm.treemodel));
    }
});
