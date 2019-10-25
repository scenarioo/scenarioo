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

    private transformNode(node, nodeTitle): any {
        let transformedNode: any = {
            nodeLabel: nodeTitle,
        };

        if (Array.isArray(node)) {
            transformedNode.childNodes = this.createArrayChildNodes(node);
        } else if (this.isObject(node)) {
            transformedNode.childNodes = this.createObjectChildNodes(node);
        } else if (this.isString(node)) {
            transformedNode = node;
        }

        return transformedNode;
    }

    private isObject(o: any): boolean {
        return (typeof o === 'object' || typeof o === 'function') && (o !== null);
    }

    private isString(s: any): boolean {
        return typeof s === 'string' || s instanceof String;
    }

    private createObjectChildNodes(node: any) {
        const childNodes = [];
        Object.keys(node).forEach((key: string) => {
            const value = node[key];
            if (Array.isArray(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: this.createArrayChildNodes(value),
                });
            } else if (this.isObject(value)) {
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

    private createArrayChildNodes(array: any[]) {
        const childNodes = [];
        array.forEach((element) => {
            if (this.isString(element)) {
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
