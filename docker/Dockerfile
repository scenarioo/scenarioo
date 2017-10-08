FROM tomcat:8.5.9-jre8

MAINTAINER Scenarioo
LABEL description="A simple container running the Scenarioo viewer application on a Tomcat server."

# SET THE SCENARIOO DATA DIRECTORY VIA THE ENVIRONMENT VARIABLE
ENV SCENARIOO_DATA /doku

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo.war /usr/local/tomcat/webapps/

# RUN TOMCAT
CMD ["catalina.sh", "run"]
