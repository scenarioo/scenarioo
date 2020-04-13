import {IFlatLabelConfiguration} from '../../generated-types/backend-types';
import {Color} from '../../shared/utils/Color';

export class LabelConfiguration implements IFlatLabelConfiguration {
    private constructor(public backgroundColor: string,
                        public foregroundColor: string,
                        public name: string) {
    }

    isEmpty(): boolean {
        return this.name === '' && this.backgroundColor === '' && this.foregroundColor === '';
    }

    isValid(): boolean {
        const validBackgroundColorSet = this.backgroundColor !== '' && Color.isHexColorValid(this.backgroundColor);
        return this.isEmpty() || (this.name !== '' && validBackgroundColorSet);
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
