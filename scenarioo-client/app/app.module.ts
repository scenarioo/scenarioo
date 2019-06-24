import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from './shared/services/restController.module';
import {ConfigurationService} from './services/configuration.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ManageTabsComponent} from './manage/manage-tabs/manage-tabs.component';
import {TabsModule} from 'ngx-bootstrap';
import {BranchAliasesDirective} from './manage/branchAliases/branch-aliases.directive';
import {GeneralSettingsDirective} from './manage/generalSettings/general-settings.directive';
import {LabelColorsDirective} from './manage/labelColors/label-colors.directive';
import {BuildsListDirective} from './manage/buildImport/builds-list.directive';
import {ComparisonsDirective} from './manage/comparisons/comparisons.directive';
import {LocationService} from './shared/location.service';
import {BuildDiffInfoService} from './diffViewer/services/build-diff-info.service';
import {BuildDiffInfosService} from './diffViewer/services/build-diff-infos.service';
import {MainPageComponent} from './build/mainpage/mainpage.component';
import {UseCaseComponent} from './build/usecase-overview/usecase-overview.component';
import {SelectedBranchAndBuildService} from './shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from './shared/navigation/branchesAndBuilds.service';
import {SelectedComparison} from './diffViewer/selectedComparison.service';
import {OrderModule} from 'ngx-order-pipe';
import {FilterPipe} from './pipes/filter.pipe';

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
        UseCaseComponent,
        FilterPipe,
    ],
    entryComponents: [
        LabelMetadataComponent,
        ManageTabsComponent,
        MainPageComponent,
        UseCaseComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule,
        RestControllerModule,
        TabsModule.forRoot(),
        OrderModule,
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
        {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
        {provide: SelectedBranchAndBuildService, useFactory: (i: any) => i.get('SelectedBranchAndBuildService'), deps: ['$injector']},
        {provide: BranchesAndBuildsService, useFactory: (i: any) => i.get('BranchesAndBuildsService'), deps: ['$injector']},
        {provide: SelectedComparison, useFactory: (i: any) => i.get('SelectedComparison'), deps: ['$injector']},
    ],
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
