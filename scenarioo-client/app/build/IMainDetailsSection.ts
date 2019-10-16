import {LabelConfigurationMap} from '../shared/services/labelConfigurationsResource.service';

export interface IMainDetailsSection {
    name: string;
    key: string;
    dataTree: any;
    isFirstOpen: boolean;
    detailSectionType: string;
    config?: LabelConfigurationMap;
}
