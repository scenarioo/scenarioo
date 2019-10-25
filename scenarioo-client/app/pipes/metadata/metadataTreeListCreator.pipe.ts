import {Injectable, Pipe, PipeTransform} from '@angular/core';
import {MetadataTreeCreatorPipe} from './metadataTreeCreator.pipe';

@Pipe({
    name: 'scMetadataTreeListCreator',
})

/**
 * A pipe to transform a list of several metadata tree structures (in an array)
 * into a view model tree for displaying metadata.
 */
@Injectable()
export class MetadataTreeListCreatorPipe implements PipeTransform {

    constructor(private metadataTreeCreatorPipe: MetadataTreeCreatorPipe) {
    }

    transform(metadata: any): any {

        const metadataTrees = {};

        Object.keys(metadata).forEach((key) => {
            const value = metadata[key];
            metadataTrees[key] = this.metadataTreeCreatorPipe.transform(value);
        });

        return metadataTrees;
    }
}
