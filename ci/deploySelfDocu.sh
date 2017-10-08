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

# Properties
SCENARIOO_DATA_ROOT=/var/lib/scenarioo
PIPELINE_BRANCH_DIR=/var/lib/jenkins/jobs/scenarioo-ci-pipeline/branches/$BRANCH

# just to prevent errors because of missing archive directories
mkdir -p $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive/$BRANCH/scenarioo-self-docu
mkdir -p $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive-most-recent/$BRANCH/scenarioo-self-docu

# First copy all archived self docu builds back
echo "Restoring archived self docu builds"
cp -r $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive/$BRANCH/scenarioo-self-docu $SCENARIOO_DATA_ROOT/scenarioDocuExample-$BRANCH
cp -r $SCENARIOO_DATA_ROOT/scenarioo-self-docu-archive-most-recent/$BRANCH/scenarioo-self-docu $SCENARIOO_DATA_ROOT/scenarioDocuExample-$BRANCH

#Simply copy the generated documentation data to the scenarioo docu dir
echo "Deploy the generated self docu build"
cd $PIPELINE_BRANCH_DIR/workspace/scenarioo-client
cp -rf scenariooDocumentation/scenarioo-self-docu $SCENARIOO_DATA_ROOT/scenarioDocuExample-$BRANCH
rm -rf scenariooDocumentation/scenarioo-self-docu

# Trigger scenarioo demo to import new builds
curl http://localhost:8080/scenarioo-$BRANCH/rest/builds/updateAndImport
