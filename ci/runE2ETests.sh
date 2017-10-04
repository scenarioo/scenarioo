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


###
### Run e2e Tests (with self docu generation)
###
echo "Executing E2E tests"
export PROTRACTOR_BASE_URL=http://demo.scenarioo.org/scenarioo-$BRANCH
export DISPLAY=:99
PIPELINE_BRANCH_DIR=/var/lib/jenkins/jobs/scenarioo-ci-pipeline/branches/$BRANCH
cd $PIPELINE_BRANCH_DIR/workspace/scenarioo-client
#./node_modules/gulp-protractor/node_modules/protractor/bin/webdriver-manager update
gulp webdriver_update
gulp test-e2e-scenarioo
