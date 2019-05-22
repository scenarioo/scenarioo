FROM openjdk:8-jre

MAINTAINER Scenarioo
LABEL description="Scenarioo Viewer Web Application"

# SET THE SCENARIOO DATA DIRECTORY VIA THE ENVIRONMENT VARIABLE
ENV SCENARIOO_DATA /scenarioo/data

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo-latest.war /scenarioo/bin/scenarioo.war

# RUN TOMCAT
CMD java -Xms400m -Xmx800m -jar /scenarioo/bin/scenarioo.war
