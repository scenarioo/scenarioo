import {LabelConfigurationService} from './label-configuration.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {getTestBed, TestBed} from '@angular/core/testing';

// TODO danielsuter does not work because of missing polyfills
// describe('LabelConfigurationService', () => {
//     let injector: TestBed;
//     let service: LabelConfigurationService;
//     let httpMock: HttpTestingController;
//
//     const serviceUrl = 'rest/labelconfigurations';
//
//     beforeEach(() => {
//         TestBed.configureTestingModule({
//             imports: [HttpClientTestingModule],
//             providers: [LabelConfigurationService]
//         });
//         injector = getTestBed();
//         service = injector.get(LabelConfigurationService);
//         httpMock = injector.get(HttpTestingController);
//     });
//
//     it('should assign enum values', () => {
//         const payload: any = {
//             'happy-path': {'backgroundColor': '#009800', 'foregroundColor': '#FFFFFF'},
//             'happy': {'backgroundColor': '#009800', 'foregroundColor': '#FFFFFF'}
//         };
//
//         service.get().subscribe((labelConfigurations) => {
//             expect(labelConfigurations['happy-path']).toBeDefined();
//             expect(labelConfigurations['happy']).toBeDefined();
//         });
//
//         const request = httpMock.expectOne(serviceUrl);
//         expect(request.request.method).toBe('GET');
//         request.flush(payload);
//     });
//
//
//     afterEach(() => {
//         httpMock.verify();
//     });
// });
