import {Injectable, Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from '@angular/common';

declare var angular: angular.IAngularStatic;

@Pipe({
    name: 'scDateTime',
})

@Injectable()
export class DateTimePipe extends DatePipe implements PipeTransform {

    transform(date) {

        if (typeof date === 'undefined') {
            return '';
        }
        if (typeof date === 'string' && date === '') {
            return '';
        }

        return super.transform(date, 'longDate') + ', ' + super.transform(date, 'shortTime');

    }
}
