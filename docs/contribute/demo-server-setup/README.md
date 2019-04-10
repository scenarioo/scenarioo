# Demo Server Setup

This folder contains documentation about how our Demo server is setup and configured.

The Demo Server is hosted by OVH (http://SoYouStart.com) and the required software is installed directly using Ansible and Vagrant.

## Setting up the Demo Server Host
So You Start offers dedicated servers for a monthly or yearly fee. The server can be created using a web interface.
The configuration is described in [OVH Server Setup](OVH-Server-Setup.md).

## How is the Demo Server deployed and updated?
Every time a pull request is merged onto, or a commit happens to, the master branch of scenarioo-infrastructure a Circle CI job is started which triggers a deployment to the demo server.

Ansible checks for every configured step if it has changed in the configuration, and if it has changed, the step is executed. E.g. if only a deployed branch was changed, then the software components installation steps are not executed.

A full deployment with installation of all components takes about 15 Minutes on Circle CI, a deployment where only branches have changed takes about 5 Minutes.

## What is installed on the Demo Server?
All the tools and packages that will be installed can be found in the scenarioo-infrastructure repo 
* Docker (with various dependent packages) & ElasticSearch [see scenarioo-infrastructure\roles\docker\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/docker/tasks/main.yml)
* Tomcat [scenarioo-infrastructure\roles\tomcat\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/tomcat/tasks/main.yml)
* [Nginx](nginx.md) as a proxy [scenarioo-infrastructure\roles\nginx\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/nginx/tasks/main.yml)
