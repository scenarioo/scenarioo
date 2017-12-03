import {Component, Input} from '@angular/core';

@Component({
    selector: 'sc-label-metadata',
    template: require('./label-metadata.component.html'),
    styles: [require('./label-metadata.component.css').toString()]
})
export class LabelMetadataComponent {

    @Input()
    useCaseLabels: string[];

    @Input()
    scenarioLabels: string[];

    @Input()
    pageLabels: string[];

    @Input()
    stepLabels: string[];

    @Input()
    labelConfigurations: any;

    getLabelStyle(label: string) {
        if (this.labelConfigurations) {
            const labelConfig = this.labelConfigurations[label];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }

        return {}
    }
}
