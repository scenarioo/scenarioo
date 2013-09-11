#!/bin/sh
BASEDIR=$(dirname $0)
cd $BASEDIR

if [ "$#" -gt 0 ]; then
    echo "Set path for nodeJS as configured: $1"
    export PATH=$PATH:$1
fi

if [ "$#" -gt 1 ]; then
    if [ "$1" -eq "installGrunt" ]; then
        echo "Was started with option 'installGrunt", therefore grunt will be installed for building."
        npm install grunt-cli
        #npm install less
        #npm install grunt
        #npm install grunt-cli
        #npm install --save-dev grunt-contrib-livereload

        echo "Set path for installed grunt to ./node_modules/grunt-cli/bin/"
        export PATH=$PATH:./node_modules/grunt-cli/bin/
    fi
fi


# install all other needed dependencies
npm install

# let grunt build everything
# TODO remove --force, as soon as all problems are resolved
grunt build --force
