# Development Environment Setup

This guide helps you setting up all the tools you need to make a contribution to the Scenarioo source code using
IntelliJ. 

This is the recommended setup, which means we are most likely able to support you in case of issues.

There are some additional notes for Ubuntu users documented under [Development Environment on Ubuntu](Development-Environment-Ubuntu.md).

## Quick Setup

### Install Tools

The following tools should be installed and running on your computer:

 * Git (most recent version)
 * Java JDK 1.8
 * IntelliJ IDEA Ultimate (most recent version) 
   * Ask @bruderol if you want to use an open source license for it
 * Docker: highly recommended for local e2e testing
 * Graphviz & PlantUML: optional, but recommended to see and edit UML diagrams in our markdown documentation
   * see paragraph below about [Setup PlantUML](#setup-plantuml-for-diagrams-in-documentation)
 * Node.js 8.11+ (optional)
    * To work in the scenarioo-js writer library, you will need Node.js
    * To work in the scenarioo-java writer library or in the scenarioo viewer application, Node.js is optional 
   
To work with the Full Text Search feature, you additionally need one of the following:

 * Docker
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

### Install IntelliJ Plugins and import Scenarioo

 * Install the following IntelliJ IDEA plugins if you don't have them already:
   * NodeJs
   * Karma
   * .ignore 
   * PlantUML (useful for UML preview in PUML files)
  
 * Install Graphviz and PlantUML as described later [here](#setup-plantuml-for-diagrams-in-documentation) to be able to see and edit diagrams directly in markdown files.
                 
 * Import Scenarioo by using "New project from existing sources":
   * Choose 'scenarioo' folder
   * Import "From external model: Gradle" and use the gradle wrapper (default settings)
   * A dialog appears asking whether the existing .idea folder should be overwritten. Select `Yes`. :warning: This deletes all existing run configurations, thus you need to revert the changes (see next step).
   * Do a revert of all changes: Menu `VCS > Git > Revert` -> `Revert`
   
### Install and Configure Docker

If you want to use the ElasticSearch run configuration in IntelliJ to run the Full Text Search E2E tests, you have to install Docker and configure it correctly. Otherwise you can skip this step.

After installing Docker you need to make configuration changes for IntelliJ and ElasticSearch to work correctly.

In Docker Settings:
 * "General" > "Expose daemon on tcp://localhost:2375 without TLS"
    * This is needed so that our IntelliJ Docker run configurations can connect to Docker
 * "Advanced" > "Memory" > 4096MB (or more)
    * The default is 2048MB, this is not enough, because ElasticSearch alone needs 2GB to start.
 * "Shared Drives" > Share the drive where you checked out the Scenarioo repository
    * This is needed if you want to use the "Scenarioo - Hafenarbeiter komponiert Dev Cluster" run configuration, because it accesses the war and the generated test data. 

### Setup PlantUML for Diagrams in Documentation

We use PlantUML notation for diagrams in our markdown documentation. 

An example of such a diagram can be found [here in the documentation of the Details feature](../features/details/Details.md)

To render and edit those diagrams you need Graphviz installed and PlantUML in IntelliJ.

Setup:
* Install Graphviz:
   * download from here: https://graphviz.gitlab.io/download/
   * see also https://plantuml.com/de/graphviz-dot for more installation instructions     
* Install PlantUML for IntelliJ (see also https://www.jetbrains.com/help/idea/markdown.html#diagrams)
  * File / Settings / Language & Frameworks / Markdown
  * Click "Install" next to "PlantUML framework isn't installed"

When this is installed you should see the diagrams in Markdown previews in IntelliJ (otherwise empty your intelliJ cache to regenerate). You should be able to modify the diagrams in markdown text and immediately see the changes.
 
## Developer Guide

For more information on how to develop, build and test Scenarioo properly, head over to the [Developer Guide](Developer-Guide.md)!
