#!/bin/sh
BASEDIR=$(dirname $0)
cd $BASEDIR

echo $PATH
pwd

# TODO remove --force, as soon as all problems are resolved
grunt build --force
