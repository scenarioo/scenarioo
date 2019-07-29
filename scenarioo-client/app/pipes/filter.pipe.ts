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
            if (typeof singleItem === 'object') {
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

        Object.keys(object).forEach((key) => {
            if (!returnTrue) {
                const value = object[key];
                if (value === null) {
                    returnTrue = false;
                } else if (typeof value === 'string') {
                    if (this.contains(value, string)) {
                        returnTrue = true;
                    }
                } else {
                    returnTrue = this.objectContainsString(value, string);
                }
            }
        });

        return returnTrue;
    }

    contains(haystack: string, needle: string): boolean {
        return haystack.toLowerCase().indexOf(needle.toLowerCase()) > -1;
    }
}
