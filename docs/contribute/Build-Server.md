# Scenarioo CI/CD Build and Demo Server

## CI/CD Build Server
Different build servers are used for the different projects (scenarioo, scenarioo-js and scenarioo-cs). An overview page with links to all three build servers can be accessed through
http://ci.scenarioo.org or http://build.scenarioo.org (both forward to http://demo.scenarioo.org/overview/)

## Demos

Latest release: http://demo.scenarioo.org

Development for future release: http://demo.scenarioo.org/scenarioo-develop

Your special feature or release branch (if it has a CI/CD-build-job): http://demo.scenarioo.org/scenarioo-{branch-name}

## Circle CI Pipeline

Whenever you push a new branch to the `scenarioo` repository it is automatically built.

## Setup of E2E Tests on Build Server

This image is a bit outdated, since we started using Circle CI Pipelines, but the basic procedure is still the same.

![End to End Tests Scenarioo Protractor](https://cloud.githubusercontent.com/assets/3780183/8078418/fe24f0b6-0f5d-11e5-87f2-1b738bc68d57.png)

## Demo Server Installation and Configuration

Further Details about all tools installed on the Demo Server can be found in [Demo Server Configuration](./demo-server-setup/README.md) and further sub pages per tool.
