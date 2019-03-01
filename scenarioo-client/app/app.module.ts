import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {LabelMetadataComponent} from './step/label-metadata/label-metadata.component';
import {FormsModule} from '@angular/forms';
import {LabelConfigurationService} from './services/label-configuration.service';
import {HttpClientModule} from '@angular/common/http';
import {VersionResource} from "./shared/services/versionResource.service";

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
        UpgradeModule
    ],
    providers: [
        LabelConfigurationService,
        VersionResource,
    ]
})
export class AppModule {
    constructor(private upgrade: UpgradeModule) {
    }

    ngDoBootstrap() {
        this.upgrade.bootstrap(document.documentElement, ['scenarioo']);
    }
}
