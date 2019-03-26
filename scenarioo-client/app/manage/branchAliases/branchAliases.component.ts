import {Component, OnInit} from '@angular/core';
import {BranchesResource} from '../../shared/services/branchesResource.service';
import {BranchAliasesResource} from '../../shared/services/branchAliasResource.service';
import {BranchAliasService} from '../../shared/services/branchAlias.service';
import {FormControl, Validators} from '@angular/forms';

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
    branchAliasService;

    branchResource: BranchesResource;
    branchAliasesResource: BranchAliasesResource;

    aliasNameInput = new FormControl('', Validators.required);
    referencedBranchInputControls = [];

    constructor(branchResource: BranchesResource, branchAliasesResource: BranchAliasesResource, branchAliasService: BranchAliasService) {
        this.branchResource = branchResource;
        this.branchAliasesResource = branchAliasesResource;
        this.branchAliasService = branchAliasService;
    }

    checkValidity(): boolean {
        if (this.referencedBranchInputControls.length <= 0) {
            return true;
        }
        for (const control of this.referencedBranchInputControls.slice(0, this.referencedBranchInputControls.length - 1)) {
            if (control.invalid) {
                return true;
            }
        }
        return false;
    }

    setupFormControls(): void {
        this.referencedBranchInputControls = [];
        for (const branchAlias of this.branchAliases) {
            this.referencedBranchInputControls.push(new FormControl(branchAlias.referencedBranch, Validators.required));
        }
    }

    ngOnInit(): void {
        this.loadBranchAliases();
        this.loadBranchesWithoutAliases();
    }

    loadBranchesWithoutAliases(): void {
        this.branchResource.query().subscribe((branches) => {
            const branchesWithoutAliases = [];
            for (const branch of branches) {
                if (!branch.isAlias) {
                    branchesWithoutAliases.push(branch);
                }
            }
            this.branches = branchesWithoutAliases;
        });
    }

    deleteEntry(aliasName) {
        if (aliasName !== '') {
            for (let index = 0; index < this.branchAliases.length; index++) {
                const branchAlias = this.branchAliases[index];
                if (branchAlias.name === aliasName) {
                    this.branchAliases.splice(index, 1);
                    break;
                }
            }
        }
    }

    aliasNameChanged() {
        const aliasName = this.branchAliases[this.branchAliases.length - 1].name;
        if (aliasName !== '') {
            this.branchAliases.push(BranchAliasesComponent.createEmptyAlias());
            this.referencedBranchInputControls.push(new FormControl('', Validators.required));
        }
    }

    loadBranchAliases() {
        this.branchAliasesResource.get().subscribe((branchAliases) => {
            branchAliases.push(BranchAliasesComponent.createEmptyAlias());
            this.branchAliases = branchAliases;
            this.setupFormControls();
        });
    }

    reset() {
        this.loadBranchAliases();
    }

    save() {
        this.uniqueError = false;
        this.successfullyUpdatedBranchAliases = false;

        const branchAliasesToSave = [];
        for (const branchAlias of this.branchAliases) {
            if (branchAlias.name !== '') {
                branchAliasesToSave.push(branchAlias);
            }
        }

        if (this.areBuildAliasesUnique(branchAliasesToSave) === false) {
            this.uniqueError = true;
            return;
        }
        this.branchAliasesResource.save(branchAliasesToSave).subscribe(() => {
            this.branchAliasService.branchesLoadedSubject.next(true);
            });
        this.successfullyUpdatedBranchAliases = true;
    }

    areBuildAliasesUnique(buildAliases) {
        let unique = true;
        const aliasesMap = {};
        buildAliases.forEach((buildAlias) => {
            if (aliasesMap[buildAlias.name] === undefined) {
                aliasesMap[buildAlias.name] = '';
            } else {
                unique = false;
            }
        });
        return unique;
    }

    static createEmptyAlias() {
        return {
            name: '',
            referencedBranch: '',
            description: '',
        };
    }

}
