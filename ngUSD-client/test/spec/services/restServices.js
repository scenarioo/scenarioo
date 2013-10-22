'use strict';

describe('Service :: restServices', function () {

    beforeEach(module('ngUSDClientApp.services'));

    it('should inject ConfigResource', inject(function (ConfigResource) {
        expect(ConfigResource).not.toBeUndefined();
        expect(ConfigResource.get).not.toBeUndefined();
    }));

});
