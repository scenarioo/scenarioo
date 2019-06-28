import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {BuildDiffInfoService} from './services/build-diff-info.service';
import {BuildDiffInfosService} from './services/build-diff-infos.service';
import {UseCaseDiffInfoService} from './services/use-case-diff-info.service';
import {UseCaseDiffInfosService} from './services/use-case-diff-infos.service';
import {ScenarioDiffInfoService} from './services/scenario-diff-info.service';
import {ScenarioDiffInfosService} from './services/scenario-diff-infos.service';
import {StepDiffInfosService} from './services/step-diff-infos.service';
import {StepDiffInfoService} from './services/step-diff-info.service';

@NgModule({
    declarations: [],
    entryComponents: [],
    imports: [
        HttpClientModule,
    ],
    providers: [
        BuildDiffInfoService,
        BuildDiffInfosService,
        UseCaseDiffInfoService,
        UseCaseDiffInfosService,
        ScenarioDiffInfoService,
        ScenarioDiffInfosService,
        StepDiffInfosService,
        StepDiffInfoService,
    ],
})
export class DiffViewerModule {

}
