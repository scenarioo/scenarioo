/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import {Component, OnInit, TemplateRef} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {SharePageService} from '../../../shared/navigation/sharePage/sharePage.service';
import {Location, PlatformLocation} from '@angular/common';

@Component({
    selector: 'sc-share',
    template: require('./share.component.html'),
    styles: [require('./share.component.css').toString()],
})
export class ShareComponent implements OnInit {

    currentBrowserLocation: string;

    modalRef: BsModalRef;

    eMailSubject: string;
    eMailUrl: string;
    pageUrl: string;

    imageUrl: string;

    constructor(private modalService: BsModalService,
                private sharePageService: SharePageService,
                private location: Location,
                private platformLocation: PlatformLocation) {
    }

    ngOnInit(): void {

        // TODO: Workaround until the full migration, when the Angular router is available
        // this.currentBrowserLocation = this.location.prepareExternalUrl(this.location.path());
        this.currentBrowserLocation = (this.platformLocation as any).location.href;

        this.sharePageService.getPageUrl().subscribe((pageUrl) => {
            this.pageUrl = this.getPageUrl(pageUrl);
            this.eMailUrl = encodeURIComponent(this.pageUrl);
        });

        this.sharePageService.getImageUrl().subscribe((imageUrl) => {
            this.imageUrl = imageUrl;
        });

        this.eMailSubject = encodeURIComponent('Link to Scenarioo');

    }

    public getPageUrl(pageUrl: string): string {
        if (pageUrl === undefined) {
            return this.currentBrowserLocation;
        } else {
            return pageUrl;
        }
    }

    openShare(shareContent: TemplateRef<string>) {
        this.modalRef = this.modalService.show(shareContent);
    }

}
