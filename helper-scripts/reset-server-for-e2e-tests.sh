#!/bin/sh
#
# This script ensures to reset your configuration of your scenarioo server 
# to the correct config that you should use for demo and e2e test purposes.
#
# Copy this script somwhere, and change the pathes to match your environment.
#
# Then for running e2e tests you should always do the following:
# 1. (optional, but to be bullet proof) do a clean build of scenarioo-docu-generation-example project to remove old outdated test data
# 2. run JUnit tests of scenarioo-docu-generation-example to recreate demo data 
# 3. run this script to configure your server properly with the newest version of the configuration
# 4. restart your Java backend server
#

# directory where you have the source code checked out (inside this directory the script expects the projects scenarioo-server and scenarioo-docu-generation-example)
source_code_directory=~/Code/scenarioo
source_code_directory_with_escaped_slashes=~\/Code\/scenarioo

# server config directory where your server is configured to read the config.xml from:
server_config_directory=~/.scenarioo

# the script code (no need to change!)
echo "Please check the directories are configured correctly ..."
echo "Source code directory: $source_code_directory"
echo "Server config directory: $server_config_directory"
cp -f "$source_code_directory/scenarioo-server/src/main/resources/config-for-demo/config.xml" "$server_config_directory/config.xml"
sed -i.bak "s/<testDocumentationDirPath>.*<\/testDocumentationDirPath>/<testDocumentationDirPath>$source_code_directory_with_escaped_slashes\/scenarioo-docu-generation-example\/build\/scenarioDocuExample<\/testDocumentationDirPath>/g" $server_config_directory/config.xml


