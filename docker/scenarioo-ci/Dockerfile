FROM tomcat:8.5.9-jre8

MAINTAINER Scenarioo
LABEL description="Scenarioo Viewer - Build for each branch"

# SET THE SCENARIOO DATA DIRECTORY VIA THE ENVIRONMENT VARIABLE
ENV SCENARIOO_DATA /scenarioo/data

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo.war /usr/local/tomcat/webapps/
# ADD E2E RESULT TO /scenarioo/data
ADD ./scenarioDocuExample /scenarioo/data

# RUN TOMCAT
CMD ["catalina.sh", "run"]
