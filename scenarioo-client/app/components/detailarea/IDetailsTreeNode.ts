/**
 * View model that the details area can display.
 */
export interface IDetailsTreeNode {
    nodeLabel?: string;
    nodeValue?: string | number;
    childNodes: IDetailsTreeNode[];
}
