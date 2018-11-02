FROM tomcat:8.5.9-jre8

MAINTAINER Scenarioo
LABEL description="Scenarioo Viewer - Build for each branch"

# SET THE SCENARIOO DATA DIRECTORY VIA THE ENVIRONMENT VARIABLE
ENV SCENARIOO_DATA /scenarioo/data

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo.war /usr/local/tomcat/webapps/

# ADD E2E RESULT TO /scenarioo/currentBuildData
# On startup this will be copied into the mounted /scenarioo/data directory
# This way we can merge the lastest results in this image with possibly
# already existing scenarioo runs present in /scenarioo/data.
ADD ./scenarioDocuExample /scenarioo/currentBuildData
ADD ./copyDataAndRun.sh /root/copyDataAndRun.sh

# RUN TOMCAT
CMD ["/root/copyDataAndRun.sh"]
