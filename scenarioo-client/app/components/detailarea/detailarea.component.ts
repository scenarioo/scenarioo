import {Component} from '@angular/core';

@Component({
    selector: 'sc-detailarea',
    template: require('./detailarea.component.html'),
    styles: [require('./detailarea.component.css').toString()],
})

export class DetailareaComponent {

    isFirstOpen = true;
    collapsed: boolean;

}
