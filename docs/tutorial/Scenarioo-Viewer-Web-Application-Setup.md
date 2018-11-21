# Setup of Scenarioo Viewer Web App

## Use as Docker Image

Instead of installing the Scenarioo Viewer web app by yourself (as described below) you can simply run Scenarioo as a docker image: See [Scenarioo Viewer Docker Image](Scenarioo-Viewer-Docker-Image.md) for instructions.

## Download as WAR

The latest stable Scenarioo web application WAR file can be downloaded from the release section:
[Scenarioo Webapplication Releases](https://github.com/scenarioo/scenarioo/releases)

Scenarioo requires Java 8 or higher.

Further Release Candidate Versions are available through [Downloads & Links](../downloads-and-links.md), if you want to to test newer versions not yet officially released.

## Installation and Setup
### Running Scenarioo with the provided Tomcat Webserver
1. **Configure the context** under which the app should be deployed by creating the file `config/application.properties` next to the downloaded war, with the following entry
     ```
     server.servlet.contextPath=/scenarioo
     ```
     If this file is missing, the app can be reached under the default context `scenarioo`.
     
     The Tomcat access log can also be enabled using `application.properties`. Just add the following entries:
     ```
     server.tomcat.accesslog.enabled=true
     server.tomcat.accesslog.directory=<absolute path to log-directory>
     ```

2. **Execute the WAR** file with Java, e.g 
     ```
     cd <war-location>
     java -jar scenarioo.war
     ```

### Installing Scenarioo into a Webserver
Alternatively you can install your favorite Webserver and deploy the WAR into it.

1. **Install a Tomcat Webserver** or any other favourite Java webserver (Tomcat 8.5.31+ or 9 should all work with Scenarioo). You can download and unzip one from from http://tomcat.apache.org/ 

2. **Deploy the WAR** file into it, e.g. simply copy the WAR file into the webapps folder on tomcat. 
    * If you use a release candidate WAR file that has the version in the name, then make sure to rename the war to simply `scenarioo.war` before deploying it, because the name defines the context under which you can access the app.

### Setup
1. **Configure the Scenarioo data directory** where Scenarioo can store and read its data. By default Scenarioo is using a folder `.scenarioo` in your "user.home"-directory.
    * To configure a different directory to use add the following line to context.xml in your webserver's installation directory (usually located in folder 'conf' of your tomcat installation):  
`<Parameter name="scenariooDataDirectory" value=">>>path to a directory where scenarioo can store its data<<<" override="true" description="Location of Scenarioo data"/>`
    * Alternatively you can set an environment variable called `SCENARIOO_DATA` for the user under which Tomcat is running and define the path there
    * Make sure that the Tomcat user has the correct access rights for the folder (permission to create files and directories inside). Under linux systems you can do this with following commands:
     ```
     sudo chown -R tomcat: <scenarioo-docu-directory>
     sudo chmod ug+rws <scenarioo-docu-directory> 
     ```
    * The config.xml file will be created for you automatically in this directory by the Scenarioo Viewer Web Application when you do some configuration changes inside the webapplication for the first time and save it.

2. **Start the Tomcat Server** (or whatever java server you are using)

3. **Startup Scenarioo Viewer Web App** through your browser using following URL: http://{your-ip-address-and-port-or-host-url}/scenarioo (e.g. http://localhost:8080/scenarioo )

4. **Configure your Viewer and advanced features (optional)** as described in [Viewer Configuration](Configuration.md).
