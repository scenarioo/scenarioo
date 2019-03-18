import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'label-colors',
    template: require('./branchAliases.component.html'),
    styles: [require('./branchAliases.component.css').toString()],
})

export class BranchAliasesComponent implements OnInit {

    branches = [];
    branchAliases = [];
    uniqueError = false;
    successfullyUpdatedBranchAliases = false;

    ngOnInit(): void {
    }
}
