# Important Scenarioo Developer How To

This readme contains important developer information for developers on how to develop the open source project Scenarioo.

## Setup Development Environment

**Currently we try to migrate to a new developer setup using IntelliJ Ultimate.**
**The described setup here is still experimental and use at your own risk!**

This chapter tells you exactly what you need to get started with working on Scenarioo in the area of the Scenarioo Viewer WebApp (including AngularJS frontend and Java REST backend) as well as the Java Writer library for writing content. 

Some of the core developers use a virtual machine with all the required tools for Scenarioo development.
Contact the core development team if you want to use the same virtual machine to start more quickly.

## Prerequisites

 * Java JDK 1.7: we still try to be backward compatible, for some projects that can not yet use 1.8

## Setup and Use of GIT 

 * Install GIT

 * Keep in mind to **always commit with Unix style line endings**, also if you are working on Windows (make sure to configure GIT accordingly, if not yet!). 

 * **Make sure that you personalize your GIT by setting your username and email for commits (!! important !!)**:

     ```
      $ git config --global user.name "John Doe"             
      $ git config --global user.email johndoe@example.com
     ```
     
     see also here: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup

 * In case you are a GIT newbie please ask your developer colleagues to help you or refer to the very good (and free) book at: http://git-scm.com/book to get started

 * For most things you will work with the IntelliJ GIT client or use the GIT command line

 * (optional) For working with github, github desktop might be helpful: https://desktop.github.com/

 * Please refer to our [branching strategy](Branching-strategy) about how we use branches and create releases.

 * (optional) some useful git settings for git bash (unsure whether this works for all platforms ...):
   see https://github.com/forkch/dotfiles/blob/develop/git-settings.sh 
   (make sure to download the files 'git-prompt.sh' and 'git-completion.sh' as well)

 * (optional) you can use whatever other GIT tools you need
    * (optional) on the linux developer VM we had once following recommended tools installed:
       * gitk : Very rich functionality
       * git gui : gui to stage, stash, commit and push your changes 
       * gitg : Simple git interface (very nice git history tree)
       * giggle : more of a git viewer to review changes in the files graphically
      

# Install NodeJS and NodeJS development tools

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
     * Gradle (if not included ??)
     * NodeJs (if not included ??)
     * Markdown Plugins
     * .gitignore plugin
     * maybe more ... this is not yet well defined ...
          
 * Import the projects by using "New project from existing sources":
    * scenarioo-java: Import "from external model: Gradle" and use the gradle wrapper (default settings)
    * scenarioo: Import "From external model: Gradle" and use the gradle wrapper (default settings)

## Open points from old setup to be integrated in this development setup instructions here

 * check your JavaScript code against our `.eslintrc` file!
 * See also [WebStorm IDE Settings](WebStorm-IDE-Settings).

  * All developers have to use same formatting and other java settings in eclipse, as explained here: https://github.com/scenarioo/scenarioo/wiki/Eclipse-IDE-Settings
  * **[Tomcat 7](http://tomcat.apache.org)** (running on port 8080)
        * Add the server to the _Servers_ view in Eclipse and publish the _scenarioo-server_ under the path _/scenarioo_ (should be correct by default).
 
  * See [Eclipse IDE Settings](Eclipse-IDE-Settings)


# Developer Guide

How to build, test, start, configure and browse the Scenarioo Viewer Web app locally, please refer to the [Developer Guide](Developer Guide)
