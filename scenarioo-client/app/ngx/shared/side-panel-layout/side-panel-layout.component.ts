import {Component} from '@angular/core';

@Component({
    selector: 'sc-side-panel-layout',
    template: require('./side-panel-layout.component.html'),
    styles: [require('./side-panel-layout.component.css').toString()]
})
export class SidePanelLayoutComponent {

    isSidePanelDisplayed: boolean = true;

    onToggleSidePanelDisplayed() {
        this.isSidePanelDisplayed = !this.isSidePanelDisplayed;
    }

    getButtonLabel() {
        if (this.isSidePanelDisplayed) {
            return 'Hide Details';
        } else {
            return 'Show Details';
        }
    }

}
