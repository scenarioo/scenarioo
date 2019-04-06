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

## Quick Setup

### Install Tools

The following tools should be installed and running on your computer:

 * Git (most recent version)
 * Java JDK (1.8)
 * Node.js (8.11+)
 * IntelliJ IDEA Ultimate (most recent version) 
   * Ask @bruderol if you want to use an open source license for it
   
To work with the Full Text Search feature, you additionally need the following:

 * Elasticsearch (5+)
   * See the [Full Text Search Setup Guide](../features/full-text-search/setup.md) for details on how to setup.

   
### Setup Git

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
     git config --global user.name "John Doe"             
     git config --global user.email johndoe@example.com
     ```
     :warning: Make sure to configure the same email as registered in your github account
     See also here: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup
     
 * Please refer to our [Branching Strategy](Branching-strategy.md) for more information about how we use branches and create releases


### Get the Sources

Clone the Scenarioo viewer application repository:

```
git clone https://github.com/scenarioo/scenarioo.git
```

For working on the viewer application, this is enough. See the [Developer Guide](Developer-Guide.md) for more information on how to work with the writer libraries as well.
For a complete list of all repositories, check https://github.com/scenarioo.

**For Windows:** In case of troubles with `Filename too long` errors
    
```
cd scenarioo
git config core.longpaths true
git checkout -f HEAD
```

Also make sure you have an up-to-date version of Git installed.

### Build and run the application

 * Install the following IntelliJ IDEA plugins if you don't have them already:
   * NodeJs
   * Karma
   * .ignore
                 
 * Import Scenarioo by using "New project from existing sources":
   * Choose 'scenarioo' folder
   * Import "From external model: Gradle" and use the gradle wrapper (default settings)
   
 * Build the viewer application by executing the "Scenarioo - Full Clean Build" run configuration
   * or on command-line run: `./gradlew clean build bootWar`
   * :warning: Make sure JVM 1.8 is configured as runtime JVM for Gradle (Tab "Gradle">Button "Gradle Settings">Gradle JVM).
   * **On Windows:** If you get some Python errors during "npmInstall" task, you can probably ignore this optional npm dependency problems and just try to run it once again (or use something like `npm install -g npm-build-tools` to fix it)
    
 * Execute the "Scenarioo - Fruehligsstiefel" run configuration
   * or on command-line run: 
   ```
   # Set the path to the example data in your environment variables (system-dependent)
   export SCENARIOO_DATA=&lt;your-project-source-path&gt;/scenarioo-docu-generation-example/build/scenarioDocuExample 
   ./gradlew bootRun
   ```

   * This starts the viewer application backend as a standalone Spring Boot app
   * You should see in the log output that it is importing the example documentation data properly.
   
 * Finally, start serving the frontend:
   ```
   cd scenarioo-client
   npm start
   ```
   * This will spawn a webpack development server locally

 * Now you can access the viewer application by browsing to http://localhost:8500/scenarioo/
   * :warning: The `/` at the end of the URL is mandatory!
   * If you change files in the client code, the browser will refresh automatically


## Developer Guide

Great, by now you should be ready to work on your first issues!

For more information on how to develop, build and test Scenarioo properly, head over to the [Developer Guide](Developer-Guide.md)!
   
## Known Issues

### General Issues

 * There seems to be an issue, when not using JVM 1.8 for gradle. But when this is currently configured to 1.8, it works well :-) Has been documented above accordingly.
