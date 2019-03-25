import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule, ReactiveFormsModule } from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import { HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from "./shared/services/restControllerModule";
import {LabelColorsComponent} from './manage/labelColors/labelColors.component';
import {BranchAliasesComponent} from "./manage/branchAliases/branchAliases.component";
import {BranchAliasService} from "./shared/services/branchAlias.service";

@NgModule({
    declarations: [
        LabelMetadataComponent,
        LabelColorsComponent,
        BranchAliasesComponent,
    ],
    entryComponents: [
        LabelMetadataComponent,
        LabelColorsComponent,
        BranchAliasesComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule,
        RestControllerModule,
        ReactiveFormsModule,
    ],
    providers: [
        LabelConfigurationService,
        BranchAliasService,
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
