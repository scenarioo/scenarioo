#!/bin/sh
BASEDIR=$(dirname $0)
cd $BASEDIR

# TODO remove --force, as soon as all problems are resolved
grunt build --force
