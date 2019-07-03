# Scenarioo Viewer Docker Image

Scenarioo provides a Docker Image to run the Scenarioo Viewer web application on your local machine, on a server or somewhere in the cloud. 

The following page describes, how to install and run this docker image (it is more or less a one liner! :smile: ).

## Prerequisites

To run the Scenarioo docker image it is necessary to install Docker. Visit the [docker installation page](https://docs.docker.com/installation/) for installation guides. 

## Run Scenarioo Docker Image

How to set up and run the Scenarioo Viewer Web App as a docker image:

1. Simply run the following one liner command to install and run the docker image:
    ```
    sudo docker run -it --name scenarioo -p 8080:8080 -v /home/user/your-scenarioo-docu-folder:/scenarioo/data scenarioo/webapp:4.0.1
    ```
    with following parameters:    
    * `--name` - defines a name for the container (= running image)  
    * `-p` - maps a port of your host OS to the container's internal port.  
    * `-v` - maps the directory where your Scenarioo documentation data and config will be stored, simply replace `/home/user/your-scenarioo-docu-folder` by the absolute path to the folder where you want Scenarioo to read from and store in the documentation and config data. Make sure that the docker image wil have read and write access to this directory for storing its data (on linux use simply `sudo chmod ug+rws <scenarioo-docu-directory>` or see [Viewer Web App Setup](Scenarioo-Viewer-Web-Application-Setup.md) for more information in case of problems).  
    * **Remark for Backwards-Compatibility when migrating an existing docker setup (version 3.x or before) to the newest docker image version:** the new docker image will as well store its config file inside the configured data directory, this was not the case for earlier docker image versions out of the box. Therefore you might have to migrate your settings from config files inside your old docker containers to the new container, if you had any special settings configured before. In future this wont be an issue anymore, since the config file is now as well stored in your configured Scenarioo data directory.


2. Access [http://localhost:8080/scenarioo/](http://localhost:8080/scenarioo/) in your web browser to verify the Viewer application is running.

3. At this point, the Scenarioo Configuration is only in memory. Go to `Manage` -> `General Settings` (see upper right navigation in the Viewer) and at least add your application name and save the configuration. The configuration file `config.xml` will be saved to the directory you have configured as docu directory.

4. Start adding your documentation data to the mapped directory and let the Viewer application import it. More information on how to publish documentation data can be found in [How to Publish Documentation Data](Publish-Documentation-Data.md)

5. You can configure advanced settings of the Scenarioo Viewer as explained in [Viewer Configuration](Configuration.md) by changing the stored `config.xml` inside the mapped directory.

## More Information

* [DockerHub](https://hub.docker.com/u/scenarioo/) for the official Scenarioo docker image that we provide.

* [Docker](https://docs.docker.com/) for more information and documentation about docker.

* [Viewer Web App Setup Instructions](Scenarioo-Viewer-Web-Application-Setup.md), to refer to in case of troubles with the web app setup inside the docker image.

* [Viewer Configuration](Configuration.md) for more advanced configuraiton options.

* [Publish Documentation Data](Publish-Documentation-Data.md) for how to publish documentation data to the Scenarioo Viewer Web App.
