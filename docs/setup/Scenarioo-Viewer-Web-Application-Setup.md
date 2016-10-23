# Use as Docker Image

Instead of installing the Scenarioo Viewer web app by yourself (as described below) you can simply run scenarioo as a docker image: See [Scenarioo Viewer Docker Image](Scenarioo-Viewer-Docker-Image) for instructions.

# Download as WAR

The latest stable Scenarioo web application WAR file can be downloaded from the release section:
[Scenarioo Webapplication Releases](https://github.com/scenarioo/scenarioo/releases)

Scenarioo requires Java 6 or higher.

# Installation and Setup

1. Install a tomcat webserver or any other favourite Java webserver (currently we recommend tomcat7, all other servers are currently untested to be compatible with scenarioo).

2. Deploy the WAR file into it, e.g. simply copy the WAR file into the webapps folder on tomcat. If you use a release candidate WAR file that has the version in the name, then make sure to rename the war to simply `scenarioo.war` before deploying it, because the name defines the context under which you can access the app.

3. Configure the directory where you want to store documentation data and scenarioo can read this data from and store additional data inside, like e.g. its configuration. By default scenarioo is using a folder `.scenarioo` in your "user.home"-directory. You have to ensure that the scenarioo webapplication has access rights to create additional data (directories and files) inside this directory. Otherwise you will encounter some exceptions on server startup or when you try to save configuration changes in the webapplication. Proceed as follows to configure such a directory:
  * To configure a different directory to use add the following line to context.xml in your webserver's installation directory (usually located in folder 'conf' of your tomcat installation):  
`<Parameter name="scenariooConfigurationDirectory" value=">>>path to a directory where scenarioo can store its configuration data<<<" override="true" description="Location of scenarioo config.xml file"/>`
  * Make sure that the tomcat user has the correct access rights for this folder (needs rights to create files and directories inside). Under linux systems you can do this with following commands:

     ```
     sudo chown -R tomcat: <scenarioo-docu-directory>
     sudo chmod ug+rws <scenarioo-docu-directory> 
     ```

  * the config.xml file will be created for you automatically by the Scenarioo webapplication inside this directory, when you do some configuration changes inside the webapplication for the first time and save it.

4. start the tomcat server (or whatever java server you are using)

5. Startup scenarioo through your browser using following URL: http://{your-ip-address-and-port-or-host-url}/scenarioo (e.g. http://localhost:8080/scenarioo )

6. Configure your scenarioo webapplication in the "Manage" area (see top right link in the application) on the "Configuration" tab. 

    * You have to configure the directory where you want to generate the documentation content into, which must also be a directory where the webapplication has write access and can create sub folders (see linux access right commands above). We recommend to configure the same directory, where you allready configured to store the configuration for scenarioo, such that you have all the scenarioo data stored in one place. 
    * In future versions of scenarioo we will anyway change this, such that you can only configure one scenarioo data directory and this will be only configurable in the server configuration (and not through the globaly accessible config UI anymore). So by configuring this directory to point to the same scenarioConfigurationDirectory you are allready well prepared for updates to future scenarioo versions.
    
7. **Setup Additional Features (Optional):** For being able to use following additional features, you have to follow additional setup instructions for using this special features that require additional configuration and setup steps:

* **Full Text Search**: For searching any text in Scenarioo see [Full Text Search Setup Instructions](../features/full-text-search/setup.md)

* **Diff-Viewer Screen Comparison** for Image-Comparison of Screenshots see [DiffViewer Setup Instructions](../features/diff-viewer/setup.md)

