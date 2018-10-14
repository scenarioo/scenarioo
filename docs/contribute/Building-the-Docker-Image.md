# Scenarioo Docker Image

## Build it

Creating a Docker image means to create a ready-to-run-artifact. This includes setting up the infrastructure, installing the Scenarioo war file and configuring the webapp. This process can be made manually or can be registered as a task on your CI.

Prerequisite: You need to have docker installed on your machine. On Ubuntu this can be done by running `sudo apt-get install docker.io`.

1. You find the necessary Dockerfile in the directory [docker](https://github.com/scenarioo/scenarioo/tree/develop/docker/scenarioo).
2. Locate the scenarioo.war you want to deploy. For official docker image releases download the official released WAR from https://github.com/scenarioo/scenarioo/releases or use the output of the release branch jenkins build (scenario-<version>.war with exact correct version number! Please check!) 
3. Put the `scenarioo.war` next to the Dockerfile into same directory and rename the WAR to `scenarioo.war` without version number.
4. Open a bash and navigate to the directory which contains the files
5. Now run the following command:  
`sudo docker build -t scenarioo/webapp:x.y.z .`
where `x.y.z` stands for the Scenarioo version you are building the docker image for.
`build` is the docker command and the -t options allows to tag/name the created image (repository/artifact:tag). For more options view the docs.
6. Use `sudo docker images` to verify the existence of the created image
7. Now you are ready to run (see ["Run Scenarioo Docker Image"](../tutorial/Scenarioo-Viewer-Docker-Image#run-scenarioo-docker-image)) or push (see [Push image to Dockerhub](#Push-image-to-Dockerhub)) your image.

## Push image to Dockerhub
To make an image public available it is necessary to push it to the Dockerhub. As soon as an image is publicly available on Dockerhub it can be run from everywhere without even pulling it in advance. Just run it as described in ["Run Scenarioo Docker Image"](../tutorial/Scenarioo-Viewer-Docker-Image#run-scenarioo-docker-image).  
  
1. Open a bash and check that the docker image is listed on the working machine:  
`sudo docker images`
2. Run `sudo docker login` for authentication. You will be asked for the username (scenarioo) and the password.
3. Run the command: `sudo docker push scenarioo/webapp:x.y.z` where `x.y.z` stands for the Scenarioo version.
4. Check [Dockerhub](https://hub.docker.com/u/scenarioo/) to verify the image.

Check the docs for further actions like deleting, tagging and other commands on images.

## How to setup and use the Docker Image?

See [Docker Image Setup Instructions](../tutorial/Scenarioo-Viewer-Docker-Image.md)
