import {Injectable, Pipe, PipeTransform} from '@angular/core';
import {TreeDataOptimizerPipe} from './treeDataOptimizer.pipe';
import {TreeDataCreatorPipe} from './treeDataCreator.pipe';
import {IDetailsTreeNode} from '../../components/detailarea/IDetailsTreeNode'

@Pipe({
    name: 'scMetadataTreeCreator',
})

/**
 * A pipe to create a view model data tree to display a metadata input data object.
 */
@Injectable()
export class MetadataTreeCreatorPipe implements PipeTransform {

    constructor(private treeDataOptimizerPipe: TreeDataOptimizerPipe,
                private treeDataCreator: TreeDataCreatorPipe) {
    }

    transform(data: any): IDetailsTreeNode {
        return this.treeDataOptimizerPipe.transform(this.treeDataCreator.transform(data));
    }
}
