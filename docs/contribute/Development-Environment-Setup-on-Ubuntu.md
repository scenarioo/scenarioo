# Dev Env Setup on Ubunut

**There is a new setup with IntelliJ that we recommend to use now: [Development Environment with IntelliJ](Development-Environment-with-IntelliJ.md)**

@adiherzog Still needed to keep this? Is this up to date? Can we remove some redundancy with other explanations?

## Git

```
sudo apt-get install git
```

## NPM

Install node.js (Version 0.10.33 or newer). See here for installation instructions: https://github.com/nodesource/distributions.

To prevent problems with access rights of globally installed node modules (the ones where use use `npm install -g`) we suggest to create your own folder for storing global modules. One way to achieve this is to create a folder in your home directory and set it as the prefix for global npm installs. The idea is inspired by this [Stackoverflow post](http://stackoverflow.com/a/21712034/581553).

```
mkdir ~/.npm_global
sudo npm config set prefix ~/.npm_global -g
```

If you did this, the binaries that npm installs globally (typically `gulp` for example) will be linked from `~/.npm_global/bin`. Therefore you have to add this path to your `PATH` variable. You can do this by adding the following line to `~/.bash_aliases`.

```
export PATH="$PATH:$HOME/.npm_global/bin"
```

Now you are ready to install the few global node modules that we need for Scenarioo.

```
npm install -g gulp bower phantomjs
```

## Clone the Scenarioo repositories

If you did not clone the Scenarioo repo yet, do so now:

```
git clone https://github.com/scenarioo/scenarioo.git scenarioo
git clone https://github.com/scenarioo/scenarioo-java.git scenarioo-java
```

Next, install the local modules needed by the Scenarioo client. For this, you have to navigate to the `scenarioo/scenarioo-client` folder that you checked out. Then execute:

```
npm install
```

## Java Development Kit for Java 6

Install Java 6 JDK and make sure it is the default Java version on your system. If you want to use WebStorm 9, the official Sun / Oracle Java version is required. Otherwise you can also use OpenJDK. We can recommend the following way to do it ([as described here](http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html)):

```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java6-installer
```

Execute `java -version` to make sure the version is 1.6.xx.

Alternatively you can also use OpenJDK.

```
sudo apt-get install openjdk-7-jdk
```

## Tomcat

Just download and unzip. Don't use `apt-get` for this.

Then add it in the Eclipse Servers section.

For running Scenarioo you need to speicify the folder where Scenarioo should store its config.xml file. Add the following line to Tomcats context.xml file and set the value you want.

```
<Parameter name="scenariooConfigurationDirectory" value=">>>path to a directory where your scenarioo config.xml will be or is stored<<<" override="true" description="Location of scenarioo config.xml file"/>
```

## IDEs and Browsers

If you want to use the same tools that the Scenarioo core developers use, install these:

* Variant 1: IntelliJ 14 or newer
* Variant 2: Eclipse + WebStorm
  * Eclipse Luna
    * Add the Gradle plugin (http://dist.springsource.com/release/TOOLS/gradle, only select Gradle IDE). See [here](http://marketplace.eclipse.org/content/gradle-integration-eclipse-44#.VHuc2zGG88q) for Eclipse Luna.
    * Import the project using `File` -> `Import...` -> `Gradle` -> `Gradle Project`. Browse to the location where you checked out the Scenarioo repository. Click `Build Model`. Then select all projects except `scenarioo-client` and import them.
  * WebStorm 9
* Firefox and Google Chrome

## Build Scenarioo Viewer Web App

If this worked so far without ugly error messages, it looks all fine and you could work with your development environment. 

How to build, test, start, configure and browse the Scenarioo Viewer Web app locally, please refer to the [Developer Guide](Developer-Guide.md)
