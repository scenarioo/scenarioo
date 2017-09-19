#!/bin/bash
###
### Run e2e Tests (with self docu generation)
###
echo "Executing E2E tests"
export PROTRACTOR_BASE_URL=http://demo.scenarioo.org/scenarioo-$BRANCH
export DISPLAY=:99
cd /var/lib/jenkins/jobs/scenarioo-$BRANCH/workspace/scenarioo-client
#./node_modules/gulp-protractor/node_modules/protractor/bin/webdriver-manager update
gulp webdriver_update
gulp test-e2e-scenarioo
