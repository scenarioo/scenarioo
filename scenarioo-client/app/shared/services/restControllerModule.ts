import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {UpgradeModule} from "@angular/upgrade/static";
import {EmptyResponseBodyErrorInterceptor} from "./emptyResponseBodyErrorInterceptor";
import {VersionResource} from "./versionResource.service";
import {SearchEngineStatusService} from "./searchEngineStatus.service";
import {ApplicationStatusService} from "./applicationStatus.service";
import {BranchAliasesResource} from "./branchAliasResource.service";
import {BranchesResource} from "./branchesResource.service";
import {BuildImportService} from "./buildImport.service";

@NgModule({
    declarations: [],
    entryComponents: [],
    imports: [
        HttpClientModule,
        UpgradeModule,
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: EmptyResponseBodyErrorInterceptor,
            multi: true
        },
        VersionResource,
        SearchEngineStatusService,
        ApplicationStatusService,
        BranchAliasesResource,
        BranchesResource,
        BuildImportService,
    ]
})
export class RestControllerModule {
    constructor(private upgrade: UpgradeModule) {
    }
}
