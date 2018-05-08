# E2E Testing with Protractor

[Protractor](https://github.com/angular/protractor) is the UI testing tool for testing AngularJS apps.
It uses [Selenium](http://www.seleniumhq.org/) to control the browser.

To write your own E2E tests, check our example E2E scenarios in `scenarioo-client\test\protractorE2E\specs`.

For more information on Protractor see [Protractor API Docs](http://angular.github.io/protractor/#/api).

## Run all E2E tests

### Prerequisites

* Test data is in place and is not corrupt
  (generate by running `gradlew clean build` in `scenarioo-docu-generation-example`).

* Scenarioo Viewer is up and running:
  * Scenarioo Viewer is built (run `gradlew clean build` in the project root directory).
  * Scenarioo Viewer backend is running (deploy `scenarioo-latest.war` to Tomcat under the `/scenarioo` context,
    IntelliJ can help with that).
  * Scenarioo Viewer frontend is running (run using `npm start` in project root directory). 

* Note: ChromeDriver will automatically be installed / updated when you run the E2E tests using the `npm run ...` 
commands.


### Run Tests

Change into the client directory:

   ```
   cd scenarioo-client
   ```

Run the tests and also generate Scenarioo documentation (uses config file `protractor-e2e-scenarioo.conf.js`):

   ```
   npm run test-e2e-scenarioo
   ```

Only run the tests, without generating Scenarioo documentation (uses config file `protractor-e2e.conf.js`):
   ```
   npm run test-e2e
   ```

This should open a new browser window, run all tests and log test-information to the console.


### Debug Protractor Tests

You can add either `browser.pause()` or `browser.debugger()` in your test code to set a breakpoint and start debugging
from there. For details please see: http://blog.ng-book.com/debugging-protractor-tests/

Please note that this approach does not work in nodejs 8.x anymore. See issue [676](https://github.com/scenarioo/scenarioo/issues/676).


### Manually update ChromeDriver

If there's a problem with ChromeDriver, try the following commands.
 
On Linux:
```
./node_modules/protractor/bin/webdriver-manager update --chrome
```

On Windows:
```  
node node_modules\protractor\node_modules\webdriver-manager update
```
