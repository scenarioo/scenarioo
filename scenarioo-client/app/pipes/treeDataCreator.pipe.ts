import {Injectable, Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'scTreeDataCreator',
})

@Injectable()
export class TreeDataCreatorPipe implements PipeTransform {

    transform(data: any): any {

        if (!data) {
            return undefined;
        }

        if (typeof data === 'string') {
            return {nodeLabel: data};
        }

        return this.transformNode(data, '');

    }

    transformNode(node, nodeTitle) {
        let transformedNode: any = {
            nodeLabel: nodeTitle,
        };

        if (Array.isArray(node)) {
            transformedNode.childNodes = this.createArrayChildNodes(node);
        } else if (TreeDataCreatorPipe.isObject(node)) {
            transformedNode.childNodes = this.createObjectChildNodes(node);
        } else if (TreeDataCreatorPipe.isString(node)) {
            transformedNode = node;
        }

        return transformedNode;
    }

    private static isObject(o: any): boolean {
        return (typeof o === 'object' || typeof o === 'function') && (o !== null);
    }

    private static isString(s: any): boolean {
        return typeof s === 'string' || s instanceof String;
    }

    createObjectChildNodes(node: any) {
        const childNodes = [];
        Object.keys(node).forEach((key: string) => {
            let value = node[key];
            if (Array.isArray(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: this.createArrayChildNodes(value),
                });
            } else if (TreeDataCreatorPipe.isObject(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: this.createObjectChildNodes(value),
                });
            } else {
                childNodes.push({
                    nodeLabel: key,
                    nodeValue: value,
                });
            }
        });

        return childNodes;
    }

    createArrayChildNodes(array: any[]) {
        const childNodes = [];
        array.forEach((element) => {
            if (TreeDataCreatorPipe.isString(element)) {
                childNodes.push({
                    nodeLabel: element,
                });
            } else {
                childNodes.push({
                    nodeLabel: '',
                    childNodes: this.createObjectChildNodes(element),
                });
            }
        });

        return childNodes;
    }

}
