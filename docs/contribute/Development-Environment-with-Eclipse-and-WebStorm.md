Currently we try to migrate to a new developer setup using IntelliJ Ultimate.

This page contains the old setup instructions, for those still working with the old setup, until the new setup is more stable.

This page describes the setup of the development environment.

# Setting up your own development environment

Some of the core developers use a virtual machine with all the required tools for Scenarioo development.
Contact the core development team if you want to use the same virtual machine to start more quickly.

As this is an open source project, we would like to encourage contributions from any willing developer. Therefore this chapter tells you exactly what you need to get started with working on Scenarioo.

> More specific setup instructions for Ubuntu see [Development Environment Setup on Ubuntu](Development Environment Setup on Ubuntu).

> Develop using the scenarioo virtual machine [see here](Development-Environment-with-VM)

## General

* As the code is hosted on GitHub, you need **[git](http://git-scm.com/download)**. Keep in mind to **always commit with Unix style line endings**, also if you are working on Windows. See more hints about git usage below at the end of this this page.
* Our build uses Gradle, but you **do not need to install Gradle**, as we use the [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html)

## Client Development

For the client side of Scenarioo, you need to install these tools:

### Node.js

Official Download: http://nodejs.org/download (Windows, Mac, Linux Source)

Install gulp, bower, PhantomJS globally:
`npm install -g gulp bower phantomjs`

_Use the latest versions of nodejs / npm / bower and you should be fine. If you discover that something does not work with the latest version and you have to fix it, please inform the community about it so that they have a chance to upgrade their tools._

### IDE for web development

You can use your favourite Web IDE. It's best to use one that can check your JavaScript code against our `.eslintrc` file. We use [WebStorm](http://www.jetbrains.com/webstorm/).

> See also [WebStorm IDE Settings](WebStorm-IDE-Settings).

## Server Development

For the server side, you need to install these tools:

* **JDK 7**
* **[Eclipse IDE for Java EE Developers](http://www.eclipse.org/downloads/)** (Kepler) with these additional Plugins:
  * **Gradle IDE** (only pick this one!) from update site `http://dist.springsource.com/release/TOOLS/gradle`. Import the scenarioo root folder and all sub-projects into Eclipse using "Import Gradle Project".
  * All developers have to use same formatting and other java settings in eclipse, as explained here: https://github.com/scenarioo/scenarioo/wiki/Eclipse-IDE-Settings
* **[Tomcat 7](http://tomcat.apache.org)** (running on port 8080)
  * Add the server to the _Servers_ view in Eclipse and publish the _scenarioo-server_ under the path _/scenarioo_ (should be correct by default).
 
> See [Eclipse IDE Settings](Eclipse-IDE-Settings)

# GIT Usage, Settings and Tools

As a repository we use GIT on github.

Our major repositories are located here:
https://github.com/scenarioo/scenarioo
https://github.com/scenarioo/scenarioo-java

> We do not recommend the Eclipse git integration EGit but instead use the git support of WebStorm, which is very good.

However the best way to get used to work with git is to use the command line.

Please refer to [branching strategy](Branching-strategy) about how we use branches and create releases.

## Important GIT commands
Please refer to the very good (and free) book at http://git-scm.com/book to get started with git. 

## Useful Git settings for bash
see https://github.com/forkch/dotfiles/blob/develop/git-settings.sh
(make sure to download the files 'git-prompt.sh' and 'git-completion.sh' as well)

## Graphical GIT-Tools
After execution of the appropriate VM-Updatescript there are three graphical git tools installed
  * gitk : Very rich functionality
  * git gui : gui to stage, stash, commit and push your changes 
  * gitg : Simple git interface (very nice git history tree)
  * giggle : more of a git viewer to review changes in the files graphically

# Developer Guide

How to build, test, start, configure and browse the Scenarioo Viewer Web app locally, please refer to the [Developer Guide](Developer Guide)

