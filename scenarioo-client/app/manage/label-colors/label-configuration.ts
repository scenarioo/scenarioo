import {IFlatLabelConfiguration} from '../../generated-types/backend-types';

export class LabelConfiguration implements IFlatLabelConfiguration {
    private readonly VALID_HEX_COLOR_REGEX_PATTERN = /^#([0-9A-F]{3}){1,2}$/i;

    private constructor(public backgroundColor: string,
                        public foregroundColor: string,
                        public name: string) {
    }

    isEmpty(): boolean {
        return this.name === '' && this.backgroundColor === '' && this.foregroundColor === '';
    }

    isValid(): boolean {
        return this.isEmpty() || (this.name !== '' && this.backgroundColor !== '' && this.backgroundColorIsValid());
    }

    backgroundColorIsValid(): boolean {
        return this.VALID_HEX_COLOR_REGEX_PATTERN.test(this.backgroundColor);
    }

    static fromFlatLabelConfiguration(flatLabelConfiguration: IFlatLabelConfiguration): LabelConfiguration {
        return new LabelConfiguration(flatLabelConfiguration.backgroundColor,
            flatLabelConfiguration.foregroundColor,
            flatLabelConfiguration.name);
    }

    static empty(): LabelConfiguration {
        return new LabelConfiguration('', '', '');
    }
}
