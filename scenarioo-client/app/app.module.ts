import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {VersionResource} from "./shared/services/versionResource.service";
import {SearchEngineStatusService} from "./shared/services/searchEngineStatus.service";
import {ApplicationStatusService} from "./shared/services/applicationStatus.service";
import {BranchAliasesResource} from "./shared/services/branchAliasResource.service";
import {EmptyResponseBodyErrorInterceptor} from "./shared/services/emptyResponseBodyErrorInterceptor";

@NgModule({
    declarations: [
        LabelMetadataComponent
    ],
    entryComponents: [
        LabelMetadataComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: EmptyResponseBodyErrorInterceptor,
            multi: true
        },
        LabelConfigurationService,
        VersionResource,
        SearchEngineStatusService,
        ApplicationStatusService,
        BranchAliasesResource,
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
