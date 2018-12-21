FROM tomcat:8.5.9-jre8

MAINTAINER Scenarioo
LABEL description="Scenarioo Dev Tomcat Environment"

# SET THE SCENARIOO DATA DIRECTORY VIA THE ENVIRONMENT VARIABLE
ENV SCENARIOO_DATA /scenarioo/data
ENV JPDA_ADDRESS 1043
ENV JPDA_TRANSPORT dt_socket
ENV JPDA_SUSPEND n

# RUN TOMCAT
CMD ["catalina.sh", "jpda", "run"]
