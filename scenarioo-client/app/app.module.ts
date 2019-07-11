import {APP_INITIALIZER, NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {Location, LocationStrategy, HashLocationStrategy} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from './shared/services/restController.module';
import {ConfigurationService} from './services/configuration.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainPageComponent} from './build/mainpage/mainpage.component';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {ModalModule} from 'ngx-bootstrap/modal';
import {UseCasesComponent} from './build/usecases-overview/usecases-overview.component';
import {ManageTabsComponent} from './manage/manage-tabs/manage-tabs.component';
import {BranchAliasesDirective} from './manage/branchAliases/branch-aliases.directive';
import {GeneralSettingsDirective} from './manage/generalSettings/general-settings.directive';
import {LabelColorsDirective} from './manage/labelColors/label-colors.directive';
import {BuildsListDirective} from './manage/buildImport/builds-list.directive';
import {ComparisonsDirective} from './manage/comparisons/comparisons.directive';
import {LocationService} from './shared/location.service';
import {BuildDiffInfoService} from './diffViewer/services/build-diff-info.service';
import {BuildDiffInfosService} from './diffViewer/services/build-diff-infos.service';
import {SelectedBranchAndBuildService} from './shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from './shared/navigation/branchesAndBuilds.service';
import {SharePageService} from './shared/navigation/sharePage/sharePage.service';
import {SelectedComparison} from './diffViewer/selectedComparison.service';
import {OrderModule} from 'ngx-order-pipe';
import {FilterPipe} from './pipes/filter.pipe';
import {HumanReadablePipe} from './pipes/humanReadable.pipe';
import {MetadataTreeCreatorPipe} from './pipes/metadataTreeCreator.pipe';
import {TreeDataCreatorPipe} from './pipes/treeDataCreator.pipe';
import {TreeDataOptimizerPipe} from './pipes/treeDataOptimizer.pipe';
import {TooltipModule} from 'ngx-bootstrap';
import {AccordionModule} from 'ngx-bootstrap';
import {FontAwesomeModule} from 'ngx-icons';
import {DetailareaComponent} from './components/detailarea/detailarea.component';
import {CollapseModule} from 'ngx-bootstrap/collapse';
import {SharePageURL} from './shared/navigation/sharePage/sharePageUrl.service';
import {DiffViewerModule} from './diffViewer/diff-viewer.module';
import {CustomTabDirective} from './build/custom-tab.directive';
import {SketchesTabDirective} from './build/sketches-tab.directive';
import {DiffInfoService} from './diffViewer/diffInfo.service';
import {DiffInfoIconDirective} from './diffViewer/diffInfoIcon/diff-info-icon.directive';

@NgModule({
    declarations: [
        LabelMetadataComponent,
        ManageTabsComponent,
        BranchAliasesDirective,
        GeneralSettingsDirective,
        LabelColorsDirective,
        BuildsListDirective,
        ComparisonsDirective,
        MainPageComponent,
        UseCasesComponent,
        FilterPipe,
        HumanReadablePipe,
        MetadataTreeCreatorPipe,
        TreeDataCreatorPipe,
        TreeDataOptimizerPipe,
        DetailareaComponent,
        CustomTabDirective,
        SketchesTabDirective,
        DiffInfoIconDirective,
    ],
    entryComponents: [
        LabelMetadataComponent,
        ManageTabsComponent,
        MainPageComponent,
        UseCasesComponent,
        DetailareaComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule,
        RestControllerModule,
        DiffViewerModule,
        TabsModule.forRoot(),
        ModalModule.forRoot(),
        OrderModule,
        TooltipModule.forRoot(),
        AccordionModule.forRoot(),
        CollapseModule.forRoot(),
        FontAwesomeModule,
    ],
    providers: [
        ConfigurationService,
        {
            provide: APP_INITIALIZER,
            useFactory: (cs: ConfigurationService) => () => {
                return cs.loadConfigurationFromBackend();
            },
            deps: [ConfigurationService],
            multi: true,
        },
        LabelConfigurationService,
        BuildDiffInfoService,
        BuildDiffInfosService,
        DiffInfoService,
        SharePageURL,
        Location,
        MetadataTreeCreatorPipe,
        TreeDataCreatorPipe,
        TreeDataOptimizerPipe,
        {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
        {provide: SelectedBranchAndBuildService, useFactory: (i: any) => i.get('SelectedBranchAndBuildService'), deps: ['$injector']},
        {provide: BranchesAndBuildsService, useFactory: (i: any) => i.get('BranchesAndBuildsService'), deps: ['$injector']},
        {provide: SelectedComparison, useFactory: (i: any) => i.get('SelectedComparison'), deps: ['$injector']},
        {provide: SharePageService, useFactory: (i: any) => i.get('SharePageService'), deps: ['$injector']},
        {provide: LocationStrategy, useClass: HashLocationStrategy },
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
