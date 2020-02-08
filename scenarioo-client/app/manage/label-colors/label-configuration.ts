import {IFlatLabelConfiguration} from '../../generated-types/backend-types';

export class LabelConfiguration implements IFlatLabelConfiguration {
    private constructor(public backgroundColor: string,
                        public foregroundColor: string,
                        public name: string) {
    }

    isEmpty(): boolean {
        return this.name === '' && this.backgroundColor === '' && this.foregroundColor === '';
    }

    isValid(): boolean {
        return this.isEmpty() || (this.name !== '' && this.backgroundColor !== '');
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
