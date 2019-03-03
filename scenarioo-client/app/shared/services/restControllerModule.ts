import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";

import {EmptyResponseBodyErrorInterceptor} from "./emptyResponseBodyErrorInterceptor";
import {VersionResource} from "./versionResource.service";
import {SearchEngineStatusService} from "./searchEngineStatus.service";
import {ApplicationStatusService} from "./applicationStatus.service";
import {BranchAliasesResource} from "./branchAliasResource.service";
import {BranchesResource} from "./branchesResource.service";
import {BuildImportService} from "./buildImport.service";
import {BuildImportLogResource} from "./buildImportLogResource.service";

@NgModule({
    declarations: [],
    entryComponents: [],
    imports: [
        HttpClientModule,
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
        BuildImportLogResource,
    ]
})
export class RestControllerModule {
    constructor() {
    }
}
