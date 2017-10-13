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
export PROTRACTOR_BASE_URL=http://localhost:8080/scenarioo-$BRANCH
export DISPLAY=:99

pushd scenarioo-client
gulp webdriver_update
gulp test-e2e-scenarioo
popd
