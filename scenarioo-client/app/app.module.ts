import {APP_INITIALIZER, NgModule} from '@angular/core';
import {Location, LocationStrategy, HashLocationStrategy} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from './shared/services/restController.module';
import {ConfigurationService} from './services/configuration.service';
import {TitleComponent} from './components/title/title.component';
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
import {DiffViewerModule} from './diffViewer/diff-viewer.module';
import {OrderModule} from 'ngx-order-pipe';
import {FilterPipe} from './pipes/filter.pipe';
import {TooltipModule} from 'ngx-bootstrap';
import {SharePageURL} from './shared/navigation/sharePage/sharePageUrl.service';
import {ScenariosComponent} from './build/scenarios-overview/scenarios-overview.component';
import {SelectedBranchAndBuildService} from './shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from './shared/navigation/branchesAndBuilds.service';
import {SelectedComparison} from './diffViewer/selectedComparison.service';
import {HumanReadablePipe} from './pipes/humanReadable.pipe';
import {ScenarioComponent} from './build/scenario-overview/scenario-overview.component';

@NgModule({
    declarations: [
        LabelMetadataComponent,
        TitleComponent,
        ManageTabsComponent,
        BranchAliasesDirective,
        GeneralSettingsDirective,
        LabelColorsDirective,
        BuildsListDirective,
        ComparisonsDirective,
        MainPageComponent,
        UseCasesComponent,
        ScenariosComponent,
        ScenarioComponent,
        FilterPipe,
        HumanReadablePipe,
    ],
    entryComponents: [
        LabelMetadataComponent,
        TitleComponent,
        ManageTabsComponent,
        MainPageComponent,
        UseCasesComponent,
        ScenariosComponent,
        ScenarioComponent,
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
        {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
        SharePageURL,
        Location,
        {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
        {provide: SelectedBranchAndBuildService, useFactory: (i: any) => i.get('SelectedBranchAndBuildService'), deps: ['$injector']},
        {provide: BranchesAndBuildsService, useFactory: (i: any) => i.get('BranchesAndBuildsService'), deps: ['$injector']},
        {provide: SelectedComparison, useFactory: (i: any) => i.get('SelectedComparison'), deps: ['$injector']},
        {provide: LocationStrategy, useClass: HashLocationStrategy },
    ],
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
