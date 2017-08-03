# Setup of Scenarioo Viewer Web App

## Use as Docker Image

Instead of installing the Scenarioo Viewer web app by yourself (as described below) you can simply run scenarioo as a docker image: See [Scenarioo Viewer Docker Image](Scenarioo-Viewer-Docker-Image.md) for instructions.

## Download as WAR

The latest stable Scenarioo web application WAR file can be downloaded from the release section:
[Scenarioo Webapplication Releases](https://github.com/scenarioo/scenarioo/releases)

Scenarioo requires Java 6 or higher.

Further Release Candidate Versions are available through [Downloads & Links](downloads-and-links.md), if you want to to test newer versions not yet officially released.

## Installation and Setup

1. **Install a Tomcat Webserver** or any other favourite Java webserver (tomcat 7, 8 or 9 should all work with scenarioo 3).

2. **Deploy the WAR** file into it, e.g. simply copy the WAR file into the webapps folder on tomcat. If you use a release candidate WAR file that has the version in the name, then make sure to rename the war to simply `scenarioo.war` before deploying it, because the name defines the context under which you can access the app.

3. **Configure the Scenarioo Config Directory** where scenarioo can store and read configuration data from (and that we as well recommend to use as the directory to store documentation data inside as well, see later). By default scenarioo is using a folder `.scenarioo` in your "user.home"-directory. You have to ensure that the scenarioo webapplication has access rights to create additional data (directories and files) inside this directory. Otherwise you will encounter some exceptions on server startup or when you try to save configuration changes in the webapplication. Proceed as follows to configure such a directory:
  * To configure a different directory to use add the following line to context.xml in your webserver's installation directory (usually located in folder 'conf' of your tomcat installation):  
`<Parameter name="scenariooConfigurationDirectory" value=">>>path to a directory where scenarioo can store its configuration data<<<" override="true" description="Location of scenarioo config.xml file"/>`
  * Make sure that the tomcat user has the correct access rights for this folder (needs rights to create files and directories inside). Under linux systems you can do this with following commands:
     ```
     sudo chown -R tomcat: <scenarioo-docu-directory>
     sudo chmod ug+rws <scenarioo-docu-directory> 
     ```
  * the config.xml file will be created for you automatically in this directory by the Scenarioo Viewer Web Application when you do some configuration changes inside the webapplication for the first time and save it (see later).

4. **Start the Tomcat Server** (or whatever java server you are using)

5. **Startup Scenarioo Viewer Web App** through your browser using following URL: http://{your-ip-address-and-port-or-host-url}/scenarioo (e.g. http://localhost:8080/scenarioo )

6. **Configure the Scenarioo Documentation Files Directory** where scenarioo can read and write documentation data from:
  * Currently this can and has to be configured separately on the configuration tab in the "Manage" area of the Viewer web application: use top right link "Manage" and browse to "General Settings" tab.
  * **Configure the directory under `Documentation Data Directory Path`** by providing the full absolute path to the directory you want to use to store all documentation data. 
  * **We strongly recommend to use the same path for this setting that you already configured for the config directory (see point 3).** First because the directory needs to have same access rights as described for config directory already. And second because in future scenarioo versions we will remove this setting and use only one directory for storing all the documentation data and config data in one central place. By configuring this directory to point to the same scenarioConfigurationDirectory you are allready well prepared for updates to future scenarioo versions.

7. **Configure your Viewer and advanced features (optional)** as described in [Viewer Configuration](Configuration.md). 
