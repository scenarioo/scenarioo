import {Injectable, Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'scTreeDataOptimizer',
})

@Injectable()
export class TreeDataOptimizerPipe implements PipeTransform {

    transform(rootNode: any): any {

        // TODO Check with Rolf whether we need to remove empty child nodes
        // optimizeTree(rootNode, removeEmptyChildNodes);
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

    optimizeChildNodes(node, operation) {
        if (node.childNodes === undefined) {
            return;
        }

        const modifiedChildNodes = [];

        node.childNodes.forEach((childNode) => {
            operation(childNode, modifiedChildNodes);
        });

        node.childNodes = modifiedChildNodes;

        node.childNodes.forEach((childNode) => {
            this.optimizeChildNodes(childNode, operation);
        });
    }

    pullUpChildrenOfDetailsNodes(childNode, modifiedChildNodes) {
        if (TreeDataOptimizerPipe.isDetailsNode(childNode)) {
            childNode.childNodes.forEach((grandChildNode) => {
                modifiedChildNodes.push(grandChildNode);
            });
        } else {
            modifiedChildNodes.push(childNode);
        }
    }

    static isDetailsNode(node) {
        return node.nodeLabel !== null && node.nodeLabel === 'details';
    }

    optimizeNodes(node, operation) {
        operation(node);

        if (node.childNodes === undefined) {
            return;
        }

        node.childNodes.forEach((childNode) => {
            this.optimizeNodes(childNode, operation);
        });
    }

    pullUpTypeToReplaceNodeLabel(node) {
        const childNode = TreeDataOptimizerPipe.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'type');

        if (childNode === undefined) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
        node.nodeObjectType = childNode.nodeValue;
    }

    moveChildrenChildNodeBehindOthers(node) {
        const childrenNode = TreeDataOptimizerPipe.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'children');

        if (childrenNode !== undefined) {
            this.addNodeToChildNodesAfterAllOthers(node, childrenNode);
        }
    }

    pullUpNameToReplaceEmptyNodeLabel(node) {
        if ((typeof node.nodeLabel === 'string') && node.nodeLabel !== '') {
            return;
        }

        const childNode = TreeDataOptimizerPipe.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if (childNode === undefined) {
            return;
        }

        node.nodeLabel = childNode.nodeValue;
        node.nodeObjectName = childNode.nodeValue;
    }

    pullUpNameToReplaceEmptyNodeValue(node) {
        if ((typeof node.nodeValue === 'string') && node.nodeValue !== '') {
            return;
        }

        const childNode = TreeDataOptimizerPipe.getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, 'name');

        if (childNode === undefined) {
            return;
        }

        node.nodeValue = childNode.nodeValue;
        node.nodeObjectName = childNode.nodeValue;
    }

    setFallBackLabelIfLabelIsEmpty(node) {
        if ((typeof node.nodeValue !== 'string') || node.nodeLabel.length === 0) {
            node.nodeLabel = 'Item';
        }
    }

    static getChildNodeWithSpecifiedNodeLabelAndRemoveIt(node, type) {
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

    addNodeToChildNodesAfterAllOthers(node, childNodeToAdd) {
        if (!Array.isArray(node.childNodes)) {
            node.childNodes = [];
        }
        node.childNodes.push(childNodeToAdd);
    }

    removeRootNodeLabelIfItIsItemAndHasNoValue(rootNode) {
        const ITEM = 'Item';
        if (rootNode.nodeLabel !== undefined && rootNode.nodeLabel === ITEM && (rootNode.nodeValue === undefined || rootNode.nodeValue === '')) {
            delete rootNode.nodeLabel;
        }
    }
}
