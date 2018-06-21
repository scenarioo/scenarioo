# Development Environment on Ubuntu

Refer to the main guide:
[Development Environment with IntelliJ](Development-Environment-Setup.md). This page only contains some
Ubuntu specific additions.


## Node.js

Install node.js (check [Development Environment with IntelliJ](Development-Environment-Setup.md) to see which 
version we currently use). [Installation instruction](https://github.com/nodesource/distributions).

To prevent problems with access rights of globally installed node modules (the ones where use use `npm install -g`) we suggest to create your own folder for storing global modules. One way to achieve this is to create a folder in your home directory and set it as the prefix for global npm installs. The idea is inspired by this [Stackoverflow post](http://stackoverflow.com/a/21712034/581553).

```
mkdir ~/.npm_global
sudo npm config set prefix ~/.npm_global -g
```

If you did this, the binaries that npm installs globally will be linked from `~/.npm_global/bin`. Therefore you have to add this path to your `PATH` variable. You can do this by adding the following line to `~/.bash_aliases`.

```
export PATH="$PATH:$HOME/.npm_global/bin"
```

Now you are ready to install the global node module that we need for Scenarioo.

```
npm install -g phantomjs
```


## Java Development Kit for Java 8

Install Java 8 JDK and make sure it is the default Java version on your system.

Here's the recommended way to do it: https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04

Execute `java -version` to make sure the version is 1.8.x.


## Tomcat

Just download and unzip. Don't use `apt-get` for this.
