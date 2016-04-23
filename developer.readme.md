# Scenarioo Developer How To

This readme contains important developer information for developers on how to develop the open source project Scenarioo.

**New developers have to read all this information (including linked resources) carefully before starting!**

## Setup Development Environment

**Currently we try to migrate to a new developer setup using IntelliJ Ultimate.**
**The described setup here is still experimental and use at your own risk!**

This chapter tells you exactly what you need to get started with working on Scenarioo in the area of the Scenarioo Viewer WebApp (including AngularJS frontend and Java REST backend) as well as the Java Writer library for writing content. 

Some of the core developers use a virtual machine with all the required tools for Scenarioo development.
Contact the core development team if you want to use the same virtual machine to start more quickly.

## Prerequisites

 * Java JDK 1.6 (you can probably use 1.8 JDK, but you have to set the language level to 1.6 in IntelliJ, because we still try to be backward compatible, for some projects that can not yet use 1.8).
      * currently it is recommended to install a JDK 1.7, because as @patrickschweizer told me, he had troubles with a JDK 8 to run the gradle build.
 * Tomcat 7 needs to be installed: http://tomcat.apache.org

## Setup and Use of GIT 

 * Install GIT

 * Keep in mind to **always commit with Unix style line endings**, also if you are working on Windows (make sure to configure GIT accordingly, if not yet!). 

 * **Make sure that you personalize your GIT by setting your username and email for commits (!! important !!)**:

     ```
      $ git config --global user.name "John Doe"             
      $ git config --global user.email johndoe@example.com
     ```
     see also here: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup

 * For most things you will work with the IntelliJ GIT client or use the GIT command line
     * In case you are a GIT newbie please ask your developer colleagues to help you or refer to the very good (and free) book at: http://git-scm.com/book to get started
     * Also following link might be helpful to understand how to work with the very good git client of IntelliJ: https://www.jetbrains.com/help/idea/2016.1/using-git-integration.html
      
 * Please refer to our **[branching strategy](Branching-strategy) about how we use branches and create releases**.
    * using git flow for this is very helpful: http://jeffkreeftmeijer.com/2010/why-arent-you-using-git-flow/
 
 * (optional) you can use whatever other GIT tools you need
    * for working with github, github desktop might be helpful: https://desktop.github.com/
    * on the linux developer VM we had once following additional tools installed:
       * gitk : Very rich functionality
       * git gui : gui to stage, stash, commit and push your changes 
       * gitg : Simple git interface (very nice git history tree)
       * giggle : more of a git viewer to review changes in the files graphically

## Install NodeJS and NodeJS development tools

 * NodeJS: http://nodejs.org/download (newest version should be okay)
   _Use the latest versions of nodejs / npm / bower and you should be fine. If you discover that something does not work with the latest version and you have to fix it, please inform the community about it so that they have a chance to upgrade their tools._
   
 * Install nodejs dev tools globaly:
   `npm install -g gulp bower phantomjs protractor`

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
     * NodeJs (if not included ?? not sure about that)
     * Markdown Plugins
     * .gitignore plugin
     * Karma plugin (IntelliJ recommends this!)
     * maybe more ... this is not yet well defined ...
          
 * Import scenarioo web app by using "New project from existing sources":
     * choose 'scenarioo' folder
     * Import "From external model: Gradle" and use the gradle wrapper (default settings)
 * By using **"File/New module"** you can add additional repositories to be part of the same project setup in one IntelliJ window:
     * Import 'scenarioo-java' by using "New module from existing sources":
     * choose 'scenarioo-java' folder
     * Import "from external model: Gradle" and use the gradle wrapper (default settings)
     
 * From "Gradle"-tab in intelliJ simply run the following gradle tasks, to build everything cleanly:
    * scenarioo-java: clean build test install
    * (TODO following does not work yet out of intelliJ somehow ... but you can work in IntelliJ anyway .... ignore it for now) scenarioo: clean build test (if you get some python errors in npm install part on windows, you can probably ignore this optional npm dependency problems and just try to tun it once again)

 * Configure a run configuration to run the installed [Tomcat 7](http://tomcat.apache.org) from IntelliJ
     * set the tomcat path to tomcat 7 installation
     * set it running on port 8080     
     * on "Deployment" tab: 
        * choose to deploy the artifact "gradle....scenarioo-server...war" (not exploded) on startup
        * Application context (!important!): /scenarioo   
     * on "Startup/Connection" tab: set environment variable "SCENARIOO_DATA" to following path: &lt;your-project-source-path&gt;\scenarioo\scenarioo-docu-generation-example\build\scenarioDocuExample
     
 * Run all tests of the sub-project "scenarioodocu-generation-example" (right click on folder and choose "Run 'All Tests'") 
   to generate example documentation data in Folder build/scenarioDocuExample

===== TODO issue #427 === 

     * when the issue #427 has been resolved, the following is obsolete: 
     
     ==== obsolete after #427 has been merged ====
         
     
        * Start the server using the run configuration, you should see something like following in the server log on first startup:
 
            WARN  org.scenarioo.dao.configuration.ConfigurationDaoImpl: no configuration directory is configured in server context, therefore trying to use fallback directory in user home.
            WARN  org.scenarioo.dao.configuration.ConfigurationDaoImpl:   file C:\Users\rbr\.scenarioo\config.xml does not exist --> loading default config.xml from classpath
 
        * Copy the file `scenarioo-server\src\main\resources\config-for-demo\config.xml` to the location where your server tries to load the configuration by default from (see server output).
                    [TODO: this will change with the new directory configuration by @mi-we soon anyway, see issue # )
                * change the configured documentation path configured inside this copied file to point to the following location instead:
                    * &lt;your-project-source-path&gt;\scenarioo\scenarioo-docu-generation-example\build\scenarioDocuExample
   
        * Stop the server again            
                    
    ==== end of obsolete part after #427 has been merged ===
    
    * then we have to test, that the above allready mentioned configuration of the variable "SCENARIOO_DATA" in tomcat run configuration works as expected.

=== end of TODO for issue #427 ===
 
 * Start the tomcat server by using the run configuration:
   you should see in the log output that it is importing the example documentation data properly.
  
 * To start the web server for serving the Angular JS frontend (scenarioo-client)
   proceed as following (or as described in the Developer Guide, see below):
   ```
   cd scenarioo-client
   npm install
   bower install
   gulp serve
   # then open the browser to browse the application 
   # on given URL, usually http://localhost:9000
   # if you change files in the client the browser will refresh automatically
   ```

## Developer Guide

For more informations on how to develop, build and test scenarioo properly, please read the following carefully:  
 
**[Developer Guide](https://github.com/scenarioo/scenarioo/wiki/Developer-Guide)**
   
## Testing the setup
    
### What has been tested and works with this setup

 [x] build and install scenarioo-java library (using gradle)
 [x] debugging the server (yes, check!)
 [x] run karma tests
 [x] gulp serve
 [x] run e2e-tests (runs very fast and very stable!)
 [x] develop ScenariooJS library
 [x] build ScenariooJS library
 [x] test ScenariooJS library (including e2e test example)
 
## Not yet tested 
 
 * change something in the writer library and link to it in server during development
 * release new writer library
 * release new web app (currently not working becuase of problems with gradle build)
 * release scenariooJS Library
 
 
## Open points - To be considered / improved / solved

### Problems to solve

#### On Windows

 * Gradle build fails 
     * probably only because npm install fails because of protractor optional dependencies having errors on windows (known issue)

 * When protractor is allready installed globaly (as on Rolf's machine, because that is what works better for windows, and that's how he uses it in his project):
     * webriver-manager update can not be run for local node_modules (./ does not work, can only run it for global protractor)
     * gulp uses local protractor --> needs local webdriver updated/installed
     * there is a gulp task webdriver_update, but somehow eslint, does not like this property name
     * see comments in gulp.js with workaround that I can currently not commit because of eslint

### from old setup to be integrated in this development setup instructions here

 * check your JavaScript code against our `.eslintrc` file!
 * See also [WebStorm IDE Settings](https://github.com/scenarioo/scenarioo/wiki/WebStorm-IDE-Settings).
 * All developers have to use same formatting and other java settings in eclipse, as explained here: https://github.com/scenarioo/scenarioo/wiki/Eclipse-IDE-Settings
 * See [Eclipse IDE Settings](https://github.com/scenarioo/scenarioo/wiki/Eclipse-IDE-Settings)

### should be improved

 * automation (skript or run config) to reset config before running e2e tests.
 * in general some run configs, maybe also some templates etc. that we can share for working more fast in IntelliJ.
 