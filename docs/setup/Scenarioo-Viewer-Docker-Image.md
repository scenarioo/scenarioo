Scenarioo provides a Docker Image to run the Scenarioo Viewer web application on your local machine, on a server or somewhere in the cloud. 

The following page describes, how to install and run this docker image (it is more or less a one liner! :smile: ).

## Prerequisites

To run the Scenarioo docker image it is necessary to install Docker. Visit the [docker installation page](https://docs.docker.com/installation/) for installation guides. 

## Run Scenarioo Docker Image

You want to set up the scenarioo infrastructure and run the Scenarioo webapplication? It is a one-liner:

1. Run `sudo docker run -it --rm --name scenarioo -p 8080:8080 -v $(pwd)/scenarioDocuExample:/doku scenarioo/webapp:2.2.0`  
with following parameters:    
`--name` - defines a name for the container (= running image)  
`-p` - maps a port of your host OS to the container internal port.  
`-v` - defines where the Sceanrioo-Docu-Directory is located (watch out: if you use $(pwd) you have to run the command in a specific directory)  

2. Access [http://localhost:8080/scenarioo/](http://localhost:8080/scenarioo/) in your webbrowser to verify the running webclient.

## More informations

The scenarioo docker image is hosted here on [dockerhub](https://hub.docker.com/u/scenarioo/).

More documentation about Docker can be found [here](https://docs.docker.com/).
