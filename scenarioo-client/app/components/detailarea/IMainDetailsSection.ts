import {LabelConfigurationMap} from '../../shared/services/labelConfigurationsResource.service';
import {IDetailsTreeNode} from './IDetailsTreeNode';

export interface IMainDetailsSection {
    name: string;
    key: string;
    isFirstOpen: boolean;
    detailSectionType: string;

    /**
     * data tree for tree section type
     */
    dataTree?: IDetailsTreeNode;

    /**
     * Other values to display for issues section type
     */
    values?: any[];

    /**
     * just for special label section to display the right colors (might be solveable by injection instead!)
     */
    labelConfigurations?: LabelConfigurationMap;
}
