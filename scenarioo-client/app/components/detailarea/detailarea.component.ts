import {Component} from '@angular/core';

@Component({
    selector: 'sc-detailarea',
    template: require('./detailarea.component.html'),
    styles: [require('./detailarea.component.css').toString()],
})

export class DetailareaComponent {

    isOpen: boolean = false;
    isFirstOpen: boolean = true;
    collapsed: boolean = false;

}
