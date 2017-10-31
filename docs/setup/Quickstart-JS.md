# Quickstart with Scenarioo-JS

The aim of this page is to describe the minimal viable setup of Scenarioo from scratch using `protractor` and the `scenarioo-js` writer library.


## Prerequisites

Install the [latest npm LTS version](https://nodejs.org/en/)

Install the [latest Google Chrome version](https://www.google.ch/chrome)


## Minimal Protractor Test

Create an empty folder for the project.

Open the folder in a console.

Create an initial `package.json` file:

```
npm init -y
```

Install [protractor](http://www.protractortest.org):

```
npm install -D protractor
```

Create folder `tests` with file `wikipediaTest.js`. Add content:

```
describe("Wikipedia", function() {

    it("Look up Protractor", function() {
        browser.get("http://www.wikipedia.org");
        scenarioo.step("Start page");

        element(by.id('searchInput')).sendKeys('Protractor');
        scenarioo.step("Entered search term Protractor");

        element(by.css('#search-form button')).click();
        expect(element(by.id('firstHeading')).getText()).toBe("Protractor");
        scenarioo.step("Article about Protractor");
    });

});
```

Add file `protractor.conf.js` in project folder, add content (`waitForAngularEnabled()` is required because the page we are going to test is not an Angular page):

```
exports.config = {
    specs: [ "tests/*.js"],
    onPrepare: function() {
        browser.waitForAngularEnabled(false);
    }
}
```

Add script to `package.json`:

```
  "scripts": {
    "pretest": "webdriver-manager update",
    "test": "protractor protractor.conf.js"
  },
```

Run the test:

```
npm test
```


# Minimal Scenarioo Integration

Add the scenarioo-js writer library:

```
npm install -D scenarioo-js
```


Extend `protractor.conf.js` as follows.

a) Add at the beginning of the file:

```
var scenarioo = require("./node_modules/scenarioo-js");
```

b) Add in `onPrepare` function:

```
scenarioo.setupJasmineReporter(jasmine, {
    targetDirectory: 'scenarioo-output',
    branchName: 'master',
    branchDescription: '',
    buildName: 'build_' + new Date().getTime(),
    revision: ''
});
```

Extend test as follows.

a) Add at the beginning of the file:

```
var scenarioo = require("../node_modules/scenarioo-js");
```

b) In the test after navigating to the Wikipedia page:

```
scenarioo.step("Start page");
```

Run the test again (as we have integrated Scenarioo now):

```
npm test
```


# Install Viewer to Browse Output

Download [Tomcat 9](https://tomcat.apache.org)

Unzip Tomcat

Add line to `context.xml` of Tomcat pointing to the subfolder `scenarioo-output` of your project:

```
<Parameter name="scenariooDataDirectory" value="D:\Code\scenarioo-demo\scenarioo-output" override="true" />
```

Download the [latest scenarioo development build](http://build.scenarioo.org/jenkins/job/scenarioo-ci-pipeline/job/develop/lastSuccessfulBuild/artifact/scenarioo-server/build/libs/). (This is currently required because the released version does not have the simple configuration option we use here yet).

Move Scenarioo `*.war` file into `webapps` folder of Tomcat and rename it to `scenarioo.war`.

Start up Tomcat using the startup script (`startup.bat` or `startup.sh` depending on your platform).

Access Scenarioo viewer under: [http://localhost:8080/scenarioo]()

If you can't access this page, make sure no other application is using port `8080`.

Click the use case "Wikipedia" and then the scenario "Look up Protractor". You should see this screen:

![Scenarioo showing steps of the scenario](Quickstart-JS.png)