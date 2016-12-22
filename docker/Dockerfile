FROM tomcat:8.5.9-jre8

MAINTAINER Scenarioo

# INSTALL GRAPHICS MAGICK
# RUN add-apt-repository ppa:dhor/myway
RUN apt-get update -y
RUN apt-get install -y graphicsmagick

# REPLACE THE ORIGINAL CONTEXT.XML WITH THE MODIFIED VERSION TO OVERRIDE THE CONFIG FILE PATH
COPY ./context.xml /usr/local/tomcat/conf/

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo.war /usr/local/tomcat/webapps/

# UNZIP WAR MANUALLY
RUN unzip -o /usr/local/tomcat/webapps/scenarioo.war -d /usr/local/tomcat/webapps/scenarioo/

# CHANGE PATH TO DOCUMENTATION FILES IN THE DEFAULT CONFIG FILE
RUN sed -i "s/<testDocumentationDirPath>\/home\/scenarioo\/scenarioo\/scenarioo-docu-generation-example\/build\/scenarioDocuExample<\/testDocumentationDirPath>/<testDocumentationDirPath>\/docu<\/testDocumentationDirPath>/" /usr/local/tomcat/webapps/scenarioo/WEB-INF/classes/config.xml

# REMOVE WAR
RUN rm /usr/local/tomcat/webapps/scenarioo.war

# RUN TOMCAT
CMD ["catalina.sh", "run"]
