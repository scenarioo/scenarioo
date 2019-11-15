import {IFlatLabelConfiguration} from '../../generated-types/backend-types';

export class LabelConfiguration implements IFlatLabelConfiguration {

    backgroundColor: string;
    foregroundColor: string;
    name: string;

    private constructor(backgroundColor: string, foregroundColor: string, name: string) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.name = name;
    }

    isEmpty(): boolean {
        return this.name === '' && this.backgroundColor === '' && this.foregroundColor === '';
    }

    isValid() {
        return this.isEmpty() || (this.name !== '' && this.backgroundColor !== '');
    }

    static fromFlatLabelConfiguration(flatLabelConfiguration: IFlatLabelConfiguration): LabelConfiguration {
        return new LabelConfiguration(flatLabelConfiguration.backgroundColor,
            flatLabelConfiguration.foregroundColor,
            flatLabelConfiguration.name);
    }

    static empty() {
        return new LabelConfiguration('', '', '');
    }
}
