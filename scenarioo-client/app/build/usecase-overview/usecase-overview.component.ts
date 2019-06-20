import {Component, OnInit} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;


@Component({
    selector: 'sc-usecase-overview',
    template: require('./usecase-overview.component.html'),
    styles: [require('./usecase-overview.component.css').toString()],
})
export class UseCaseComponent implements OnInit {

    usecases: UseCaseSummary[] = [];
    /*
    table: {
        search: {searchTerm: ''},
        sort: {column: 'name', reverse: false},
    }
    */


    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private useCasesResource: UseCasesResource) {

    }

    ngOnInit(): void {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            this.branchesAndBuildsService.getBranchesAndBuilds().then(branchesAndBuilds => {
                console.log(branchesAndBuilds);
                this.useCasesResource.query({
                    branchName: selection.branch,
                    buildName: selection.build,
                }).subscribe((useCaseSummaries: UseCaseSummary[]) => {
                    this.usecases = useCaseSummaries;
                    console.log(useCaseSummaries);
                });
            });
        });
    }

    resetSearchField() {
        //this.table.search = {searchTerm: ''};
    }

    /*
    gotoUseCase(useCase) {
       //this.location..goTo('/usecase/' + useCase.name);
    }



     */

    getLabelStyle(labelName) {
        /*
        if (vm.labelConfigurations) {
            const labelConfig = vm.labelConfigurations[labelName];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
        */

    }



}
