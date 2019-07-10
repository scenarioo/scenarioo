import {Injectable, Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'scTreeDataCreator',
})

@Injectable()
export class TreeDataCreatorPipe implements PipeTransform {

    transform(data: any): any {

        if (!data) {
            return undefined ;
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

        if (angular.isArray(node)) {
            transformedNode.childNodes = this.createArrayChildNodes(node);
        } else if (angular.isObject(node)) {
            transformedNode.childNodes = this.createObjectChildNodes(node);
        } else if (angular.isString(node)) {
            transformedNode = node;
        }

        return transformedNode;
    }

    createObjectChildNodes(node) {
        const childNodes = [];
        angular.forEach(node, (value, key) => {
            if (angular.isArray(value)) {
                childNodes.push({
                    nodeLabel: key,
                    childNodes: this.createArrayChildNodes(value),
                });
            } else if (angular.isObject(value)) {
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

    createArrayChildNodes(array) {
        const childNodes = [];
        angular.forEach(array, (element) => {
            if (angular.isString(element)) {
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
