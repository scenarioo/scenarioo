import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from "./shared/services/restControllerModule";
import {StatusStylingComponent} from "./manage/generalSettings/statusStyling/statusStyling.component";
import {GeneralSettingsComponent} from "./manage/generalSettings/generalSettings.component";
import {DisplayOptionsComponent} from "./manage/generalSettings/displayOptions/displayOptions.component";
import {AdditionalColumnsComponent} from "./manage/generalSettings/additionalColumns/additionalColumns.component";
import {BranchesBuildsComponent} from "./manage/generalSettings/branchesBuilds/branchesBuilds.component";
import {FulltextSearchComponent} from "./manage/generalSettings/fulltextSearch/fulltextSearch.component";
import {LabelColorsComponent} from "./manage/labelColors/labelColors.component";
import {ComparisonComponent} from "./manage/comparisons/comparison.component";
import {ComparisonStatusMapperService} from "./manage/comparisons/comparisonStatusMapper.service";
import {SearchPipe} from "./manage/comparisons/search.pipe";
import {DiffInfoIconDirective} from "./diffViewer/diffInfoIcon/diffInfoIcon.directive";
import {OrderByPipe} from "./manage/comparisons/order.pipe";
import {ModalViewComponent} from "./modalView/modalView.component";
import {ComparisonModalContent} from "./modalView/comparisonModalView/comparison.modalContent";

@NgModule({
    declarations: [
        LabelMetadataComponent,
        StatusStylingComponent,
        DisplayOptionsComponent,
        AdditionalColumnsComponent,
        BranchesBuildsComponent,
        GeneralSettingsComponent,
        FulltextSearchComponent,
        LabelColorsComponent,
        ComparisonComponent,
        SearchPipe,
        OrderByPipe,
        DiffInfoIconDirective,
        ModalViewComponent,
        ComparisonModalContent,
    ],
    entryComponents: [
        LabelMetadataComponent,
        StatusStylingComponent,
        DisplayOptionsComponent,
        AdditionalColumnsComponent,
        BranchesBuildsComponent,
        GeneralSettingsComponent,
        FulltextSearchComponent,
        LabelColorsComponent,
        ComparisonComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        UpgradeModule,
        RestControllerModule
    ],
    providers: [
        LabelConfigurationService,
        ComparisonStatusMapperService,
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
