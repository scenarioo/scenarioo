import {APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {HashLocationStrategy, Location, LocationStrategy} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from './shared/services/restController.module';
import {ConfigurationService} from './services/configuration.service';
import {TitleComponent} from './components/title/title.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainpageComponent} from './build/mainpage/mainpage.component';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {ModalModule} from 'ngx-bootstrap/modal';
import {UseCasesOverviewComponent} from './build/use-cases-overview/use-cases-overview.component';
import {ProgressbarModule} from 'ngx-bootstrap/progressbar';
import {ManageTabsComponent} from './manage/manage-tabs/manage-tabs.component';
import {LabelColorsComponent} from './manage/label-colors/label-colors.component';
import {BuildListComponent} from './manage/builds/build-list.component';
import {ComparisonsDirective} from './manage/comparisons/comparisons.directive';
import {AnnotatedScreenshotDirective} from './step/screenAnnotations/annotatedScreenshot.directive';
import {ScreenAnnotationsButtonDirective} from './step/screenAnnotations/screenAnnotationsButton.directive';
import {LocationService} from './shared/location.service';
import {BuildDiffInfoService} from './diffViewer/services/build-diff-info.service';
import {BuildDiffInfosService} from './diffViewer/services/build-diff-infos.service';
import {SelectedBranchAndBuildService} from './shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from './shared/navigation/branchesAndBuilds.service';
import {SelectedComparisonService} from './diffViewer/selectedComparison.service';
import {OrderModule} from 'ngx-order-pipe';
import {ScSearchFilterPipe} from './pipes/searchFilter.pipe';
import {HumanReadablePipe} from './pipes/humanReadable.pipe';
import {MetadataTreeCreatorPipe} from './pipes/metadata/metadataTreeCreator.pipe';
import {TreeDataCreatorPipe} from './pipes/metadata/treeDataCreator.pipe';
import {TreeDataOptimizerPipe} from './pipes/metadata/treeDataOptimizer.pipe';
import {DateTimePipe} from './pipes/dateTime.pipe';
import {AccordionModule, TooltipModule} from 'ngx-bootstrap';
import {FontAwesomeModule} from 'ngx-icons';
import {DetailareaComponent} from './components/detailarea/detailarea.component';
import {DetailAccordionComponent} from './components/detailarea/detail-accordion/detail-accordion.component';
import {CollapseModule} from 'ngx-bootstrap/collapse';
import {SharePageService} from './components/share-link/sharePage.service';
import {DiffViewerModule} from './diffViewer/diff-viewer.module';
import {CustomTabDirective} from './build/custom-tab.directive';
import {SketchesTabDirective} from './build/sketches-tab.directive';
import {DiffInfoService} from './diffViewer/diffInfo.service';
import {DiffInfoIconDirective} from './diffViewer/diffInfoIcon/diff-info-icon.directive';
import {ScenariosOverviewComponent} from './build/scenarios-overview/scenarios-overview.component';
import {RouteParamsService} from './shared/route-params.service';
import {MetadataTreeListCreatorPipe} from './pipes/metadata/metadataTreeListCreator.pipe';
import {ShareLinkComponent} from './components/share-link/share-link.component';
import {BranchAliasesComponent} from './manage/branch-aliases/branch-aliases.component';
import {UrlContextExtractorService} from './shared/utils/urlContextExtractor.service';
import {LocalStorageService} from './services/localStorage.service';
import {GeneralSettingsComponent} from './manage/general-settings/general-settings.component';
import {BuildDetailComponent} from './manage/builds/build-detail.component';
import {BuildImportStatusService} from './services/build-import-status.service';
import {Breadcrumbs} from './shared/navigation/breadcrumbs/breadcrumbs.component';
import {SketcherLinkService} from './shared/navigation/breadcrumbs/sketcherLink.service';
import {BreadcrumbsService} from './shared/navigation/breadcrumbs/breadcrumbs.service';
import {ProgressbarComponent} from './components/progressbar/progressbar.component';
import {StepViewComponent} from './build/step-view/step-view.component';
import {TreeComponent} from './components/detailarea/tree/tree.component';
import {RootScopeService} from './shared/rootScope.service';
import {ComparisonViewDirective} from './step/comparison/comparisonView.directive';

@NgModule({
    declarations: [
        ManageTabsComponent,
        LabelColorsComponent,
        BuildListComponent,
        BuildDetailComponent,
        ComparisonsDirective,
        AnnotatedScreenshotDirective,
        ScreenAnnotationsButtonDirective,
        MainpageComponent,
        ShareLinkComponent,
        UseCasesOverviewComponent,
        ScSearchFilterPipe,
        HumanReadablePipe,
        MetadataTreeCreatorPipe,
        TreeDataCreatorPipe,
        TreeDataOptimizerPipe,
        DateTimePipe,
        MetadataTreeListCreatorPipe,
        DetailareaComponent,
        DetailAccordionComponent,
        CustomTabDirective,
        SketchesTabDirective,
        DiffInfoIconDirective,
        ComparisonViewDirective,
        ScenariosOverviewComponent,
        TitleComponent,
        Breadcrumbs,
        BranchAliasesComponent,
        GeneralSettingsComponent,
        ProgressbarComponent,
        StepViewComponent,
        TreeComponent,
    ],
    entryComponents: [
        ManageTabsComponent,
        MainpageComponent,
        ShareLinkComponent,
        UseCasesOverviewComponent,
        DetailareaComponent,
        DetailAccordionComponent,
        TitleComponent,
        Breadcrumbs,
        ScenariosOverviewComponent,
        ProgressbarComponent,
        StepViewComponent,
        TreeComponent,
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
        ProgressbarModule.forRoot(),
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
        BreadcrumbsService,
        LabelConfigurationService,
        BuildImportStatusService,
        BuildDiffInfoService,
        BuildDiffInfosService,
        DiffInfoService,
        SharePageService,
        Location,
        ScSearchFilterPipe,
        MetadataTreeCreatorPipe,
        MetadataTreeListCreatorPipe,
        TreeDataCreatorPipe,
        TreeDataOptimizerPipe,
        DateTimePipe,
        LocalStorageService,
        UrlContextExtractorService,
        HumanReadablePipe,
        SelectedComparisonService,
        {provide: RootScopeService, useFactory: (i: any) => i.get('$rootScope'), deps: ['$injector']},
        {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
        {provide: RouteParamsService, useFactory: (i: any) => i.get('$routeParams'), deps: ['$injector']},
        {
            provide: SelectedBranchAndBuildService,
            useFactory: (i: any) => i.get('SelectedBranchAndBuildService'),
            deps: ['$injector'],
        },
        {
            provide: BranchesAndBuildsService,
            useFactory: (i: any) => i.get('BranchesAndBuildsService'),
            deps: ['$injector'],
        },
        {
            provide: SketcherLinkService,
            useFactory: (i: any) => i.get('SketcherLinkService'),
            deps: ['$injector'],
        },
        {provide: LocationStrategy, useClass: HashLocationStrategy},
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
