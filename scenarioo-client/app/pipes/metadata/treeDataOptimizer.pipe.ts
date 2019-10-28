import {Injectable, Pipe, PipeTransform} from '@angular/core';
import {IDetailsTreeNode} from '../../components/detailarea/IDetailsTreeNode';

@Pipe({
    name: 'scTreeDataOptimizer',
})

/**
 * Optimizes a metadata view model tree into a more nicely displayable view model of a metadata tree.
 * This not transforms but really modifies the passed data structure by optimizing it
 * to a more nicely displayable form by applying several optimizations
 * (like pulling up some fields into node labels etc.).
 */
@Injectable()
export class TreeDataOptimizerPipe implements PipeTransform {

    /**
     * Caution: this pipe seems to modify the passed data structure directly!
     * (to improve next time we touch this code!)
     * @param rootNode the view model data structure to optimize
     */
    transform(rootNode: any): IDetailsTreeNode {

        this.optimizeChildNodes(rootNode, this.pullUpChildrenOfDetailsNodes);

        this.optimizeNodes(rootNode, this.pullUpTypeToReplaceNodeLabel);
        this.optimizeNodes(rootNode, this.moveChildrenChildNodeBehindOthers);

        // this happens after making the labels human readable,
        // because the name node value could be a technical expression
        this.optimizeNodes(rootNode, this.pullUpNameToReplaceEmptyNodeLabel);
        this.optimizeNodes(rootNode, this.pullUpNameToReplaceEmptyNodeValue);
        this.optimizeNodes(rootNode, this.setFallBackLabelIfLabelIsEmpty);
        this.removeRootNodeLabelIfItIsItemAndHasNoValue(rootNode);

        return rootNode;
    }

    private optimizeChildNodes(node, operation) {
        if (node.childNodes === undefined) {
            return;
        }

        const modifiedChildNodes = [];

        node.childNodes.forEach((childNode) => {
            operation.call(this, childNode, modifiedChildNodes);
        });

        node.childNodes = modifiedChildNodes;

        node.childNodes.forEach((childNode) => {
            this.optimizeChildNodes(childNode, operation);
        });
    }

    private optimizeNodes(node, operation) {

        operation(node);

        if (node.childNodes === undefined) {
            return;
        }

        node.childNodes.forEach((childNode) => {
            this.optimizeNodes(childNode, operation);
        });
    }

    private pullUpChildrenOfDetailsNodes = (childNode, modifiedChildNodes) => {
        if (this.isDetailsNode(childNode)) {
            childNode.childNodes.forEach((grandChildNode) => {
                modifiedChildNodes.push(grandChildNode);
            });
        } else {
            modifiedChildNodes.push(childNode);
        }
    }

    private isDetailsNode = (node) => {
        return node.nodeLabel !== null && node.nodeLabel === 'details';
    }

    private pullUpTypeToReplaceNodeLabel = (node) => {
        const childNode = this.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'type');

        if (childNode === undefined) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
        node.nodeObjectType = childNode.nodeValue;
    }

    private moveChildrenChildNodeBehindOthers = (node) => {
        const childrenNode = this.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'children');

        if (childrenNode !== undefined) {
            this.addNodeToChildNodesAfterAllOthers(node, childrenNode);
        }
    }

    private pullUpNameToReplaceEmptyNodeLabel = (node) => {
        if ((typeof node.nodeLabel === 'string') && node.nodeLabel !== '') {
            return;
        }

        const childNode = this.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if (childNode === undefined) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
        node.nodeObjectName = childNode.nodeValue;
    }

    private pullUpNameToReplaceEmptyNodeValue = (node) => {
        if ((typeof node.nodeValue === 'string') && node.nodeValue !== '') {
            return;
        }

        const childNode = this.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if (childNode === undefined) {
            return;
        }

        node.nodeValue = childNode.nodeValue;
        node.nodeObjectName = childNode.nodeValue;
    }

    private setFallBackLabelIfLabelIsEmpty = (node) => {
        if ((typeof node.nodeLabel !== 'string') || node.nodeLabel.length === 0) {
            node.nodeLabel = 'Item';
        }
    }

    private getChildNodeWithSpecifiedNodeLabelAndRemoveIt = (node, type) => {
        if (!Array.isArray(node.childNodes)) {
            return undefined;
        }

        const modifiedChildNodes = [];
        let nameChildNode;

        for (const i in node.childNodes) {
            const childNode = node.childNodes[i];
            if (childNode.nodeLabel.toLowerCase() === type) {
                nameChildNode = childNode;
            } else {
                modifiedChildNodes.push(childNode);
            }
        }

        node.childNodes = modifiedChildNodes;

        return nameChildNode;
    }

    private addNodeToChildNodesAfterAllOthers = (node, childNodeToAdd) => {
        if (!Array.isArray(node.childNodes)) {
            node.childNodes = [];
        }
        node.childNodes.push(childNodeToAdd);
    }

    private removeRootNodeLabelIfItIsItemAndHasNoValue = (rootNode) => {
        const ITEM = 'Item';
        if (rootNode.nodeLabel !== undefined && rootNode.nodeLabel === ITEM && (rootNode.nodeValue === undefined || rootNode.nodeValue === '')) {
            delete rootNode.nodeLabel;
        }
    }
}
