import {Component, OnInit} from '@angular/core';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {CustomTabContentResource} from '../../shared/services/customTabContentResource.service';

@Component({
    selector: 'sc-custom-tab',
    template: require('./custom-tab.component.html'),
})
export class CustomTabComponent implements OnInit {

    searchField = '';
    toggleLabel = '';
    tabId: string;
    treemodel: any;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildService: BranchesAndBuildsService,
                private customTabContentResource: CustomTabContentResource) {
    }

    ngOnInit(): void {
        this.selectedBranchAndBuildService.callOnSelectionChange((selected) => {
            this.branchesAndBuildService.getBranchesAndBuilds().then((branchesAndBuilds) => {

            });
        });
    }

    resetSearchField() {

    }

    expandAndCollapseTree() {

    }
}
