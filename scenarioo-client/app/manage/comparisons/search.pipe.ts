import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'customBranchFilter'
})
export class SearchPipe implements PipeTransform {

    transform(value: any, args?: any): any {
        if (!args) {
            return value;
        }
        return value.filter((comparison) => {
            return (
                comparison.baseBuild.branchName.toLocaleLowerCase().includes(args) ||
                comparison.name.toLocaleLowerCase().includes(args) ||
                comparison.status.toLocaleLowerCase().includes(args));
        })

    }

}
