'use strict';

describe('Service :: restServices', function () {


    beforeEach(angular.mock.module('ngUSDClientApp.services'));


    it('should inject ConfigResource', inject(function (ConfigResource) {
        expect(ConfigResource).not.toBeUndefined();
        expect(ConfigResource.get).not.toBeUndefined();
    }));

    describe('HostnameAndPort :: development', function () {

        beforeEach(function () {
            module(function ($provide) {
                $provide.constant('ENV', 'development');
            });
        });

        it('should resolve the host name to local host', inject(function (HostnameAndPort) {
            expect(HostnameAndPort.forNgResource()).toBe('http://localhost\\:8080');
            expect(HostnameAndPort.forLink()).toBe('http://localhost:8080');
        }));

    });

    describe('HostnameAndPort :: production', function () {

        beforeEach(function () {
            module(function ($provide) {
                $provide.constant('ENV', 'production');
            });
        });

        it('should resolve the host name to relative host', inject(function (HostnameAndPort) {
            expect(HostnameAndPort.forNgResource()).toBe('');
            expect(HostnameAndPort.forLink()).toBe('');
        }));

    });

});
