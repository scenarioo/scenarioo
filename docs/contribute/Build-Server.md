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

## Setup of Build Server

There are a number of aliases defined in the .bash_aliases file of the ubuntu user. They all start with `sc-` and lead to the Tomcat, Jenkins and the Scenarioo data directories.

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

## CI Tools Installation and Configuration

Further Details about all tools installed on the CI can be found in [CI Server & Tools Configuration](./ci-server-setup/README.md) and further sub pages per tool.
