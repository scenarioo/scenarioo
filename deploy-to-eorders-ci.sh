#!/bin/bash
gradle clean war && scp ngUSD-server/build/libs/ngusd.war eorders-ci:/var/lib/tomcat7/webapps/
