#!/bin/bash

# Clean up scenarioo documentation builds inside tha passed scenarioo branch directory:
# * Keep youngest build in any case
# * Keep up to 5 builds younger than 5 days
# * Keep all builds with a keep.txt file inside
# * remove all other builds (exceeding 5 builds or older than 5 days)

# Input values
# --dir=<full path of branch directory to clean>

for i in "${@}"
do
    case ${i} in
        --dir=*)
            BRANCH_DIRECTORY="${i#*=}"
            shift
        ;;

        *)
            # unknown option
        ;;
    esac
done

echo "  Cleaning scenarioo builds in directory $BRANCH_DIRECTORY"

# Keep maximum number of builds
# Caution: number must be one higher than the number of builds you want to keep
NUM_BUILDS_TO_KEEP_PLUS_ONE=6
for OLD_BUILD_DIR in $(find $BRANCH_DIRECTORY/* -maxdepth 0 -type d -printf '%T@ %p\n' | sort -k 1nr | sed 's/^[^ ]* //' | tail -n +$NUM_BUILDS_TO_KEEP_PLUS_ONE) ; do
    if [ -f "$OLD_BUILD_DIR/keep.txt" ]
    then
        echo "    build $OLD_BUILD_DIR has keep.txt - keep forever"
    else
        echo "    build $OLD_BUILD_DIR is old and there are too much builds - remove"
        rm -rf $OLD_BUILD_DIR
    fi
done

# Keep maximum number of even older builds
# Caution: number must be one higher than the number of builds you want to keep
NUM_BUILDS_TO_KEEP_PLUS_ONE=2
NUM_DAYS_TO_KEEP=5
for OLD_BUILD_DIR in $(find $BRANCH_DIRECTORY/* -maxdepth 0 -type d -ctime +$NUM_DAYS_TO_KEEP -printf '%T@ %p\n' | sort -k 1nr | sed 's/^[^ ]* //' | tail -n +$NUM_BUILDS_TO_KEEP_PLUS_ONE) ; do
    if [ -f "$OLD_BUILD_DIR/keep.txt" ]
    then
        echo "    build $OLD_BUILD_DIR is too old but has keep.txt - keep forever"
    else
        echo "    build $OLD_BUILD_DIR is too old - remove"
        rm -rf $OLD_BUILD_DIR
    fi
done
