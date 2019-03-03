import {NgModule} from "@angular/core";
import {LabelMetadataComponent} from "../../step/label-metadata/label-metadata.component";
import {BrowserModule} from "@angular/platform-browser";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {UpgradeModule} from "@angular/upgrade/static";
import {EmptyResponseBodyErrorInterceptor} from "./emptyResponseBodyErrorInterceptor";
import {LabelConfigurationService} from "../../services/label-configuration.service";
import {VersionResource} from "./versionResource.service";
import {SearchEngineStatusService} from "./searchEngineStatus.service";
import {ApplicationStatusService} from "./applicationStatus.service";
import {BranchAliasesResource} from "./branchAliasResource.service";
import {BranchesResource} from "./branchesResource.service";

@NgModule({
    declarations: [

    ],
    entryComponents: [
    ],
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
    ]
})
export class RestControllerModule {
    constructor(private upgrade: UpgradeModule) {
    }
}
