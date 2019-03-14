import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {RestControllerModule} from "./shared/services/restController.module";

@NgModule({
    declarations: [
        LabelMetadataComponent
    ],
    entryComponents: [
        LabelMetadataComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        UpgradeModule,
        RestControllerModule,
    ],
    providers: [
        LabelConfigurationService,
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
