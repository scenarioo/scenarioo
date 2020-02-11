import {Component, OnInit} from '@angular/core';
import {LabelConfigurationsListResource} from '../../shared/services/labelConfigurationsListResource.service';
import {LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {LabelConfiguration} from './label-configuration';
import {getContrastingForegroundColorOfBackgroundColor, hexadecimalColorIsValid} from './color-helpers';

@Component({
    selector: 'sc-label-colors',
    template: require('./label-colors.component.html'),
    styles: [require('./label-colors.component.css').toString()],
})
export class LabelColorsComponent implements OnInit {
    availableBackgroundColors: string[] = [
        '#e11d21',
        '#eb6420',
        '#fbca04',
        '#009800',
        '#006b75',
        '#207de5',
        '#0052cc',
        '#5319e7',
    ];

    labelConfigurations: LabelConfiguration[] = [];
    successfullyUpdatedLabelConfigurations = false;

    constructor(private labelConfigurationsListResource: LabelConfigurationsListResource,
                private labelConfigurationsResource: LabelConfigurationsResource) {
    }

    ngOnInit(): void {
        this.loadLabelConfigurations();
    }

    onDelete(configurationToDelete: LabelConfiguration) {
        this.labelConfigurations = this.labelConfigurations
            .filter((configuration) => configuration.name !== configurationToDelete.name);
    }

    onLabelNameChanged() {
        const lastLabelInList = this.labelConfigurations[this.labelConfigurations.length - 1];
        if (!lastLabelInList.isEmpty()) {
            this.labelConfigurations = [...this.labelConfigurations, LabelConfiguration.empty()];
        }
    }

    onReset() {
        this.loadLabelConfigurations();
    }

    onSave() {
        if (this.allConfigurationsAreValid()) {
            this.labelConfigurationsResource.save(this.mapLabelConfigurations())
                .subscribe(() => {
                    this.successfullyUpdatedLabelConfigurations = true;
                });
        }
    }

    onBackgroundColorSelected(labelConfiguration: LabelConfiguration, color: string) {
        labelConfiguration.backgroundColor = color;
        labelConfiguration.foregroundColor = getContrastingForegroundColorOfBackgroundColor(labelConfiguration.backgroundColor);
    }

    onBackgroundColorChanged(labelConfiguration: LabelConfiguration) {
        labelConfiguration.foregroundColor = getContrastingForegroundColorOfBackgroundColor(labelConfiguration.backgroundColor);
    }

    private loadLabelConfigurations() {
        this.labelConfigurationsListResource.query()
            .subscribe((labelConfigurations) => {
                this.labelConfigurations = [
                    ...labelConfigurations.map(LabelConfiguration.fromFlatLabelConfiguration),
                    LabelConfiguration.empty()];
            });
    }

    private allConfigurationsAreValid(): boolean {
        return this.labelConfigurations.every((value) => value.isValid());
    }

    private mapLabelConfigurations(): Record<string, LabelConfiguration> {
        const labelConfigurationsAsMap = {};
        for (const labelConfiguration of this.labelConfigurations) {
            if (!labelConfiguration.isEmpty()) {
                labelConfigurationsAsMap[labelConfiguration.name] = labelConfiguration;
            }
        }
        return labelConfigurationsAsMap;
    }
}
