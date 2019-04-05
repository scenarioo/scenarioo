# Development Environment Setup

This guide helps you setting up all the tools you need to make a contribution to the Scenarioo source code using
IntelliJ. 

This is the recommended setup, which means we are most likely able to support you in case of issues.

There are some additional notes for Ubuntu users documented under [Development Environment on Ubuntu](Development-Environment-Ubuntu.md).


## Documentation for Developers

The published documentation under http://www.scenarioo.org/docs/develop should reflect the most recent changes of Scenarioo 
development. This is the state of our documentation on develop branch.

Alternatively, you can find the newest sources of our documentation here:
https://github.com/scenarioo/scenarioo/tree/develop/docs 

If during setup you find some information that is outdated in the documentation please help to improve it and contribute the changes as a Pull Request. 
You can click the "Edit this page" link on top of these pages to navigate directly to the edit window of said page.

Thanks a lot!

## Install Tools

The following tools should be installed and running on your computer:

 * Git (most recent version)
 * Java JDK (1.8)
 * Node.js (8.11+)
 * IntelliJ IDEA Ultimate (most recent version) 
   * Ask @bruderol if you want to use an open source license for it
   
To work with the Full Text Search feature, you additionally need the following:

 * Elasticsearch (5+)
   * See [Elasticsearch 5 Setup Instructions for CI](../contribute/ci-server-setup/Elasticsearch.md) for how to setup the same as on CI, or [Full Text Search Setup Guide](../features/full-text-search/setup.md) for details

   
## Setup and Use of Git

 * For most things you will work with the IntelliJ GIT client or use the GIT command line
     * If you have not worked with git before, one way to get familiar with it is the very good (and free) book at http://git-scm.com/book
     * Also following link might be helpful to understand how to work with the very good git client of IntelliJ: https://www.jetbrains.com/help/idea/2016.1/using-git-integration.html
 * Always commit with Unix style line endings
    * **For Windows:** We recommend to use following setting to ensure unix style line endings:
       ```
       git config --global core.autocrlf input
       ```
      If you don't want to set this globally, please set it at least for the Scenarioo projects.
      See for more info: https://help.github.com/articles/dealing-with-line-endings/
 * Make sure that you personalize your GIT by setting your username and email for commits:

     ```
      $ git config --global user.name "John Doe"             
      $ git config --global user.email johndoe@example.com
     ```
     :warning: Make sure to configure the same email as registered in your github account
     See also here: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup
     
 * Please refer to our [Branching Strategy](Branching-strategy.md) for more information about how we use branches and create releases


## Get the Sources

For a start, clone the following repositories:

```
    git clone https://github.com/scenarioo/scenarioo.git
    git clone https://github.com/scenarioo/scenarioo-java.git
```

There are more interesting repositories with more examples and other writer libraries available under https://github.com/scenarioo

**For Windows:** In case of troubles with `Filename too long` errors
    
```
cd scenarioo
git config core.longpaths true
git checkout -f HEAD
```

Also make sure you have an up-to-date version of Git installed.

## Setup Projects in IntelliJ

 * Install IntelliJ Plugins (this list is not yet consolidated):
     * Gradle (probably already included, but not sure)
     * NodeJs (if not already included ?? not sure about that)
     * .gitignore plugin
     * Karma plugin (IntelliJ recommends this!)
     * Markdown Plugins:
         * Make sure to only use "Markdown support" by Jetbrains (it is now the best and should be part of IntelliJ)
         * disbale any other "Markdown"-Plugins (if you have, otherwise you might not be able to see and use the nice preview that comes with "Markdown support")
         * Make sure to use the new nice "JavaFX"-preview for markdown under Settings/Markdown/Preview (you will like that!)
         * after changing those plugins settings you might have to restart IntelliJ to let the changes take effect.
         * if you open a markdown file you should see a blue MD icon in the file's editor tab, and be able to choose a side-by-side View wirh Preview that looks nicely (in editor's toolbar).
                 
 * Import Scenarioo web app by using "New project from existing sources":
     * choose 'scenarioo' folder
     * Import "From external model: Gradle" and use the gradle wrapper (default settings)
 * By using **"File/New module"** you can add additional repositories to be part of the same project setup in one IntelliJ window:
     * Import 'scenarioo-java' by using "New module from existing sources":
     * choose 'scenarioo-java' folder
     * Import "from external model: Gradle" and use the gradle wrapper (default settings)
     
 * From "Gradle"-tab in intelliJ simply run the following gradle tasks, to build everything cleanly:
    * scenarioo-java: `clean build install` (this is needed as soon as your development branch uses latest snaphot of the writer!)
    * scenarioo: `clean build`
         * take care to configure JVM 1.8 as runtime JVM for gradle (Tab "Gradle">Button "Gradle Settings">Gradle JVM).
         * And if you get some python errors in npm install part on windows, you can probably ignore this optional npm dependency problems and just try to run it once again (or use something like `npm install -g npm-build-tools` to fix it)

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
    
 * Run all tests of the sub-project "scenarioodocu-generation-example" to generate Scenarioo example documentation data in Folder "build/scenarioDocuExample"
    * run `./graldlew clean test` (or by choosing it in the Gradle View in IntelliJ, which should as well work)
    * alternativley: select folder 'test' under 'src' folder and right click on 'test' folder and choose "Run 'All Tests'
    * it is recommended to remember a run config for this step to regenerate test data when needed. 
    * **Hint for e2e tests:** This will bring the test data into the correct state (including viewer configuration in file config.xml) for running the e2e tests. It is required to rerun these tests before you execute e2e tests.
          
 * Start the tomcat server by using the run configuration:
   you should see in the log output that it is importing the example documentation data properly.
  
 * To start the web server for serving the Angular JS frontend (scenarioo-client)
   proceed as following:
   ```
   cd scenarioo-client
   npm install
   npm start
   ```

 * Then open the browser to browse the application on the given URL, usually http://localhost:8500
   * If you change files in the client the browser will refresh automatically


## Developer Guide

For more information on how to develop, build and test Scenarioo properly, please read the [Developer Guide](Developer-Guide.md)!
   
## Testing the Setup
    
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

 * There seems to be an issue, when not using JVM 1.8 for gradle. But when this is currently configured to 1.8, it works well :-) Has been documented above accordingly.

## Open points - To be considered / improved / solved

### from old setup to be integrated in this development setup instructions here

 * check your JavaScript code against our `.eslintrc` file!

### should be improved

 * automation (skript or run config) to reset config before running e2e tests.
 * in general some run configs, maybe also some templates etc. that we can share for working more fast in IntelliJ.
