#!/bin/bash

# Input values
# --branch=BRANCH

for i in "${@}"
do
    case ${i} in
        --branch=*)
            BRANCH="${i#*=}"
            shift
        ;;

        *)
            # unknown option
        ;;
    esac
done

# Workspace Directory - all paths used should be defined relative to this
WORKSPACE_DIR=$(pwd)
echo "Workspace Dir: $WORKSPACE_DIR"

# Properties
SCENARIOO_DATA_ROOT=/var/lib/scenarioo
SCENARIOO_DATA_DIR=/var/lib/scenarioo/scenarioDocuExample-$BRANCH

# Precreate archive directories (just to prevent errors because of missing archive directories)
mkdir -p $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive/scenarioo-$BRANCH
mkdir -p $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive-most-recent/scenarioo-$BRANCH

# First copy all archived self docu builds back
echo "Restoring archived self docu builds"
cp -r $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive/* $SCENARIOO_DATA_DIR
cp -r $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive-most-recent/* $SCENARIOO_DATA_DIR

# Deploy generated self docu of this build run
echo "Deploy the generated self docu build"
cp -rf ./scenarioo-client/scenariooDocumentation/* $SCENARIOO_DATA_DIR
rm -rf ./scenarioo-client/scenariooDocumentation

# Trigger scenarioo demo to import new builds
curl http://localhost:8080/scenarioo-$BRANCH/rest/builds/updateAndImport
