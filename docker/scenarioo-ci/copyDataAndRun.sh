#!/bin/bash

# To keep data of multiple builds we copy the current build data into the
# mounted scenarioo data directory. This way we complement already existing data
# with the lastest test run results bundled with this image.
cp -pR /scenarioo/currentBuildData /scenarioo/data

# Start tomcat
$CATALINA_HOME/bin/catalina.sh run
