#!/bin/bash
# Sample script on how to automatically cleanup branches form scenarioo documentation data when they have been deleted in git

# Script configuration:
DIR_LOCAL_REPO=/put-absolute-path-to-your-local-cloned-repo-here
SCENARIOO_DATA_ROOT=/put-absolute-path-to-your-scenarioo-data-root-dir-here

# Detetc all branches currently available in GIT
cd $DIR_LOCAL_REPO
git fetch -p
if (( $? )); then
  echo "Failure: git repo not available." >&2
  exit 1
fi
AVAILABLE_BRANCHES=$(git branch -r | awk '{print $1}' | sed 's/origin\///g' | sed 's/\//_/g')

# Delete all branch directories from file system that are not inside AVAILABLE_BRANCHES anymore
for BRANCH_DIR in $(find $SCENARIOO_DATA_ROOT/* -maxdepth 0 -type d) ; do
    if [ -f "$BRANCH_DIR/branch.xml" ]
    then
        BRANCH_NAME=$(basename $BRANCH_DIR)
        if echo "$AVAILABLE_BRANCHES" | grep -Fxq "$BRANCH_NAME"
        then
            echo "branch $BRANCH_NAME still exists - keeping"
        else
            echo "branch $BRANCH_NAME not exists anymore - removing"
            rm -rf $BRANCH_DIR
        fi
    fi
done
