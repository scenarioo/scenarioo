#!/bin/bash
# Sample script on how to automatically delete build directories that are older than some days from specified branch directory
# but keeps all builds having a "keep.txt" file inside forever.
# this could be called in your build script to cleanup docu data after each new generated docu build
SCENARIOO_DATA_ROOT=/var/lib/scenarioo/docuDataDir
BRANCH_NAME=master
NUM_DAYS_TO_KEEP_BUILDS=21

for OLD_BUILD_DIR in $($SCENARIOO_DATA_ROOT/$BRANCH_NAME/* -maxdepth 0 -type d -ctime +$NUM_DAYS_TO_KEEP_BUILDS) ; do
    if [ -f "$OLD_BUILD_DIR/keep.txt" ]
    then
        echo "build $OLD_BUILD_DIR has keep.txt - keep forever"
    else
        echo "build $OLD_BUILD_DIR is too old - remove"
        rm -rf $OLD_BUILD_DIR
    fi
done
