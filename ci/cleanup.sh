#!/bin/bash
set -e

# Script to Cleanup deployments and data of removed branches
# also triggers removal of outdated self docu data builds for existing branches

echo "Cleanup Deployments and Scenarioo Data ..."

WORKSPACE_DIR=$(pwd)
echo "Workspace Dir: $WORKSPACE_DIR"

# Remove orphaned deployments and data of branches not existing anymore
SCENARIOO_DATA_ROOT=/var/lib/scenarioo/data
SCENARIOO_ARCHIVE_DATA_ROOT=/var/lib/scenarioo/archive-data

git fetch -p
if (( $? )); then
  echo "Failure: git repo not available." >&2
  exit 1
fi
AVAILABLE_BRANCHES=$(git branch -r | awk '{print $1}' | sed 's/origin\///g' | sed 's/\//\-/g' | sed 's/\#//g' )
echo "Available Branches:"
echo "$AVAILABLE_BRANCHES"
echo "$AVAILABLE_BRANCHES" | grep -Fxq "develop" || {
    echo "ERROR on cleanup: develop branch not found in branches, stopping here and doing no cleanup";
    exit -1;
}
echo "$AVAILABLE_BRANCHES" | grep -Fxq "master" || {
    echo "ERROR on cleanup: master branch not found in branches, stopping here and doing no cleanup";
    exit -1;
}

for BRANCH_DIR in $(find $SCENARIOO_DATA_ROOT/* -maxdepth 0 -type d) ; do
    BRANCH_NAME=$(basename $BRANCH_DIR)
    if echo "$AVAILABLE_BRANCHES" | grep -Fxq "$BRANCH_NAME"
    then
        echo "branch $BRANCH_NAME still exists - keep"
        # check for contained branches to clean outdated builds ...
        for BRANCH_DOCU_DIR in $(find $BRANCH_DIR/* -maxdepth 0 -type d) ; do
            BRANCH_DOCU_NAME=$(basename $BRANCH_DOCU_DIR)
            # Only example data `wikipedia-` docu branch folders are never cleaned at all!
            # (also the application data directory should better not be cleaned up!)
            if [[ -f "$BRANCH_DOCU_DIR/branch.xml" ]] && [[ $BRANCH_DOCU_NAME != *"wikipedia"* ]]; then
                echo " but cleanup published scnearioo docu builds for $BRANCH_DOCU_NAME ..."
                ./ci/cleanupOutdatedScenariooBuilds.sh --dir=$BRANCH_DOCU_DIR
            fi
        done
    else
        echo "branch $BRANCH_NAME not exists anymore - removing"
        echo "   remove branch's scenarioo data directory"
        rm -rf $BRANCH_DIR
        echo "   remove branch's scenarioo archive-data directory"
        rm -rf $SCENARIOO_ARCHIVE_DATA_ROOT/$BRANCH_NAME
        echo "   remove branch's self docu data directory"
        rm -rf $SCENARIOO_DATA_ROOT/develop/scenarioo-$BRANCH_NAME
        echo "   undeploy branch's scenarioo web app instance"
        curl -u $TOMCAT_USERPASS http://localhost:8080/manager/text/undeploy\?path\=/scenarioo-$BRANCH_NAME
        rm -f /var/lib/tomcat8/webapps/scenarioo-$BRANCH_NAME.war
        rm -rf /var/lib/tomcat8/webapps/scenarioo-$BRANCH_NAME
    fi
done
