#!/bin/bash
set -e

# Input values
# --branch=BRANCH

for i in "${@}"
do
    case ${i} in
        --branch=*)
            export BRANCH="${i#*=}"
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
npm run test-e2e-scenarioo
popd

# Remove sketcher data generated from e2e tests
rm -rf /var/lib/scenarioo/data/$BRANCH/scenarioo-application-data/sketcher
