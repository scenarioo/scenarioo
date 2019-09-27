import {Component, OnInit} from '@angular/core';
import {BranchAliasesResource} from '../../shared/services/branchAliasResource.service';
import {BranchesResource} from '../../shared/services/branchesResource.service';
import {IBranchAlias} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';

@Component({
    selector: 'sc-branch-aliases',
    template: require('./branch-aliases.component.html'),
    styles: [require('./branch-aliases.component.css').toString()],
})
export class BranchAliasesComponent implements OnInit {

    branchAliases$: Observable<IBranchAlias[]>;

    constructor(private branchAliasResource: BranchAliasesResource, private branchesResource: BranchesResource) {

    }

    ngOnInit(): void {
        this.branchAliases$ = this.branchAliasResource.get();
    }

}
