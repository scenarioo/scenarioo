# Setup of Scenarioo Viewer Web App

## Use as Docker Image

Instead of installing the Scenarioo Viewer web application by yourself (as described below) you can simply run Scenarioo as a docker image: See [Scenarioo Viewer Docker Image](Scenarioo-Viewer-Docker-Image.md) for instructions.

## Download

The latest stable Scenarioo web application WAR file can be downloaded from the release section:
[Scenarioo Webapplication Releases](https://github.com/scenarioo/scenarioo/releases)

Scenarioo requires Java 8 or higher. 

You can directly run the standalone runnable WAR without needing to install any application server, or you can choose to deploy it to your application server of choice (e.g. Tomcat), this is up to you. See the next section about installation and setup.

Further Release Candidate Versions are available through [Downloads & Links](../downloads-and-links.md), if you want to to test newer versions not yet officially released.

## Installation and Setup

Choose one of the two following setups explained in the following sections:
* **[Setup 1 - Running as Standalone Application](#setup-1---running-as-standalone-application):** Scenarioo is packaged as Spring Boot application with an included Tomcat web server to run it directly without the need to install and deploy to a web server. 
* **[Setup 2 - Deploy WAR on Web Server](#setup-2---deploy-war-on-web-server):** This works the same way as before the standalone runner was available, you need to have a web server (like e.g. Tomcat) you can deploy to.

### Setup 1 - Running as Standalone Application

Run Scenarioo directly without the need to install a web server.

1. **Configure the Scenarioo data directory** where Scenarioo can store and read its data. By default Scenarioo is using a folder `.scenarioo` in your "user.home"-directory.
    
    To configure a different data directory either set the environment variable `SCENARIOO_DATA` or add the following line to `application.properties` next to the downloaded war:  
    ```
    scenarioo.data=<path to a directory where scenarioo can store its data>
    ```
    
2. **Execute the WAR** file with Java, e.g 
     ```
     cd <war-location>
     java -jar scenarioo.war
     ```

3. **Startup Scenarioo Viewer Web App** through your browser using the following URL: http://{your-ip-address-and-port-or-host-url}/scenarioo (e.g. http://localhost:8080/scenarioo )

4. **Configure your Viewer and advanced features (optional)** as described in [Viewer Configuration](Configuration.md).

### Setup 2 - Deploy WAR on Web Server

Alternatively you can deploy the Scenarioo WAR file into your favorite webserver (e.g.Tomcat).

1. **Install a Tomcat webserver** or any other favorite Java webserver (Tomcat 8.5.31+ or 9 should all work with Scenarioo). You can download and unzip one from here: http://tomcat.apache.org/ 

2. **Configure the Scenarioo data directory** where Scenarioo can store and read its data. By default Scenarioo is using a folder `.scenarioo` in your "user.home"-directory.
    * To configure a different directory to use, add the following line to context.xml in your webserver's installation directory (usually located in folder 'conf' of your Tomcat installation):  
`<Parameter name="scenariooDataDirectory" value=">>>path to a directory where scenarioo can store its data<<<" override="true" description="Location of Scenarioo data"/>`
    * Alternatively you can set an environment variable called `SCENARIOO_DATA` for the user under which Tomcat is running and define the path there
    * Make sure that the Tomcat user has the correct access rights for the folder (permission to create files and directories inside). Under linux systems you can do this with following commands:
      ```
      sudo chown -R tomcat: <scenarioo-docu-directory>
      sudo chmod ug+rws <scenarioo-docu-directory> 
      ```
    * The config.xml file will be created for you automatically in this directory by the Scenarioo Viewer Web Application when you do some configuration changes inside the webapplication for the first time and save it.
    
3. **Deploy the WAR** file into it, e.g. simply copy the WAR file into the webapps folder in Tomcat. 
    * If you use a release candidate WAR file that has the version in the name, then make sure to rename the war to simply `scenarioo.war` before deploying it, because the name defines the context under which you can access the app.

4. **Start the Tomcat Server** (or whatever java server you are using)

5. **Startup Scenarioo Viewer Web App** through your browser using following URL: http://{your-ip-address-and-port-or-host-url}/scenarioo (e.g. http://localhost:8080/scenarioo )

6. **Configure your Viewer and advanced features (optional)** as described in [Viewer Configuration](Configuration.md).
