import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {UseCasesTabComponent} from './ngx/use-cases-tab/use-cases-tab.component';
import {SidePanelLayoutComponent} from './ngx/shared/side-panel-layout/side-panel-layout.component';
import {SideAreaComponent} from './ngx/shared/side-panel-layout/side-area.component';
import {MainAreaComponent} from './ngx/shared/side-panel-layout/main-area.component';
import {HeaderRowComponent} from './ngx/shared/side-panel-layout/header-row.component';

@NgModule({
    declarations: [
        LabelMetadataComponent,
        UseCasesTabComponent,
        SidePanelLayoutComponent,
        MainAreaComponent,
        SideAreaComponent,
        HeaderRowComponent

    ],
    entryComponents: [
        LabelMetadataComponent,
        UseCasesTabComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule
    ],
    providers: [
        LabelConfigurationService
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
