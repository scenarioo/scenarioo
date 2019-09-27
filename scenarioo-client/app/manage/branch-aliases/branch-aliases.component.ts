import {Component, OnInit} from '@angular/core';
import {BranchAliasesResource} from '../../shared/services/branchAliasResource.service';
import {BranchesResource} from '../../shared/services/branchesResource.service';
import {IBranchAlias, IBranchBuilds} from '../../generated-types/backend-types';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';

@Component({
    selector: 'sc-branch-aliases',
    template: require('./branch-aliases.component.html'),
    styles: [require('./branch-aliases.component.css').toString()],
})
export class BranchAliasesComponent implements OnInit {

    branchAliases: IBranchAlias[] = [];
    branches$: Observable<IBranchBuilds[]>;
    successfullyUpdatedBranchAliases = false;
    uniqueError = false;
    requiredError = false;

    constructor(private branchAliasResource: BranchAliasesResource, private branchesResource: BranchesResource) {

    }

    ngOnInit(): void {
        this.refreshBranchAliases();
        this.loadBranchesWithoutAliases();
    }

    private loadBranchesWithoutAliases() {
        this.branches$ = this.branchesResource.query()
            .pipe(map((branches) => branches.filter((branch) => !branch.isAlias)));
    }

    private refreshBranchAliases() {
        this.branchAliasResource.get().subscribe((branchAliases) => {
            this.branchAliases = [...branchAliases, (this.createEmptyAlias())];
        });
    }

    private createEmptyAlias(): IBranchAlias {
        return {
            name: '',
            referencedBranch: '',
            description: '',
        };
    }

    onAliasNameChanged() {
        const aliasName = this.branchAliases[this.branchAliases.length - 1].name;
        if (aliasName !== '') {
            this.branchAliases.push(this.createEmptyAlias());
        }
    }

    onDelete(branchAliasToDelete: IBranchAlias) {
        this.branchAliases = this.branchAliases.filter((alias) => alias.name !== branchAliasToDelete.name);
    }

    onSave() {
        this.requiredError = this.areRequiredFieldsEmpty();
        this.uniqueError = !this.areBuildAliasesUnique();
        if (this.uniqueError || this.requiredError) {
            return;
        }

        this.branchAliasResource.save(this.branchAliases
            .filter((alias) => alias.name !== ''))
            .subscribe(() => {
                this.successfullyUpdatedBranchAliases = true;
            });

    }

    onReset() {
        this.refreshBranchAliases();
    }

    private areBuildAliasesUnique(): boolean {
        const branchAliasNames = this.branchAliases.map((alias) => alias.name);
        const uniqueAliases = branchAliasNames.filter((name, i) => branchAliasNames.indexOf(name) === i);
        return branchAliasNames.length === uniqueAliases.length;
    }

    private areRequiredFieldsEmpty(): boolean {
        return this.branchAliases
            .filter((alias) => alias.name !== '')
            .some((alias) => alias.referencedBranch === '' || alias.referencedBranch === undefined);
    }
}
