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

import {ShareComponent} from './share.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from '@angular/platform-browser-dynamic/testing';
import {BsModalService, ModalModule} from 'ngx-bootstrap';
import {SharePageURL} from '../../../shared/navigation/sharePage/sharePageUrl.service';
import {HashLocationStrategy, Location, LocationStrategy, PlatformLocation} from '@angular/common';
import {LocationService} from '../../../shared/location.service';
import {SharePageService} from '../../../shared/navigation/sharePage/sharePage.service';

describe('share component', () => {
    let component: ShareComponent;
    let fixture: ComponentFixture<ShareComponent>;
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
                SharePageURL,
                Location,
                {provide: LocationService, useFactory: (i: any) => i.get('$location'), deps: ['$injector']},
                {provide: SharePageService, useFactory: (i: any) => i.get('SharePageService'), deps: ['$injector']},
                {provide: LocationStrategy, useClass: HashLocationStrategy},
            ],
            imports: [ModalModule.forRoot()],
            declarations: [ShareComponent],
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ShareComponent);
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
        const sharePageURLService = fixture.debugElement.injector.get(SharePageURL);
        spyOn(sharePageURLService, 'getPageUrl').and.returnValue(URL);
        component.ngOnInit();
        void expect(component.pageUrl).toBe(URL);
    });

    it('getPageUrl behaves correctly when url is not set', () => {
        const sharePageURLService = fixture.debugElement.injector.get(SharePageURL);
        spyOn(sharePageURLService, 'getPageUrl').and.returnValue(undefined);
        component.ngOnInit();
        void expect(component.pageUrl).toBe('http://localhost:7070/context.html');
    });

    it('should set data', () => {
        const email = 'scenarioo@scenarioo.org';
        component.eMailUrl = email;
        void expect(component.eMailUrl).toBe(email);
    });

});
