FROM tomcat:8.0
 
MAINTAINER Scenarioo

# ADD WAR TO TOMCAT-WEBAPPS
ADD ./scenarioo.war /usr/local/tomcat/webapps/

# UNZIP WAR MANUALLY
RUN unzip -o /usr/local/tomcat/webapps/scenarioo.war -d /usr/local/tomcat/webapps/scenarioo/

# CHANGE DOKU-PATH IN CONFIG
RUN sed -i "s/<testDocumentationDirPath>\/home\/scenarioo\/scenarioo\/scenarioo-docu-generation-example\/build\/scenarioDocuExample<\/testDocumentationDirPath>/<testDocumentationDirPath>\/doku<\/testDocumentationDirPath>/" /usr/local/tomcat/webapps/scenarioo/WEB-INF/classes/config.xml

# REMOVE WAR
RUN rm /usr/local/tomcat/webapps/scenarioo.war

# RUN TOMCAT
CMD ["catalina.sh", "run"]
