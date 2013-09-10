#!/bin/sh
# run this script with sudo and restart the vm afterwards

unzip update_2013_09_09_001_gradle-1.7-bin.zip -d /opt/gradle

echo '==================================='
echo ''
echo 'ATTENTION: You need to perform some manual steps:'
echo '- Add /opt/gradle/gradle-1.7/bin to the PATH variable in /etc/environment (reboot required to make this effective systemwide)'
echo '- Add the Gradle Plugin to your eclipse installation according to https://github.com/spring-projects/eclipse-integration-gradle/blob/master/README.md#installing-gradle-tooling-from-update-site'
echo ''
echo '==================================='
