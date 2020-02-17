#This dockerfile extends the circleci image with graphviz and java
#It is necessary for the circleci publish_docs job so puml images are displayed in the scenarioo documentation (scenarioo.org/docs)
FROM circleci/node:8-stretch

MAINTAINER Scenarioo
LABEL description="Scenarioo CircleCI docker image with graphviz"

RUN sudo apt-get install -y graphviz=2.38.0-17 -V \
    && sudo apt-get install -y openjdk-8-jdk
