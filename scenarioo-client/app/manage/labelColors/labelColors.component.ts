import {Component, OnInit} from '@angular/core';
import {LabelConfigurationsListResource} from '../../shared/services/labelConfigurationsListResource.service';
import {LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';

@Component({
    selector: 'label-colors',
    template: require('./labelColors.component.html'),
    styles: [require('./labelColors.component.css').toString()],
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
    labelConfigurationsListResource: LabelConfigurationsListResource;
    labelConfigurationsResource: LabelConfigurationsResource;

    constructor(labelConfigurationsListResource: LabelConfigurationsListResource, labelConfigurationsResource: LabelConfigurationsResource) {
        this.labelConfigurationsListResource = labelConfigurationsListResource;
        this.labelConfigurationsResource = labelConfigurationsResource;
    }

    ngOnInit(): void {
        this.loadLabelConfigurations();
    }

    loadLabelConfigurations() {
        this.labelConfigurationsListResource.query()
            .subscribe((labelConfigurations) => {
                labelConfigurations.push(this.createEmptyLabelConfiguration());
                this.labelConfigurations = labelConfigurations;
            });
    }

    createEmptyLabelConfiguration() {
        return {name: '', backgroundColor: '', foregroundColor: ''};
    }

    deleteEntry(selectedLabelConfiguration) {
        if (selectedLabelConfiguration.name !== '' || selectedLabelConfiguration !== this.labelConfigurations[this.labelConfigurations.length - 1]) {

            for (let index = 0; index < this.labelConfigurations.length; index++) {
                const currentConfiguration = this.labelConfigurations[index];
                if (currentConfiguration.name === selectedLabelConfiguration.name) {
                    this.labelConfigurations.splice(index, 1);
                    break;
                }
            }
        }
    }

    addNewRow() {
        const labelName = this.labelConfigurations[this.labelConfigurations.length - 1].name;
        if (labelName !== '') {
            this.labelConfigurations.push(this.createEmptyLabelConfiguration());
        }
    }

    reset() {
        this.colorMissing = [];
        this.loadLabelConfigurations();
    }

    save() {
        this.colorMissing = [];
        const labelConfigurationsAsMap = {};
        let everythingIsValid = true;
        this.labelConfigurations.forEach((value, key) => {
            if (value.name !== '') {
                if (!value.backgroundColor) {
                    everythingIsValid = false;
                    this.colorMissing[key] = true;
                }
                labelConfigurationsAsMap[value.name] = {
                    backgroundColor: value.backgroundColor,
                    foregroundColor: value.foregroundColor,
                };
            }
        });

        if (everythingIsValid) {
            this.labelConfigurationsResource.save(labelConfigurationsAsMap)
                .subscribe(() => {
                    this.successfullyUpdatedLabelConfigurations = true;
                });
        }
    }

    selectColor(labelConfiguration, color) {
        labelConfiguration.backgroundColor = color.backgroundColor;
        labelConfiguration.foregroundColor = color.foregroundColor;
    }

}
