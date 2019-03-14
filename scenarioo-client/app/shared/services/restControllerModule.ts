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
import {ComparisonLogResource} from './comparisonLogResource.service';
import {ComparisonRecalculateResource} from './comparisonRecalculateResource.service';
import {ComparisonsResource} from './comparisonsResource.service';
import {CustomTabContentResource} from './customTabContentResource.service';
import {FullTextSearchService} from './fullTextSearch.service';
import {LabelConfigurationsListResource} from './labelConfigurationsListResource.service';
import {LabelConfigurationsResource} from './labelConfigurationsResource.service';
import {ObjectIndexListResource} from './objectIndexListResource.service';
import {ScenarioResource} from './scenarioResource.service';

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
        ComparisonLogResource,
        ComparisonRecalculateResource,
        ComparisonsResource,
        CustomTabContentResource,
        FullTextSearchService,
        LabelConfigurationsListResource,
        LabelConfigurationsResource,
        ObjectIndexListResource,
        ScenarioResource,
    ],
})
export class RestControllerModule {
}
