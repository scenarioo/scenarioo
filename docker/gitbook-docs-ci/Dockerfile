#This docker image extends the circleci image with graphviz and java
#It is necessary to display puml images in the gitbook (scenarioo.org/docs)
FROM circleci/node:8-stretch

MAINTAINER Scenarioo
LABEL description="Scenarioo CircleCI image with graphviz"

RUN sudo apt-get install -y graphviz=2.38.0-17 -V \
    && sudo apt-get install -y openjdk-8-jdk
