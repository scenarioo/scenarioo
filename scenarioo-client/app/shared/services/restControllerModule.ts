import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {EmptyResponseBodyErrorInterceptor} from './emptyResponseBodyErrorInterceptor';
import {VersionResource} from './versionResource.service';
import {SearchEngineStatusService} from './searchEngineStatus.service';
import {ApplicationStatusService} from './applicationStatus.service';
import {BranchAliasesResource} from './branchAliasResource.service';
import {BranchesResource} from './branchesResource.service';
import {BuildImportService} from './buildImport.service';
import {BuildImportLogResource} from './buildImportLogResource.service';
import {BuildImportStatesResource} from './buildImportStatesResource.service';
import {ConfigResource} from './configResource.service';
import {BuildReimportResource} from './buildReimportResource.service';
import {ComparisonCreateResource} from './comparisonCreateResource.service';

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
            multi: true,
        },
        VersionResource,
        SearchEngineStatusService,
        ApplicationStatusService,
        BranchAliasesResource,
        BranchesResource,
        BuildImportService,
        BuildImportLogResource,
        BuildImportStatesResource,
        ConfigResource,
        BuildReimportResource,
        ComparisonCreateResource,
    ],
})
export class RestControllerModule {}
