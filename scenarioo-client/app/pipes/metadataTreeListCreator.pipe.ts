import {Injectable, Pipe, PipeTransform} from '@angular/core';
import {MetadataTreeCreatorPipe} from './metadataTreeCreator.pipe';

@Pipe({
    name: 'scMetadataTreeListCreator',
})

@Injectable()
export class MetadataTreeListCreatorPipe implements PipeTransform {

    constructor(private metadataTreeCreatorPipe: MetadataTreeCreatorPipe) {
    }

    transform(metadata: any): any {

        const metadataTrees = [];

        Object.keys(metadata).forEach((key) => {

            const value = metadata[key];

            const transformedValue = this.metadataTreeCreatorPipe.transform(value);

            const entry = {
                key,
                value: transformedValue,
            };
            metadataTrees.push(entry);
        });

        return metadataTrees;
    }
}
