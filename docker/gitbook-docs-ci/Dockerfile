#This dockerfile extends the circleci image with graphviz and java
#It is necessary for the circleci publish_docs job so puml images are displayed in the scenarioo documentation (scenarioo.org/docs)
FROM cimg/node:15.3.0

MAINTAINER Scenarioo
LABEL description="Scenarioo CircleCI docker image with NodeJS, Java and graphviz for docu generation on our CI using gitbook tooling with plantuml plugin"

RUN sudo apt-get update -y \
    && sudo apt-get install -y graphviz \
    && sudo apt-get install -y openjdk-8-jdk
