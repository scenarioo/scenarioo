# Demo Server Setup

This folder contains documentation about how our Demo server is setup and configured.

The Demo Server is hosted by OVH (http://SoYouStart.com) and the required software is installed directly using Ansible and Vagrant.

## Setting up the Demo Server Host
So You Start offers dedicated servers for a monthly or yearly fee. The server can be created using a web interface.
The configuration is described in [OVH Server Setup](OVH-Server-Setup.md).

## How is the Demo Server deployed and updated?
Every time a pull request is merged or a commit is pushed to the master branch of scenarioo-infrastructure a Circle CI, job is started, which triggers a deployment to the demo server.

Ansible checks for every configured step if it has changed in the configuration, and if it has changed, the step is executed. E.g. if only a deployed branch was changed, then the software components installation steps are not executed.

A full deployment with installation of all components takes about 15 minutes on Circle CI, a deployment where only branches have changed takes about 5 minutes.

## What is installed on the Demo Server?
All the tools and packages that will be installed can be found in the respective Ansible playbooks in the scenarioo-infrastructure repo:

* Docker (with various dependent packages) & ElasticSearch [see scenarioo-infrastructure\roles\docker\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/docker/tasks/main.yml)
* Tomcat [scenarioo-infrastructure\roles\tomcat\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/tomcat/tasks/main.yml)
* [Nginx](nginx.md) as a proxy [scenarioo-infrastructure\roles\nginx\tasks\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/nginx/tasks/main.yml)

## How do I get access to the Demo Server?
To access the Demo Server you need to have an SSH-Key Pair and your user and public key needs to be registered in Scenarioo-Infrastructure.

### Creating the SSH key
Use PuttyGen (or another tool) to create an SSH key. 

Copy the value from the box "Public key for pasting into Open SSH authorized_keys file:" into a file with this name: `username.pem`.

Save the private key as *.ppk file as well (in PuttyGen: Button "Save private key"), so that you can use it to log in to the server with Putty.

### Adding your user to Scenarioo-Infrastructure
* Add your username to `required_users` in [scenarioo-infrastructure\roles\manageUsers\vars\main.yml](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/manageUsers/vars/main.yml)
* Add the file `username.pem` to [scenarioo-infrastructure\roles\manageUsers\keys](https://github.com/scenarioo/scenarioo-infrastructure/tree/master/roles/manageUsers/keys)
* Create a Pull Request with your changes in scenarioo-infrastructure.

### Configuring PuTTY to connect to the Demo Server with your credentials
* Set the correct Host Name, which you can see in the variable `mainserver` in [scenarioo-infrastructure\hosts\hosts_demoserver_ovh](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/hosts/hosts_demoserver_ovh).
* Set the correct Auto-login username under Connection > Data
* Select the saved private key under Connection > SSH > Auth > Private key file for authentication

## Important commands on the Demo Server
Here is a small collection of the most useful commands you might need when logged in to the demo server.

### Accessing the Scenarioo Server Logfile
```Bash
less /var/lib/tomcat8/logs/catalina.out
tail -f /var/lib/tomcat8/logs/catalina.out
```

### Restarting the Tomcat Server running all Demos
```Bash
sudo systemctl restart tomcat8
```
