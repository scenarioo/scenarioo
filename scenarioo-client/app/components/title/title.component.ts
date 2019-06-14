import {Component, Input} from '@angular/core';

@Component({
    selector: 'sc-title',
    template: require('./title.component.html'),
    styles: [require('./title.component.css').toString()],
})

export class TitleComponent {

    @Input()
    useCaseName: string;

    @Input()
    scenarioName: string;

    @Input()
    stepIndex: number;

}
