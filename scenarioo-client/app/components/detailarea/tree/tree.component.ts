/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import {Component, Input, SimpleChanges} from '@angular/core';
import {IDetailsTreeNode} from '../IDetailsTreeNode';
import {downgradeComponent} from '@angular/upgrade/static';
import {ScenariosOverviewComponent} from '../../../build/scenarios-overview/scenarios-overview.component';

declare var angular: angular.IAngularStatic;

const ITEM = 'Item';
const CHILDREN = 'children';

@Component({
    selector: 'sc-tree',
    template: require('./tree.component.html'),
    styles: [require('./tree.component.css').toString()],
})
export class TreeComponent {

    @Input()
    informationTree: IDetailsTreeNode;

    treeHtml: string;

    isLabel: boolean = false;

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.informationTree) {
            this.treeHtml = this.createTreeHtml(changes.informationTree.currentValue);
        }
    }

    private createTreeHtml(informationTree: IDetailsTreeNode): string {
        if (!(informationTree instanceof Object)) {
            return 'no data to display';
        } else if ((informationTree instanceof Object) && (informationTree.childNodes) && !informationTree.nodeValue) {
            let html = '';
            if (informationTree.nodeLabel === 'label') {
                this.isLabel = true;
                informationTree = informationTree.childNodes[0];
            }
            Object.keys(informationTree).forEach((rootNode) => {
                html += this.getRootNodeHtml(informationTree[rootNode]);
            });
            return html;
        }
        return this.getRootNodeHtml(informationTree);
    }

    private getRootNodeHtml(rootNode: IDetailsTreeNode): string {
        let html = '';
        if (rootNode.nodeLabel !== null) {
            html += '<ul>';
        }
        if ((rootNode instanceof Object) && (rootNode instanceof Array)) {
            Object.keys(rootNode).forEach((key) => {
                html += '<li>';
                html += this.getNodeHtml(rootNode[key]);
                html += '</li>';
            });
        } else {
            html += this.getNodeHtml(rootNode);
        }
        if (rootNode.nodeLabel !== null) {
            html += '</li></ul>';
        }
        return html;
    }

    private getNodeHtml(node: IDetailsTreeNode): string {
        if (this.isLabel === true) {
            return this.getLabelNodeHtml(node);
        }
        if ((node.nodeValue === undefined) || typeof node.nodeValue === 'string') {
            // Handle special structural nodes with internal scenarioo keywords to not display those as usual nodes with usual labels.
            if ((node.nodeLabel !== null) && node.nodeLabel === ITEM) {
                return this.getItemNodeHtml(node);
            } else if ((node.nodeLabel !== null) && node.nodeLabel === CHILDREN) {
                return this.getChildrenNodeHtml(node);
            }
        }
        if ((node.nodeObjectType === undefined) || (node.nodeObjectName === undefined)) {
            return this.getUsualValueItemNodeHtml(node);
        } else {
            return this.getObjectNodeHtml(node);
        }
    }

    /**
     * HTML for labels in the three structure.
     */
    private getLabelNodeHtml(node: IDetailsTreeNode): string {
        let html: string;
        html = '<span class="sc-text-before-label">' + node.nodeLabel + '</span>';
        Object.keys(node.childNodes).forEach((key) => {
            const label = node.childNodes[key].nodeLabel;
            html += '<span class="label label-info sc-label"> ' + label + '</span>';
        });
        return html;
    }

    /**
     * HTML for a simple value item in the three structure (most trivial node).
     */
    private getUsualValueItemNodeHtml(node: IDetailsTreeNode): string {
        let html = '';
        if (node.nodeLabel !== null) {
            html = this.getNodeTitleHtml(node);
        }
        if (node.childNodes !== null) {
            html += this.getChildNodesHtml(node.childNodes);
        }
        return html;
    }

    /**
     * HTML for a node representing an Object (with a special title from type and name)
     */
    private getObjectNodeHtml(node: IDetailsTreeNode): string {
        let html = '<div class="sc-treeNodeObject">';
        if (node.nodeLabel !== null) {
            html += '<div class="sc-treeNodeObjectTitle">';
            html += this.getNodeTitleHtml(node);
            html += '</div>';
        }
        if (node.childNodes !== null && node.childNodes.length !== 0) {
            html += this.getChildNodesHtml(node.childNodes);
        }
        html += '</div>';
        return html;
    }

    /**
     * HTML for an item in an ObjectTreeNode
     */
    private getItemNodeHtml(node: IDetailsTreeNode): string {
        let html = '<div class="sc-treeNodeItem">';
        if (node.childNodes !== null) {
            html += this.getChildNodesHtml(node.childNodes);
        }
        html += '</div>';
        return html;
    }

    /**
     * HTML for all children-Nodes in an ObjectTreeNode
     */
    private getChildrenNodeHtml(node: IDetailsTreeNode): string {
        let html = '<div class="sc-treeNodeChildren">';
        if (node.childNodes !== null) {
            html += this.getChildNodesHtml(node.childNodes);
        }
        html += '</div>';
        return html;
    }

    private getNodeTitleHtml(node: IDetailsTreeNode): string {
        return '<span class="sc-node-label">' + node.nodeLabel + '</span>' + this.getNodeValueHtml(node);
    }

    private getNodeValueHtml(node: IDetailsTreeNode): string {
        let href = '';

        if (node.nodeValue === undefined) {
            return '';
        }

        if ((node.nodeObjectType !== undefined) && (node.nodeObjectName !== undefined)) {
            const hrefObjectType = encodeURIComponent(node.nodeObjectType);
            const hrefObjectName = encodeURIComponent(node.nodeObjectName);

            href = '<a id="' + hrefObjectType + '_' + hrefObjectName + '" href="#/object/' +
                hrefObjectType + '/' + hrefObjectName + '">' + node.nodeValue + '</a>';
        } else {
            href = node.nodeValue + '';
        }
        return '<span class="sc-node-label">: </span><span class="sc-node-value">' + href + '</span>';
    }

    private getChildNodesHtml(childNodes: IDetailsTreeNode[]): string {
        if (childNodes === undefined || !(childNodes instanceof Array) || childNodes.length === 0) {
            return '';
        }
        let html = '<ul>';
        Object.keys(childNodes).forEach((value) => {
            html += '<li>' + this.getNodeHtml(childNodes[value]) + '</li>';
        });
        html += '</ul>';
        return html;
    }
}

angular.module('scenarioo.directives')
    .directive('scTree',
        downgradeComponent({component: TreeComponent}) as angular.IDirectiveFactory);
