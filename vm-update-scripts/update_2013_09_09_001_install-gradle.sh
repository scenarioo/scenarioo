#!/bin/sh
# run this script with sudo and restart the vm afterwards

sudo unzip update_2013_09_09_001_gradle-1.7-bin.zip -d /opt/gradle

echo '==================================='
echo ''
echo 'ATTENTION: You need to perform some manual steps:'
echo '- Install eclipse plugin "Gradle IDE" (only pick this one!) from following update site:'
echo '     http://dist.springsource.com/release/TOOLS/gradle'
echo '     (for unexperienced eclipse users, see detailed installation instructions under: https://github.com/spring-projects/eclipse-integration-gradle/blob/master/README.md#installing-gradle-tooling-from-update-site )'
echo '- Add /opt/gradle/gradle-1.7/bin to the PATH variable using following command and save the file:'
echo '     > sudo gedit /etc/environment &'
echo '- you have to reboot your vm for the path changes to take effect'
echo ''
echo '==================================='
