# Developer Guide

This page describes how to build, test, run the Scenarioo Viewer Webapp locally for developers on their developer machine.

## Prerequisites

You need a correctly setup Development environment for working on sceanrioo as described here: [Development Environment](Development-Environment.md)

## Build, Test and Run the Scenarioo Viewer Web App

This process describes how you clean update all your sources and build everything needed to properly run the demo locally and to run tests:

0. you need to have cloned all sources (if not yet):
    ```
    git clone https://github.com/scenarioo/scenarioo.git scenarioo
    git clone https://github.com/scenarioo/scenarioo-java.git scenarioo-java
    ```

1. Update and build the writer library and install the newest SNAPSHOT in local maven repo (only needed for develop and feature branches to use the newest development SNAPSHOT)
     ```
     cd scenarioo-java
     git pull
     ./gradlew clean build test install
     ```

2. build the server
    ```
    cd ~/scenarioo
    git pull
    ./gradlew clean build test war
    ```

3. Refresh and build in your IDE
   * Eclipse 
     * choose "refresh" on all projects
     * choose "Gradle > Refresh all" on 'scenarioo' project, and if needed on all other projects
     * if needed, choose "Gradle > Refresh dependencies" (e.g. if some dependency is not found or automatic dependency management in turned off) on all server and example-data-generator projects.
   * IntelliJ
     * TODO: what is required?

4. Generate the dummy test data:
   * Eclipse: choose to run the `AllTests` JUnit test suite (Exlipse run configuration `scenarioo-docu-generation-example-all-webtest`) in project 'scenarioo-docu-generation-example`: this builds all dummy data of the 'wikipedia-docu-example' that you need for testing the Scenarioo web app (and running e2e tests later).

5. Start up the `scenarioo-server` in Tomcat (usually done in your IDE).

6. Build, serve and browse the client of the web app:
    ```
    cd ~/scenarioo/scenarioo-client
    npm install
    npm start
    ```
    Then open the browser to browse the application under http://localhost:8500
    If you change files in the client the browser will refresh automatically
    
    Some remarks about this:
    * `npm install`: Installs node.js modules (mainly needed tools) as configured in `package.json`. They are placed in the folder `node_modules`. It also calls the relatively installed bower binary to install frontend dependencies.

7. Configure webapp correctly (if not yet) and browse it:
   * Go to the configuration page (under _Manage_ in the top right corner, then choose tab _General Settings_). You should see some preconfigured values, which means the client was able to reach the server.
   * In the field "Documentation Data Directory Path" you see where Scenarioo stores the `config.xml` file and where it expects documentation data. You can change the folder to a different one, see [Setup of Scenarioo Viewer Web App](../setup/Scenarioo-Viewer-Web-Application-Setup.md). You can either add your own `config.xml` file to this folder or let Scenarioo write the file the first time you save some configuration changes in the `Manage` section of Scenarioo.
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

The following command creates a war file which contains the server and the client code.

```
./gradlew war
```

## Client package update strategy (package.json)

Packages should by specified statically. 
Example: ``` "gulp-ng-annotate": "2.0.x" ```.

Only the bugfix version shall be dynamic. This will make our builds more stable. Specify the dynamic version by using the 'x' character.

Npm packages may be checked by using the command ``` npm outdated ```. You can update outdated packages by using ```npm update```.
