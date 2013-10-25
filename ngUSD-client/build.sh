#!/bin/sh
BASEDIR=$(dirname $0)
cd $BASEDIR

# node JS path config (usually only needed on build server)
if [ "$#" -gt 0 ]; then
    echo "Set path for nodeJS as configured: $1"
    export PATH=$PATH:$1
fi

# grunt installation (usually only needed on build server, or when grunt somehow not available yet on your machine)"
if [ "$#" -gt 1 ]; then
    if [ "$2" == "installGrunt" ]; then

        echo "Was started with option 'installGrunt', therefore grunt will be installed for building."
        npm install -g grunt-cli bower

        echo "Set path for installed grunt to ./node_modules/grunt-cli/bin/"
        export PATH=$PATH:./node_modules/grunt-cli/bin/

        echo "Set path for installed bower to ./node_modules/grunt-cli/bin/"
        export PATH=$PATH:./node_modules/bower/bin/

    fi
fi


# install all other needed dependencies
npm install
bower install

# let grunt build everything
# TODO remove --force, as soon as all problems are resolved
grunt build --force
