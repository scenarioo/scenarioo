# Developer Guide

This page describes how to build, test, run the Scenarioo Viewer Webapp locally for developers on their developer machine.

## Documentation for Developers

The published documentation under http://www.scenarioo.org/docs/develop should reflect the most recent changes of Scenarioo 
development. This is the state of our documentation on the develop branch.

Alternatively, you can find the newest sources of our documentation here:
https://github.com/scenarioo/scenarioo/tree/develop/docs 

If during setup you find some information that is outdated in the documentation please help to improve it and contribute the changes as a Pull Request. 
You can click the "Edit this page" link on top of these pages to navigate directly to the edit window of said page.

Thanks a lot!

## Prerequisites

You need a correctly setup development environment for working on Scenarioo as described here: 
[Development Environment Setup](Development-Environment-Setup.md)

## Build, Test and Run the Scenarioo Viewer Web App
 1. Build the viewer application by executing the "Scenarioo - Full Clean Build" run configuration
   * or on command-line run: `./gradlew clean build bootWar`
   * **On Windows:** If you get some Python errors during "npmInstall" task, you can probably ignore this optional npm dependency problems and just try to run it once again (or use something like `npm install -g npm-build-tools` to fix it)
    
 2. Execute the "Scenarioo - Fruehlingsstiefel" run configuration
   * or on command-line run: 
   ```
   # Set the path to the example data in your environment variables (system-dependent)
   export SCENARIOO_DATA=<your-project-source-path>/scenarioo-docu-generation-example/build/scenarioDocuExample 
   ./gradlew bootRun
   ```

   * This starts the viewer application backend as a standalone Spring Boot app
   * You should see in the log output that it is importing the example documentation data properly.
   
 3. Finally, start serving the frontend:
   ```
   cd scenarioo-client
   npm install
   npm start   
   ```
   Some remarks about this:
   * `npm install`: Installs node.js modules (mainly needed tools) as configured in `package.json`. They are placed in the folder `node_modules`.
   * This will spawn a webpack development server locally

 4. Now you can access the viewer application by browsing to http://localhost:8500/scenarioo/
   * :warning: The `/` at the end of the URL is mandatory!
   * If you change files in the client code, the browser will refresh automatically
   
 5. Before you start to develop, you should check that all [unit tests](#run-all-unit-tests) and [E2E tests](#e2e-testing-with-protractor) are successful, before you start to break them ;-) Make sure to run these tests regularly when you develop and to keep them successful.

## Run all unit tests

On the command-line run: 
   ```
   ./gradlew clean test
   ```
   
Or in IntelliJ 
    * run all java unit tests inside scenarioo/scenarioo-server
    * run all java-script karma unit tests inside scenarioo/scenarioo-client:
       `npm test`
   
## E2E Testing with Protractor

[Protractor](https://github.com/angular/protractor) is the UI testing tool for testing AngularJS apps.
It uses [Selenium](http://www.seleniumhq.org/) to control the browser.

To write your own E2E tests, check our example E2E scenarios in `scenarioo-client\test\e2e\specs`.

For more information on Protractor see [Protractor API Docs](http://angular.github.io/protractor/#/api).

### Prerequisites

Scenarioo Viewer is up and running and test data is generated. The simplest way to do this is follow the instructions in 
[Build, Test and Run the Scenarioo Viewer Web App](#build-test-and-run-the-scenarioo-viewer-web-app).

Note: ChromeDriver will automatically be installed / updated when you run the E2E tests using the `npm run ...` 
commands.

### Run E2E Tests

Change into the client directory:
   ```
   cd scenarioo-client
   ```

Run the tests and also generate Scenarioo documentation (uses config file `protractor-e2e-scenarioo.conf.js`):
   ```
   npm run e2e-scenarioo
   ```

Only run the tests, without generating Scenarioo documentation (uses config file `protractor-e2e.conf.js`):
   ```
   npm run e2e
   ```

This should open a new browser window, run all tests and log test-information to the console.

### Reset the Test Data

To run all tests a second time the test data needs to be reset.
To do this execute the "Scenarioo - Generate Testdata" run configuration

### Debug E2E Tests (client side)

Now that we are using async / await in the tests you can just set breakpoints in the IDE.

For this to work you need to install the [JetBrains IDE Support Chrome Plugin](https://chrome.google.com/webstore/detail/jetbrains-ide-support/hmhgeddbohgjknpmjagkdomcpobmllji?hl=en)

### Debug E2E Tests (server side)

To debug the server code during the execution of E2E-Tests, simply set a breakpoint in the IDE and execute the "Scenarioo- Fruehlingsstiefel" run configuration in Debug mode.

### Run a single E2E Test

If you want to run only one or a few E2E Tests, you need to replace '.it' with '.fit' in the test case.
Example:
```
scenario('First visit')
            .description('About dialog open on first access to Scenarioo to welcome new user.')
            .fit(async () => {
                //Test steps
            });
```
**Caution!** Do not commit this change!

### Run Fulltext search E2E Tests

If you run the E2E Tests without ElasticSearch, it will result in three failing tests.

To see those tests passing as well, you need to start ElasticSearch.

The easiest way to achieve this is to use the "Scenarioo - Hafenarbeiter Gummiband Suche" run configuration. Execute this configuration and wait for ElasticSearch to start. Then restart the "Scenarioo - Fruehlingsstiefel" run configuration.

To ensure that ElasticSearch works, navigate to [General Settings](http://localhost:8500/scenarioo/#/manage?tab=configuration) and verify that "ElasticSearch Status" is "Running and reachable".

If it is the first time you started this ElasticSearch image, the search index might not be present yet. To verify this search for 'donate.jsp'. If no results appear, [reimport all builds](localhost:8500/scenarioo/#/manage?tab=builds). Or generate the testdata before starting "Scenarioo - Fruehlingsstiefel".

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

## Working with scenarioo
### Hot code replacement in the backend
When changing server functionality it is possible to get the changes into the server without doing a full rebuild and restart.

To achieve this do the following:
1. Run the "Scenarioo - Fruehlingsstiefel" run configuration in debug mode.
2. Change code on the server
3. Press `Ctrl+F9`
4. Wait for a bit
5. A dialog appears: "Reload changed classes for Scenarioo - Fruehlingsstiefel"
6. Press OK
7. Reload the web page.

### Hot code replacement in the frontend
This is done automatically when the frontend is deployed with `npm start`. After a change is saved the web page is automatically reloaded.

### Docker Run Configurations

To ease development some Docker run configurations are part of the Scenarioo repository:

 * _Scenarioo - Hafenarbeiter_: This is the Docker image that we publish as part of the Scenarioo release. It builds and runs Scenarioo as standalone Spring Boot application.
 * _Scenarioo - Hafenarbeiter Kater_: This Docker image builds Scenarioo and runs it inside a Tomcat server.
 * _Scenarioo - Hafenarbeiter Gummiband Suche_: This Docker image starts ElasticSearch and exposes it on the default port, so that a running instance of Scenarioo can connect to it.
 * _Scenarioo - Hafenarbeiter komponiert Dev Cluster_: This run configuration uses Docker compose to start two Docker images, one with ElasticSearch and one with Tomcat.
 
#### Debugging Scenarioo running inside Docker

If you use the _Scenarioo - Hafenarbeiter komponiert Dev Cluster_ run configuration and want to debug Scenarioo, you have to do the following:
 1. In the file [docker-compose.yml](https://github.com/scenarioo/scenarioo/blob/develop/docker/dev-tomcat-elasticsearch-cluster/docker-compose.yml) set "JPDA_SUSPEND" to "Y". **Important:** Do not commit this change.
 2. Start the run configuration. Tomcat will wait with starting and deploying Scenarioo until a debugging session has been connected. You should see this in the log: "Listening for transport dt_socket at address: 1043".
 3. Select the _Scenarioo - Debug Docker_ run configuration and debug it.
 4. In the Docker log you should see Tomcat and Scenarioo booting up. Execution will stop at breakpoints and you can debug as usual.

## Working with scenarioo-java

Scenarioo-Server, Scenarioo-Validator and Scenarioo-Docu-Generation-Example all depend on the scenarioo-java writer library. If Scenarioo uses a snapshot version of this library (check `scenariooApiVersion` property at top of our build file `build.gradle` to see whether it uses a `SNAPSHOT`-version), then you need to build it locally for Scenarioo to work. If you want to make changes to scenarioo-java, then you can import it into IntelliJ.

### Building a SNAPSHOT version of scenarioo-java

1. You need to clone the sources (if not yet):
    ```
    git clone https://github.com/scenarioo/scenarioo-java.git scenarioo-java
    ```

2. Update and build the writer library and install the newest SNAPSHOT in the local maven repo:
     ```
     cd scenarioo-java
     git pull
     ./gradlew clean build test install
     ```
3. Add `mavenLocal()` to the `repositories` block in `build-java.gradle`.
4. Now you can continue working with Scenarioo. 
     
### Add scenarioo-java to IntelliJ

To fix issues in scenarioo-java you can import it into IntelliJ by using **"File/New/Module From Existing Sources"**.

### Running all tests in scenarioo-java

    ```
    cd scenarioo-java
    ./gradlew clean test
    ```

### Switching to a SNAPSHOT version

1. Increase the version number in `scenarioo-java/build.gradle` and append `-SNAPSHOT` to the `version` property.
2. Build the SNAPSHOT version locally [see Building a SNAPSHOT version of scenarioo-java](#building-a-snapshot-version-of-scenarioo-java)  
3. Commit the changes in scenarioo-java.
4. Update the `scenariooApiVersion` property at top of our build file `build.gradle` to the new SNAPSHOT version.
5. Add `mavenLocal()` to the `repositories` block in `build-java.gradle`.
6. Run all tests (including E2E tests)
7. Commit the changes in scenarioo in a branch and create a pull request.

### Releasing scenario-java

This is documented [here](https://github.com/scenarioo/scenarioo-java/blob/develop/docs/release-new-api.md) in the scenarioo-java project.    

## Working with scenarioo-js

Scenarioo-Client depends on scenarioo-js. The version used is defined in `package.json`. It is possible to use a scenarioo-js version from a specific branch, to check that the changes will still be compatible with scenarioo:
```
"scenarioo-js": "github:scenarioo/scenarioo-js#feature/scenarioo-676-remove-control-flow",
```

### Add scenarioo-js to IntelliJ

To fix issues in scenarioo-java you can import it into IntelliJ.
1. You need to clone the sources (if not yet):
     ```
     git clone https://github.com/scenarioo/scenarioo-js.git scenarioo-js
     ```
 
2. Import it into IntelliJ by using **"File/New/Module From Existing Sources"**.

### Running all tests in scenarioo-js

    ```
    cd scenarioo-js
    npm run test
    ```

### Releasing scenario-js

This is documented [here](https://github.com/scenarioo/scenarioo-js/blob/develop/docs/contribute/release.md) in the scenarioo-js project.

## Installing Tomcat

With Spring Boot it is no longer necessary to install Tomcat separately. However, before a release it might be sensible to check that Scenarioo also works when it is deployed into an external Tomcat.

* Download and install the latest version of Tomcat 8.5.x.
* Edit the "Scenarioo - Kater" run configuration so that it points to this Tomcat Application Server.

## Deploying Scenarioo into a standalone Tomcat in IntelliJ

* Execute the "Scenarioo - Kater" run configuration

## Build WAR File for external Server Deployement

The following command creates an executable war file which contains a tomcat server as well as the server and the client code.

```
./gradlew bootWar
```

## Client package update strategy (package.json)

Packages should b specified statically. 
Example: ``` "gulp-ng-annotate": "2.0.2" ```.

**Remark by Rolf:** I changed this from `2.0.x` to `2.0.2` to use the specific version even for minor version.
Reason: we should never automatically use newer minor versions - experiences show that it can always happen that even a minor update of a dependency makes Scenarioo not work anymore - and this is not acceptable that somebody can not work because just the day before they introduced a bug in a minor version --> Murphies Law! 
Therefore I think we should use fixed versions for all dependencies! 

Npm packages may be checked by using the command ``` npm outdated ```. You can update outdated packages by using ```npm update``` - but if you do so, you have to ensure that everything still works (e2e tests) and inform developers in case a new npm version is needed!
