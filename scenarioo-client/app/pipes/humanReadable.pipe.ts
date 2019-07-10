import {Injectable, Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'humanReadable',
})

@Injectable()
export class HumanReadablePipe implements PipeTransform {

    transform(text: any): any {
        if (text && text.length > 0) {
            // First Char
            text = text.charAt(0).toUpperCase() + text.substr(1);
            // Underline
            text = text.replace(/([_])/g, ' ');

            // Camel Case
            // example 1: ThisIsSomeText
            let regex = /([a-z])([A-Z])/g;
            let replaceFn: any = (s, group0, group1) => group0 + ' ' + group1;
            // example 2: ABadExample
            text = text.replace(regex, replaceFn);
            regex = /([A-Z])([A-Z])([a-z])/g;
            replaceFn = (s, group0, group1, group2) => group0 + ' ' + group1 + group2;
            text = text.replace(regex, replaceFn);
        }

        return text;
    }
}
