# Scenarioo CI/CD Build and Demo Server

## Jenkins CI/CD Build Server

http://ci.scenarioo.org or http://build.scenarioo.org (both forward to http://54.88.202.24/jenkins)

The server is maintained by adiherzog. Contact him if you need a Jenkins account.

## Demos

Latest release: http://demo.scenarioo.org

Development for future release: http://demo.scenarioo.org/scenarioo-develop

Your special feature or release branch (if it has a CI/CD-build-job): http://demo.scenarioo.org/scenarioo-{branch-name}

## Jenkins Pipeline

Whenever you push a new branch to the `scenarioo` repository it is automatically built.

## Nginx Proxy Configurations

Modifying NGINX proxy configuration files to redirect URLs on our demo/build server, can be done as follows:

0. All the config files are found on the demo VM in following directory:
/etc/nginx/sites-available

1. Download/Upload config files
    * Use SCP to copy files
      * from server: `scp ubuntu@54.88.202.24:/etc/nginx/sites-available/* .`
      * to server: `scp ./* ubuntu@54.88.202.24:/home/ubuntu/upload/sites-available`
    * On windows use putty, pageant and pscp (all from putty)
      * register the key for the VM with pageant
      * then use pscp in same way as scp (see above)

3. SSH to VM and copy the files from uploaded place to /etc/nginx/sites-available
> cd ~/upload/sites-available
> sudo cp ./<changed-file> /etc/nginx/sites-available/<changed-file>

4. Only in case of a new config file: Generate link for new server files in `sites-enabled`:
> sudo ln -s /etc/nginx/sites-available/<name-of-new-server-file> /etc/nginx/sites-enabled/

5. Restart the NGINX Server
> sudo service nginx restart

## Setup of Build Server

There are a number of aliases defined in the .bash_aliases file of the ubuntu user. They all start with `sc-` and
lead to the Tomcat, Jenkins and the Scenarioo data directories.

### Restart Tomcat Manually

1. SSH to the demo server:
> ssh -i .ssh/Scenarioo-Keypair.pem ubuntu@54.88.202.24  
(you need the key for that! ask one of the Leads if you need it)

2. Stop and start tomcat:
> sudo service tomcat7 stop  
> sudo service tomcat7 start  

Alternatively just use the alias `sc-tomcat-restart`.

### Automatic Tomcat Restart Every Night

A cron job restarts Tomcat every night at 3am:

```
0 3 * * * service tomcat7 restart
```

List cron jobs: `sudo crontab -l`
Edit cron jobs of user: `sudo crontab -e`

### Regular Smoke Test

The Jenkins job `scenarioo-smoketest-demo` checks between 4am and 5am each day whether the `master` and `develop` branch
demos are still available. If that's not the case an e-mail notification is sent to `admin@scenarioo.org`.

## Setup of E2E Tests on Build Server

This image is a bit outdated, since we started using Jenkins Pipelines, but the basic procedure is still the same.

![End to End Tests Scenarioo Protractor](https://cloud.githubusercontent.com/assets/3780183/8078418/fe24f0b6-0f5d-11e5-87f2-1b738bc68d57.png)
