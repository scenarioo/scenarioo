#!/bin/bash
# Sample script on how to automatically delete build directories that are older than some days from specified branch directory
# this could be called in your build script to cleanup docu data after each new generated docu build
SCENARIOO_DATA_ROOT=/var/lib/scenarioo/docuDataDir
BRANCH_NAME=master
NUM_DAYS_TO_KEEP_BUILDS=21
find $SCENARIOO_DATA_ROOT/$BRANCH_NAME/* -maxdepth 0 -type d -ctime +$NUM_DAYS_TO_KEEP_BUILDS_IN_MASTER -exec rm -rf {} \;
