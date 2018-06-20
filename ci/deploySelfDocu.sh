#!/bin/bash
set -e

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

# Properties / Settings
SCENARIOO_HOST_URL=http://localhost:8080
SCENARIOO_DATA_ROOT=/var/lib/scenarioo/data
SCENARIOO_DATA_BRANCH_DIR=$SCENARIOO_DATA_ROOT/$BRANCH
SCENARIOO_DATA_BRANCH_ARCHIVE_DIR=/var/lib/scenarioo/data-archive/$BRANCH

# Deploy self docu to develop demo (and for important branches only also to master demo):
SCENARIOO_DATA_SELF_DOCU_DIR=$SCENARIOO_DATA_ROOT/develop
SCENARIOO_DATA_SELF_DOCU_MASTER_DIR=$SCENARIOO_DATA_ROOT/master

# Restore all archived documentation data to be reatained for this branch (was archived in deploy.sh before deploying)
# (this is needed to not loose all self docu builds on develop deployment or pizza-delivery and other examples)
# [additional remark: the `|| true` is needed here to not let it fail if there was no archived data to be copied (e.g. for new feature branches without archived builds)]
echo "Restoring Documentation Data: Self Docu and Examples"
cp -rp $SCENARIOO_DATA_BRANCH_ARCHIVE_DIR/* $SCENARIOO_DATA_BRANCH_DIR || true
rm -rf $SCENARIOO_DATA_BRANCH_ARCHIVE_DIR || true

# Deploy generated self docu of this build run
echo "Deploy the generated self docu build to $SCENARIOO_DATA_SELF_DOCU_DIR"
cp -rf ./scenarioo-client/scenariooDocumentation/* $SCENARIOO_DATA_SELF_DOCU_DIR
if [ $BRANCH = "master" ] || [ $BRANCH = "develop" ]
then
    echo "Deploy the generated self docu build to $SCENARIOO_DATA_SELF_DOCU_MASTER_DIR"
    cp -rf ./scenarioo-client/scenariooDocumentation/* $SCENARIOO_DATA_SELF_DOCU_MASTER_DIR
fi
rm -rf ./scenarioo-client/scenariooDocumentation

# Do the cleanup of all directories and all data and deployments
# (done as one of the last steps, since all data is now in its place where it can be cleaned up)
./ci/cleanup.sh

# Trigger relevant scenarioo demos to update and import new builds
# (master and develop are always updated to also reflect potentialy removed self docu builds or deployed self docus during cleanup phase)
curl $SCENARIOO_HOST_URL/scenarioo-master/rest/builds/updateAndImport
curl $SCENARIOO_HOST_URL/scenarioo-develop/rest/builds/updateAndImport
curl $SCENARIOO_HOST_URL/scenarioo-$BRANCH/rest/builds/updateAndImport
