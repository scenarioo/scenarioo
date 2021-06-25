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

import {ConfigurationService} from '../../services/configuration.service';
import {BuildDiffInfosService} from '../../diffViewer/services/build-diff-infos.service';
import {SelectedComparisonService} from '../../diffViewer/selectedComparison.service';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.controllers')
    .component('scNavigationController', {
        template: require('./navigation.html'),
        controller: NavigationController,
    });

function NavigationController($location,
                              LocalStorageService,
                              BranchesAndBuildsService,
                              SelectedBranchAndBuildService,
                              SelectedComparisonService: SelectedComparisonService,
                              ApplicationInfoPopupService,
                              ConfigurationService: ConfigurationService,
                              GlobalHotkeysService,
                              BuildDiffInfosResource: BuildDiffInfosService,
                              SearchEngineStatusService,
                              $uibModal) {

    const ctrl = this;

    ConfigurationService.applicationName().subscribe((name) => {
        ctrl.applicationName = name;
    });

    ConfigurationService.getConfiguration().subscribe(loadBranchesAndBuilds);

    ctrl.COMPARISON_DISABLED = SelectedComparisonService.COMPARISON_DISABLED;
    ctrl.comparisonInfo = SelectedComparisonService.info;

    SelectedBranchAndBuildService.callOnSelectionChange(loadBranchesAndBuilds);

    ctrl.globalSearch = {
        queryString: '',
    };

    ctrl.search = () => {
        const searchTerm = ctrl.globalSearch.queryString;

        // If the search term is blank nothing happens
        if (!angular.isString(searchTerm) || searchTerm.trim() === '') {
            return;
        }

        // Since angular does not support encoded slashes in routes, we have to encode it twice.
        // See https://github.com/angular/angular.js/issues/10479
        const searchUrl = '/search/' + encodeURIComponent(encodeURIComponent(searchTerm));
        $location.url(searchUrl);
    };

    function loadSearchEngineRunning() {
        SearchEngineStatusService.isSearchEngineRunning().subscribe((result) => {
            ctrl.isSearchEngineRunning = result.running;
        });
    }

    ctrl.isSearchEngineRunning = loadSearchEngineRunning();

    function loadBranchesAndBuilds() {
        BranchesAndBuildsService.getBranchesAndBuilds().then((branchesAndBuilds) => {
            ctrl.branchesAndBuilds = branchesAndBuilds;
            loadComparisonBuilds();
        });
    }

    function loadComparisonBuilds() {
        if (ctrl.branchesAndBuilds && ctrl.branchesAndBuilds.selectedBranch && ctrl.branchesAndBuilds.selectedBuild) {
            const baseBranchName = ctrl.branchesAndBuilds.selectedBranch.branch.name;
            const baseBuildName = ctrl.branchesAndBuilds.selectedBuild.linkName;

            BuildDiffInfosResource.get(baseBranchName, baseBuildName)
                .subscribe((buildDiffInfos) => {
                    ctrl.comparisonBuilds = buildDiffInfos;
                    const preSelectedComparison = SelectedComparisonService.selected();
                    let selectedComparison = {name: SelectedComparisonService.COMPARISON_DISABLED, changeRate: 0};
                    angular.forEach(ctrl.comparisonBuilds, (comparisonBuild) => {
                        if (comparisonBuild.name === preSelectedComparison) {
                            selectedComparison = comparisonBuild;
                        }
                    });
                    SelectedComparisonService.setSelected(selectedComparison.name);
                    if (selectedComparison.name === SelectedComparisonService.COMPARISON_DISABLED) {
                        $location.search(SelectedComparisonService.COMPARISON_URL_PARAM_KEY, selectedComparison.name);
                    }
                    ctrl.selectedComparison = selectedComparison;
                }, () => {
                    resetComparisonSelection();
                });
        } else {
            resetComparisonSelection();
        }
    }

    ctrl.createComparison = () => {
        $uibModal.open({
            template: require('../../manage/comparisons/createComparisonModal.html'),
            controller: 'CreateComparisonModalController',
            controllerAs: 'vm',
            windowClass: 'modal-small',
        });
    };

    function resetComparisonSelection() {
        ctrl.comparisonBuilds = [];
        SelectedComparisonService.setSelected(SelectedComparisonService.COMPARISON_DISABLED);
        $location.search(SelectedComparisonService.COMPARISON_URL_PARAM_KEY, SelectedComparisonService.COMPARISON_DISABLED);
        ctrl.selectedComparison = undefined;
    }

    ctrl.setBranch = (branch) => {
        ctrl.branchesAndBuilds.selectedBranch = branch;
        LocalStorageService.remove(SelectedBranchAndBuildService.BUILD_KEY);
        LocalStorageService.remove(SelectedComparisonService.COMPARISON_URL_PARAM_KEY);
        $location.search(SelectedBranchAndBuildService.BRANCH_KEY, branch.branch.name);
    };

    ctrl.setBuild = (selectedBranch, build) => {
        ctrl.branchesAndBuilds.selectedBuild = build;
        LocalStorageService.remove(SelectedComparisonService.COMPARISON_URL_PARAM_KEY);
        $location.search(SelectedBranchAndBuildService.BUILD_KEY, build.linkName);
        loadComparisonBuilds();
    };

    ctrl.setComparisonBuild = (comparisonBuild) => {
        $location.search(SelectedComparisonService.COMPARISON_URL_PARAM_KEY, comparisonBuild.name);
        ctrl.selectedComparison = comparisonBuild;
    };

    ctrl.disableComparison = () => {
        const disabledComparison = {name: SelectedComparisonService.COMPARISON_DISABLED, changeRate: 0};
        ctrl.setComparisonBuild(disabledComparison);
    };

    ctrl.updating = false;

    GlobalHotkeysService.registerGlobalHotkey('i', () => {
        ctrl.showApplicationInfoPopup();
    });

    ctrl.showApplicationInfoPopup = () => {
        ApplicationInfoPopupService.showApplicationInfoPopup();
    };

}
