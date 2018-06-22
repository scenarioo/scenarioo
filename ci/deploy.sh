#!/bin/bash

# Input values
# --branch=BRANCH

for i in "${@}"
do
    case ${i} in
        --branch=*)
            BRANCH="${i#*=}"
            shift
        ;;

        *)
            # unknown option
        ;;
    esac
done

npm -v
node -v

# Properties
TOMCAT_WEBAPPS=/var/lib/tomcat7/webapps

# Workspace Directory - all paths used should be defined relative to this
WORKSPACE_DIR=$(pwd)
echo "Workspace Dir: $WORKSPACE_DIR"

# Scenarioo Docu Deployment Directories
SCENARIOO_DATA_ROOT=/var/lib/scenarioo/data
BRANCH_DATA_DIR=$SCENARIOO_DATA_ROOT/$BRANCH
BRANCH_DATA_ARCHIVE_DIR=/var/lib/scenarioo/data-archive/$BRANCH

###
### CLEANUP and PREPARATION
###

# Undeploy webapplication
echo "Undeploy Old Scenarioo Web App"
curl -u $TOMCAT_USERPASS http://localhost:8080/manager/text/undeploy\?path\=/scenarioo-$BRANCH

# Backup all self docu and other important example docu reports to be restored after successful deployment and testing of this scenarioo instance
mkdir -p $BRANCH_DATA_ARCHIVE_DIR
cp -rfp $BRANCH_DATA_DIR/scenarioo-* $BRANCH_DATA_ARCHIVE_DIR
cp -rfp $BRANCH_DATA_DIR/pizza-* $BRANCH_DATA_ARCHIVE_DIR
# wikipedia example not saved for restoring, this one was generated fresh by the scenarioo build

# Cleanup (TODO: should be avoided, better remove the whole build folder once, and everything should be put into that build folder!)
echo "Clean Old Self Docu Data"
rm -rf ./scenarioo-client/scenariooDocumentation/scenarioo_self_docu
echo "Clean Old E2E Test Reports"
rm -rf ./scenarioo-client/test-reports

# Cleanup documentation data that is deployed to the demo server
echo "Clean Old Demo Docu Data"
rm -rf $BRANCH_DATA_DIR

# Copy scenarioo wikipedia docu example data with new generated data (including config.xml for demo).
echo "Deploying Demo Docu Data (regenerated)"
cp -rf scenarioo-docu-generation-example/build/scenarioDocuExample $BRANCH_DATA_DIR

# Delete example data to save space
rm -rf scenarioo-docu-generation-example/build/scenarioDocuExample

# Configure Scenarioo Deployment Context: Location of Scenarioo Data Directory for this branch
echo "Configure Scemarioo Deployment Context (data dir for this branch)"
echo "<Context><Parameter name=\"scenariooDataDirectory\" value=\"$BRANCH_DATA_DIR\" override=\"true\" description=\"Location of scenarioo config.xml file\"/></Context>" > "/etc/tomcat7/Catalina/localhost/scenarioo-$BRANCH.xml"

###
### DEPLOYMENT
###

# Deploy the application manually, because autoDeploy is set to "false"
echo "Deploying the Scenarioo Web App to Tomcat"
cp -f $WORKSPACE_DIR/scenarioo-server/build/libs/scenarioo-latest.war $TOMCAT_WEBAPPS/scenarioo-$BRANCH.war
curl -f -u $TOMCAT_USERPASS http://localhost:8080/manager/text/deploy\?path\=/scenarioo-$BRANCH

# Wait until tomcat deployment is done
echo "Waiting for Scenarioo Viewer deployed on Tomcat ..."
for i in `seq 1 20`
do
   if [ -f $TOMCAT_WEBAPPS/scenarioo-$BRANCH/WEB-INF/classes/config.xml ]; then
      echo "Tomcat Deployment Done"
      break
   fi
   if [ "$i" -ge 20 ]; then
      echo "ERROR: Timeout on waiting for tomcat deployment!"
      exit 2
   fi
   sleep 3
done

# Wait until Java web app is available
echo "Waiting for Scenarioo Viewer started up ..."
for i in `seq 1 20`
do
   if $(curl --output /dev/null --silent --head --fail http://localhost:8080/scenarioo-$BRANCH/rest/branches); then
      echo "Scenarioo Viewer started up"
      break
   fi
   if [ "$i" -ge 20 ]; then
      echo "ERROR: Timeout on waiting for Scenarioo Viewer Web App starting up."
      exit 2
   fi
   sleep 3
done

# Some extra time for avoiding timeouts later (TODO: we have to wait for all comparisons to finish here!)
sleep 120
