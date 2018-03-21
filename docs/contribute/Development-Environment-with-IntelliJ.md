# Scenarioo Development Environment with IntelliJ

This readme contains important developer information for developers on how to develop the open source project Scenarioo with IntelliJ.

This is the recommended setup, to get up started and productive most quickly!

## Caution: Version of Documentation for Developers

The published documentation under http://www.scenarioo.org/docs may not reflect the most recent changes of scenarioo development. This is the state of our documentation at the last official release. For reading the most recent version of our documentation we recommend browsing it directly here on our develop branch: https://github.com/scenarioo/scenarioo/tree/develop/docs (or use the edit link on top of each doku page to browse to the corresponding most recent markdown file for that page)

## Prerequisites

 * Java JDK 1.8 
 * Take care to still set the language level to 1.6 in IntelliJ, because currently we still try to be backward compatible,
    for some projects that can not yet use Java 1.7 or 1.8 on their tomcat servers (for scenarioo 3.0 we will break that and can update to 1.8)
 * Tomcat 7 needs to be installed: http://tomcat.apache.org
    * you can also try Tomcat 8, which should also work, but might not yet be tested for this setup here

## Setup and Use of GIT 

 * Install GIT

 * Keep in mind to **always commit with Unix style line endings**, also if you are working on Windows (make sure to configure GIT accordingly, if not yet!). 
 
    * **For Windows:** We recommend to use following setting to ensure unix style line endings:
       ```
       git config --global core.autocrlf input
       ```
      If you not want to set this globaly, please set it at least for the scenarioo projects.
      See for more info: https://help.github.com/articles/dealing-with-line-endings/
      
 * **Make sure that you personalize your GIT by setting your username and email for commits (!! important !!)**:

     ```
      $ git config --global user.name "John Doe"             
      $ git config --global user.email johndoe@example.com
     ```
     **IMPORTANT - Set correct email: Make sure to configure the same email as registered in your github account**, otherwise your commits will not be recognized as contributions by you on github! 
     See also here: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup
     
 * **Checkout major repo:**
    ```
    git clone https://github.com/scenarioo/scenarioo.git
    ```
    * In case of troubles with `Filename too long` errors on windows:
    
        ```
        cd scenarioo
        git config core.longpaths true
        git checkout -f HEAD
        ```
        (if that does not help, check you have a new version of the windows git client!)

 * For most things you will work with the IntelliJ GIT client or use the GIT command line
     * In case you are a GIT newbie please ask your developer colleagues to help you or refer to the very good (and free) book at: http://git-scm.com/book to get started
     * Also following link might be helpful to understand how to work with the very good git client of IntelliJ: https://www.jetbrains.com/help/idea/2016.1/using-git-integration.html
      
 * Please refer to our **[branching strategy](https://github.com/scenarioo/scenarioo/blob/develop/docs/contribute/Branching-strategy.md) about how we use branches and create releases**.
    * using git flow for this might be very helpful: http://jeffkreeftmeijer.com/2010/why-arent-you-using-git-flow/
 
 * (optional) you can use whatever other GIT tools you need
    * for working with github, github desktop might be helpful: https://desktop.github.com/
    * on the linux developer VM we had once following additional tools installed (probably not needed when working with IntelliJ):
       * gitk : Very rich functionality
       * git gui : gui to stage, stash, commit and push your changes 
       * gitg : Simple git interface (very nice git history tree)
       * giggle : more of a git viewer to review changes in the files graphically

## Install NodeJS and NodeJS development tools

 * NodeJS: http://nodejs.org/download (newest version should be okay: higher than 6.2 !)
   _Use the latest versions of nodejs / npm / bower and you should be fine. If you discover that something does not work with the latest version and you have to fix it, please inform the community about it so that they have a chance to upgrade their tools._
   
 * Install nodejs dev tools globaly (might not be needed anymore, but is useful for working on command line):
   `npm install -g bower phantomjs protractor`

## Get the Sources

You need to clone at least the following repositories:

 * https://github.com/scenarioo/scenarioo
 * https://github.com/scenarioo/scenarioo-java

There are more interesting repositories with more examples and other writer libraries available under https://github.com/scenarioo

But this two repositories should be sufficient for most usual developers.

## Setup projects in IntelliJ

 * Install IntelliJ IDEA Ultimate
     * newest version
     * License: either use one of our scenarioo open source licenses or even better get yourself a commercial personal license (especially if you also need it for commercial work!)
 
 * Install IntelliJ Plugins (this list is not yet consolidated):
     * Gradle (probably allready included, but not sure)
     * NodeJs (if not allready included ?? not sure about that)
     * .gitignore plugin
     * Karma plugin (IntelliJ recommends this!)
     * Markdown Plugins:
         * Make sure to only use "Markdown support" by Jetbrains (it is now the best and should be part of IntelliJ)
         * disbale any other "Markdown"-Plugins (if you have, otherwise you might not be able to see and use the nice preview that comes with "Markdown support")
         * Make sure to use the new nice "JavaFX"-preview for markdown under Settings/Markdown/Preview (you will like that!)
         * after changing those plugins settings you might have to restart IntelliJ to let the changes take effect.
         * if you open a markdown file you should see a blue MD icon in the file's editor tab, and be able to choose a side-by-side View wirh Preview that looks nicely (in editor's toolbar).
                 
 * Import scenarioo web app by using "New project from existing sources":
     * choose 'scenarioo' folder
     * Import "From external model: Gradle" and use the gradle wrapper (default settings)
 * By using **"File/New module"** you can add additional repositories to be part of the same project setup in one IntelliJ window:
     * Import 'scenarioo-java' by using "New module from existing sources":
     * choose 'scenarioo-java' folder
     * Import "from external model: Gradle" and use the gradle wrapper (default settings)
     
 * From "Gradle"-tab in intelliJ simply run the following gradle tasks, to build everything cleanly:
    * scenarioo-java: `clean build test install` (this is needed as soon as your development branch uses latest snaphot of the writer!)
    * scenarioo: clean build test
         * take care to configure JVM 1.7 as runtime JVM for gradle, otherwise I got errors somehow when running this (Tab "Gradle">Button "Gradle Settings">Gradle JVM).
         * And if you get some python errors in npm install part on windows, you can probably ignore this optional npm dependency problems and just try to run it once again

 * Configure a run configuration to run the installed [Tomcat 7](http://tomcat.apache.org) from IntelliJ
     * set the tomcat path to tomcat 7 installation
     * set it running on port 8080     
     * on "Deployment" tab: 
        * choose to deploy the artifact "gradle....scenarioo-server...war" (not exploded) on startup
        * Application context (!important!): /scenarioo   
     * on "Startup/Connection" tab: set environment variable "SCENARIOO_DATA" to following path: &lt;your-project-source-path&gt;\scenarioo\scenarioo-docu-generation-example\build\scenarioDocuExample
     
 * Run all tests of the sub-project "scenarioodocu-generation-example" to generate scenarioo example documentation data in Folder "build/scenarioDocuExample"
    * run `./graldlew clean test` (or by choosing it in the Gradle View in IntelliJ, which should as well work)
    * alternativley: select folder 'test' under 'src' folder and right click on 'test' folder and choose "Run 'All Tests'
    * it is recommended to remember a run config for this step to regenerate test data when needed. 
    * **Hint for e2e tests:** This will bring the test data into the correct state (including viewer configuration in file config.xml) for running the e2e tests. It is required to rerun these tests before you execute e2e tests.
          
 * Start the tomcat server by using the run configuration:
   you should see in the log output that it is importing the example documentation data properly.
  
 * To start the web server for serving the Angular JS frontend (scenarioo-client)
   proceed as following (or as described in the Developer Guide, see below):
   ```
   cd scenarioo-client
   npm install
   npm serve
   # then open the browser to browse the application 
   # on given URL, usually http://localhost:9000
   # if you change files in the client the browser will refresh automatically
   ```

## Developer Guide

For more informations on how to develop, build and test scenarioo properly, please read the following carefully:  
 
**[Developer Guide](https://github.com/scenarioo/scenarioo/blob/develop/docs/contribute/Developer-Guide.md)**
   
## Testing the setup
    
### What has been tested and works with this setup

 [x] build and install scenarioo-java library (using gradle)
 [x] debugging the server (yes, check!)
 [x] run karma tests
 [x] run e2e-tests (runs very fast and very stable!)
 [x] develop ScenariooJS library
 [x] build ScenariooJS library
 [x] test ScenariooJS library (including e2e test example)
 
## Not yet tested 
 
 * change something in the writer library and link to it in server during development
 * release new writer library
 * release new web app (should work, when JVM is set to 1.7 for gradle)
 * release scenariooJS Library
 
## Known Issues

### General Issues

 * There seems to be an issue, when not using JVM 1.7 for gradle. But when this is currently configured to 1,7, it works well :-) Has been documented above accordingly.

## Open points - To be considered / improved / solved

### from old setup to be integrated in this development setup instructions here

 * check your JavaScript code against our `.eslintrc` file!
 * See also [WebStorm IDE Settings](https://github.com/scenarioo/scenarioo/blob/develop/docs/contribute/WebStorm-IDE-Settings.md).
 * All developers have to use same formatting and other java settings in eclipse, as explained [here](https://github.com/scenarioo/scenarioo/blob/develop/docs/contribute/Eclipse-IDE-Settings.md)

### should be improved

 * automation (skript or run config) to reset config before running e2e tests.
 * in general some run configs, maybe also some templates etc. that we can share for working more fast in IntelliJ.
 


