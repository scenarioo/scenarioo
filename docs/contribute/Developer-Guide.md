# Developer Guide

This page describes how to build, test, run the Scenarioo Viewer Webapp locally for developers on their developer machine.

## Prerequisites

You need a correctly setup development environment for working on Scenarioo as described here: 
[Development Environment Setup](Development-Environment-Setup.md)

## Idea for developer guide

* Run test suites (./gradlew clean test)
* Run e2e tests (integrate from e2e tests page)
  * Reset test data
  * Run tests
  * Debug tests
  * Update chrome driver manually
* Working with writer libraries
  * scenarioo-java integration
  * set snapshot version (?)
  * run tests
* Using standalone Tomcat instead of Spring Boot
  * Install and configure Tomcat in IntelliJ
  * Build WAR file for deployment
* Update client packages
* anything else?

## Build, Test and Run the Scenarioo Viewer Web App

This process describes how you clean update all your sources and build everything needed to properly run the demo locally and to run tests:

0. You need to have cloned all sources (if not yet):
    ```
    git clone https://github.com/scenarioo/scenarioo.git scenarioo
    ```
    Depending on whether the current development state of the sources needs to have a lates and greates snapshot version of the Scenarioo Writer Library `scenarioo-java`, you might as well have to clone and build the writer library repo (check `scenariooApiVersion` property at top of our build file `build.gradle` to see whether it uses a `SNAèSHOT`-version, or try it without it, if you're feeling lucky):
    ```
    git clone https://github.com/scenarioo/scenarioo-java.git scenarioo-java
    ```

1. In case current version uses SNAPSHOT version of `scenariioo-java`: Update and build the writer library and install the newest SNAPSHOT in local maven repo:
     ```
     cd scenarioo-java
     git pull
     ./gradlew clean build test install
     ```

2. build the server
    ```
    cd ~/scenarioo
    git pull
    ./gradlew clean build test bootWar
    ```

3. Refresh and build in your IDE
    * for more information on how to work in IntelliJ see [Development Environment Setup](Development-Environment-Setup.md)  

4. Generate the dummy test data:
   * This was already done by running all tests.
   * But if you just want to clean your test data to a good state again, you can also choose 
     to only run all tests inside project `scenarioo-docu-generation-example`: 
     this sub module builds all dummy data of the 'wikipedia-docu-example' that you need 
     for testing the Scenarioo web app (and running e2e tests later).
     

5. Start up the `scenarioo-server` in Tomcat (usually done in your IDE), see [Development Environment Setup](Development-Environment-Setup.md) for more info on that.

6. Build, serve and browse the client of the web app:
    ```
    cd ~/scenarioo/scenarioo-client
    npm install
    npm start
    ```
    Then open the browser to browse the application under http://localhost:8500/scenarioo/  (Caution: webpack needs the slash at the end!)
    If you change files in the client the browser will refresh automatically
    
    Some remarks about this:
    * `npm install`: Installs node.js modules (mainly needed tools) as configured in `package.json`. They are placed in the folder `node_modules`.

7. Configure webapp correctly (if not yet) and browse it:
   * Go to the configuration page (under _Manage_ in the top right corner, then choose tab _General Settings_). You should see some preconfigured values, which means the client was able to reach the server.
   * In the field "Documentation Data Directory Path" you see where Scenarioo stores the `config.xml` file and where it expects documentation data. You can change the folder to a different one, see [Setup of Scenarioo Viewer Web App](../tutorial/Scenarioo-Viewer-Web-Application-Setup.md). You can either add your own `config.xml` file to this folder or let Scenarioo write the file the first time you save some configuration changes in the `Manage` section of Scenarioo.
   * In the _Builds_ tab on the _Manage_ page of Scenarioo you can click on the _Import & Update Builds_ link in the top right corner. You should see then that Scenarioo is either currently importing the build (state = PROCESSING) or already done with it (state = SUCCESS).
   * Reload the page, as soon as the state of the builds are SUCCESS the Branch _wikipedia-docu-example_ and it's builds should be selectable in the Scenarioo navigation bar. Select the build and navigate the example documentation. If you see a list of use cases, everything should be okay.

8. Before you start to develop, you should check that all unit tests and E2E test are successful, before you start to break them ;-) Make sure to run these tests regularly when you develop and to keep them successful:
  * run all unit tests:
    * run all java unit tests inside scenarioo-java
    * run all java unit tests inside scenarioo/scenarioo-server
    * run all java-script unit tests inside scenarioo/scenarioo-client:
      `npm test`
  * run all E2E tests (=web tests): see [E2E Testing](e2eTesting.md)

**Well done, now you're ready to code!**

Now have fun improving and extending Scenarioo, we are awaiting for your first pull request soon ;-)


## Build WAR File for Server Deployement

The following command creates an executable war file which contains a tomcat server as well as the server and the client code.

```
./gradlew bootWar
```

## Client package update strategy (package.json)

Packages should b specified statically. 
Example: ``` "gulp-ng-annotate": "2.0.2" ```.

**Remark by Rolf:** I changed this to from `2.0.x` to `2.0.2` to use the specific version even for minor version.
Reason: we should never automatically use newer minor versions - experiences sho that it can always happen that even a minor update of a dependency makes Scenarioo not work anymore - and this is not acceptable that somebody can not work because just the day before they introduced a bug in a minor version --> Murphies Law! 
Therefore I think we should use fix versions for all dependencies! 
(this replaced old comment, which was: "Only the bugfix version shall be dynamic. This will make our builds more stable. Specify the dynamic version by using the 'x' character.")

Npm packages may be checked by using the command ``` npm outdated ```. You can update outdated packages by using ```npm update``` - but if you do so, you have to ensure that everything still works (e2e tests?) and inform developers in case new npm version is needed!


## Stuff taken over from other sites

### scenarioo-java setup

Move senarioo-java parts to Developer Guide, this document here should be as condensed as possible to have a working setup

 * Configure a run configuration to run the installed [Tomcat 8](http://tomcat.apache.org) from IntelliJ
     * set the tomcat path to tomcat 8 installation
     * set it running on port 8080     
     * on "Deployment" tab: 
        * choose `+` to deploy the artifact from "External Source ..."
        * select `scenarioo-latest.war` from `sceanrioo-server/build/libs/`
        * Choose to run the gradle `scenarioo-server:war` before launch
        * IMPORTANT - Application context: `/scenarioo` 
        * See also https://stackoverflow.com/questions/27610259/building-war-with-gradle-debugging-with-intellij-idea
        * You can use `Control+F9` to trigger update of classes when server is running
     * on "Startup/Connection" tab: set environment variable "SCENARIOO_DATA" to following path: &lt;your-project-source-path&gt;\scenarioo\scenarioo-docu-generation-example\build\scenarioDocuExample
         * do not forget to also set the same in the "debug" mode!
 
 * Next, execute the "Scenarioo - Generate Testdata" run configuration
    * or on command-line run: `./gradlew -p scenarioo-docu-generation-example clean test`
 This is done when running the build task already, nice!
 
 
### from old setup to be integrated in this development setup instructions here
 
  * check your JavaScript code against our `.eslintrc` file!
  
### Tests to be done with new setup and developer guide pages
 
  - [ ] build and install scenarioo-java library (using gradle)
  - [ ] debugging the server (yes, check!)
  - [ ] run karma tests
  - [ ] run e2e-tests (runs very fast and very stable!)
  - [ ] develop ScenariooJS library
  - [ ] build ScenariooJS library
  - [ ] test ScenariooJS library (including e2e test example)
  - [ ] change something in the writer library and link to it in server during development
  - [ ] release new writer library
  - [ ] release new web app (should work, when JVM is set to 1.7 for gradle)
  - [ ] release scenariooJS Library
