import {Component, OnInit} from '@angular/core';
import {BranchesResource} from "../../shared/services/branchesResource.service";

@Component({
    selector: 'branch-aliases',
    template: require('./branchAliases.component.html'),
    styles: [require('./branchAliases.component.css').toString()],
})

export class BranchAliasesComponent implements OnInit {

    branches = [];
    branchAliases = [];
    uniqueError = false;
    successfullyUpdatedBranchAliases = false;

    branchResource: BranchesResource;

    constructor(branchResource: BranchesResource){
        this.branchResource = branchResource;
    }



    ngOnInit(): void {
    }

    loadBranchesWithoutAliases(): void {
        this.branchResource.query().subscribe((branches) => {
            const branchesWithoutAliases = [];
            for(let index = 0; index < branches.length; index++){
                const branch = branches[index];
                if(!branch.isAlias){
                    branchesWithoutAliases.push(branch);
                }
            }
            this.branches = branchesWithoutAliases;
        });
    }
}
