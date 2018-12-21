#!/bin/bash

# To keep data of multiple builds we copy the current build data into the
# mounted scenarioo data directory. This way we complement already existing data
# with the lastest test run results bundled with this image.
echo "Copy E2E scenarioo data..."
mkdir -p /scenarioo/data
cp -pR /scenarioo/currentBuildData/* /scenarioo/data

# Start tomcat
echo "DONE - Start tomcat"
$CATALINA_HOME/bin/catalina.sh run
