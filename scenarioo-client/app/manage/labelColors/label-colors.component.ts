import {Component, OnInit} from '@angular/core';
import {LabelConfigurationsListResource} from '../../shared/services/labelConfigurationsListResource.service';
import {LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';

@Component({
    selector: 'sc-label-colors',
    template: require('./label-colors.component.html'),
    styles: [require('./label-colors.component.css').toString()],
})
export class LabelColorsComponent implements OnInit {
    availableColors = [{backgroundColor: '#e11d21', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#eb6420', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#fbca04', foregroundColor: '#000000'},
        {backgroundColor: '#009800', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#006b75', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#207de5', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#0052cc', foregroundColor: '#FFFFFF'},
        {backgroundColor: '#5319e7', foregroundColor: '#FFFFFF'}];

    labelConfigurations = [];
    colorMissing = [];
    successfullyUpdatedLabelConfigurations = false;

    constructor(private labelConfigurationsListResource: LabelConfigurationsListResource,
                private labelConfigurationsResource: LabelConfigurationsResource) {
    }

    ngOnInit(): void {
        this.loadLabelConfigurations();
    }

    onDelete(configurationToDelete) {
        this.labelConfigurations = this.labelConfigurations
            .filter((configuration) => configuration.name !== configurationToDelete.name);
    }

    onLabelNameChanged() {
        const labelName = this.labelConfigurations[this.labelConfigurations.length - 1].name;
        if (labelName !== '') {
            this.labelConfigurations.push(this.createEmptyLabelConfiguration());
        }
    }

    onReset() {
        this.loadLabelConfigurations();
    }

    onSave() {
        this.validateLabelConfigurations();

        if (this.allConfigurationsAreValid()) {
            this.labelConfigurationsResource.save(this.mapLabelConfigurations())
                .subscribe(() => {
                    this.successfullyUpdatedLabelConfigurations = true;
                });
        }
    }

    onColorSelected(labelConfiguration, color) {
        labelConfiguration.backgroundColor = color.backgroundColor;
        labelConfiguration.foregroundColor = color.foregroundColor;
    }

    loadLabelConfigurations() {
        this.labelConfigurationsListResource.query()
            .subscribe((labelConfigurations) => {
                labelConfigurations.push(this.createEmptyLabelConfiguration());
                this.labelConfigurations = labelConfigurations;
            });
    }

    private createEmptyLabelConfiguration() {
        return {name: '', backgroundColor: '', foregroundColor: ''};
    }

    private allConfigurationsAreValid() {
        return this.colorMissing.length === 0;
    }

    private validateLabelConfigurations() {
        let everythingIsValid = true;
        this.colorMissing = [];
        for (let _i = 0; _i < this.labelConfigurations.length; _i++) {
            if (this.labelConfigurations[_i].name !== '') {
                if (!this.labelConfigurations[_i].backgroundColor) {
                    everythingIsValid = false;
                    this.colorMissing[_i] = true;
                }
            }
        }
        return everythingIsValid;
    }

    private mapLabelConfigurations() {
        const labelConfigurationsAsMap = {};
        for (const labelConfiguration of this.labelConfigurations) {
            if (labelConfiguration.name !== '') {
                labelConfigurationsAsMap[labelConfiguration.name] = {
                    backgroundColor: labelConfiguration.backgroundColor,
                    foregroundColor: labelConfiguration.foregroundColor,
                };
            }
        }
        return labelConfigurationsAsMap;
    }
}
