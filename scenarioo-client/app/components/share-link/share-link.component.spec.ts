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

import {ShareLinkComponent} from './share-link.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from '@angular/platform-browser-dynamic/testing';
import {BsModalService, ModalModule} from 'ngx-bootstrap';
import {HashLocationStrategy, Location, LocationStrategy} from '@angular/common';
import {LocationService} from '../../shared/location.service';
import {SharePageService} from './sharePage.service';

describe('share component', () => {
    let component: ShareLinkComponent;
    let fixture: ComponentFixture<ShareLinkComponent>;
    let element;

    const URL = 'http://www.scenarioo.org';

    beforeAll(() => {
        TestBed.resetTestEnvironment();
        TestBed.initTestEnvironment(BrowserDynamicTestingModule,
            platformBrowserDynamicTesting());
    });

    beforeEach(async(() => {
        void TestBed.configureTestingModule({
            providers: [
                SharePageService,
                Location,
                {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
                {provide: LocationStrategy, useClass: HashLocationStrategy},
            ],
            imports: [ModalModule.forRoot()],
            declarations: [ShareLinkComponent],
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ShareLinkComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
        element = fixture.debugElement.nativeElement;
    });

    it('should open the modal when the button is clicked', (done) => {
        const bsModalService = fixture.debugElement.injector.get(BsModalService);
        spyOn(bsModalService, 'show');

        const shareButton = fixture.nativeElement.querySelector('button');
        void expect(shareButton).toBeDefined();

        const modalHeader = fixture.nativeElement.querySelector('h1');
        void expect(modalHeader).toBeNull();

        shareButton.click();
        fixture.detectChanges();
        void fixture.whenStable().then(() => {
            void expect(bsModalService.show).toHaveBeenCalledTimes(1);
            done();
        });
    });

    it('getPageUrl behaves correctly when url is set', () => {
        const sharePageService = fixture.debugElement.injector.get(SharePageService);
        spyOn(sharePageService, 'getPageUrl').and.returnValue(URL);
        component.ngOnInit();
        const shareButton = fixture.nativeElement.querySelector('button');
        void expect(shareButton).toBeDefined();
        shareButton.click();
        void expect(component.pageUrl).toBe(URL);
    });

    it('getPageUrl behaves correctly when url is not set', () => {
        const sharePageService = fixture.debugElement.injector.get(SharePageService);
        spyOn(sharePageService, 'getPageUrl').and.returnValue(undefined);
        component.ngOnInit();
        const shareButton = fixture.nativeElement.querySelector('button');
        void expect(shareButton).toBeDefined();
        shareButton.click();
        void expect(component.pageUrl).toBe('http://localhost:7070/context.html');
    });

});
