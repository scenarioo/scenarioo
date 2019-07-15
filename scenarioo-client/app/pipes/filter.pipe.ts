import { Injectable, Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'filter',
})

@Injectable()
export class FilterPipe implements PipeTransform {
    transform(items: any[], value: string): any[] {
        if (!items) {
            return [];
        }
        if (!value) {
            return items;
        }

        const filteredModel = [];

        items.forEach((singleItem) => {
            if(typeof singleItem === 'object') {
                if (this.objectContainsAllSearchElements(singleItem, value)) {
                    filteredModel.push(singleItem);
                }
            }
        });

        return filteredModel;

    }

    objectContainsAllSearchElements(object, value: string): boolean {
        const searchElements = value.split(' ');

        for (const i in searchElements) {
            if (typeof searchElements[i] === 'string') {
                if (!this.objectContainsString(object, searchElements[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    objectContainsString(object, string: string): boolean {
        let returnTrue = false;

        Object.keys(object).forEach((property) => {
            if (!returnTrue) {
                if (typeof property === 'string') {
                    if (this.contains(property, string)) {
                        returnTrue = true;
                    }
                } else {
                    returnTrue = this.objectContainsString(property, string);
                }
            }
        });

        return returnTrue;
    }

    contains(haystack: string, needle: string): boolean {
        return haystack.toLowerCase().indexOf(needle.toLowerCase()) > -1;
    }
}
