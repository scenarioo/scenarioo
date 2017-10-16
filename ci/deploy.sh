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

        --user=*)
            SCENARIOO_USER="${i#*=}"
            shift
        ;;

        --secret=*)
            SCENARIOO_SECRET="${i#*=}"
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

# Scenarioo directories
SCENARIOO_DATA_ROOT=/var/lib/scenarioo
BRANCH_DATA_DIR=$SCENARIOO_DATA_ROOT/scenarioDocuExample-$BRANCH
CONFIG_XML=$BRANCH_DATA_DIR/config.xml

# Jenkins directories
PIPELINE_BRANCH_DIR=/var/lib/jenkins/jobs/scenarioo-ci-pipeline/branches/$BRANCH
WORKSPACE_DIR=$(pwd)

echo "Workspace Dir: $WORKSPACE_DIR"

###
### CLEANUP and PREPARATION
###

# Undeploy webapplication
echo "Undeploy webapplication"
curl -u $SCENARIOO_USER:$SCENARIOO_SECRET http://localhost:8080/manager/text/undeploy\?path\=/scenarioo-$BRANCH

# Cleanup
echo "Cleanup built self docu data"
rm -rf $PIPELINE_BRANCH_DIR/workspace/scenarioo-client/scenariooDocumentation/scenarioo_self_docu
echo "Cleanup old test report data"
rm -rf $PIPELINE_BRANCH_DIR/workspace/scenarioo-client/test-reports

# Cleanup documentation data that is deployed to the demo server
echo "Cleanup old documentation data from demo server"
rm -rf $BRANCH_DATA_DIR

# Copy scenarioo wikipedia docu example data with new generated data.
echo "Deploying 'scenarioo-docu-generator-example' documentation data (regenerated)"
cp -rf $WORKSPACE_DIR/scenarioo-docu-generation-example/build/scenarioDocuExample $BRANCH_DATA_DIR

# Create config.xml with correct path to docu data
echo "Creating config.xml for deployment"
unzip -p $WORKSPACE_DIR/scenarioo-server/build/libs/scenarioo-latest.war WEB-INF/classes/config-for-demo/config.xml > $CONFIG_XML
sed -i.bak "s/<applicationName>.*<\/applicationName>/<applicationName>$BRANCH branch<\/applicationName>/g" $CONFIG_XML
# following line is only for debuging purpose:
#more $CONFIG_XML

# Write context file with path to config.xml file.
echo "Creating context deployment descriptor"
echo "<Context><Parameter name=\"scenariooDataDirectory\" value=\"$BRANCH_DATA_DIR\" override=\"true\" description=\"Location of scenarioo config.xml file\"/></Context>" > "/etc/tomcat7/Catalina/localhost/scenarioo-$BRANCH.xml"
#following line is only for debuging purpose:
#more /etc/tomcat7/Catalina/localhost/scenarioo-$BRANCH.xml


###
### DEPLOYMENT
###

# Deploy the application manually, because autoDeploy is set to "false"
echo "Deploying the Scenarioo Web App"
cp -f $WORKSPACE_DIR/scenarioo-server/build/libs/scenarioo-latest.war $TOMCAT_WEBAPPS/scenarioo-$BRANCH.war
curl -u $SCENARIOO_USER:$SCENARIOO_SECRET http://localhost:8080/manager/text/deploy\?path\=/scenarioo-$BRANCH


# Wait until tomcat deployment is done
for i in `seq 1 20`
do
   if [ -f $TOMCAT_WEBAPPS/scenarioo-$BRANCH/WEB-INF/classes/config-for-demo/config.xml ]; then
      echo "Tomcat deployment available"
      break
   fi
   if [ "$i" -gt 20 ]; then
      echo "ERROR: Timeout on waiting for tomcat deployment!"
      exit 2
   fi
   sleep 3
done

# Wait until Java web app is available
echo "Waiting for Java web app to become available..."
for i in `seq 1 20`
do
   if $(curl --output /dev/null --silent --head --fail http://localhost:8080/scenarioo-$BRANCH/rest/branches); then
      echo "Java web app available"
      break
   fi
   if [ "$i" -gt 20 ]; then
      echo "Timeout on waiting for Java web app"
      exit 2
   fi
   sleep 3
done

# Some extra time for avoiding timeouts later
sleep 120
