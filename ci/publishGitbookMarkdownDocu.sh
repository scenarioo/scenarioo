#!/bin/bash

# Input values
# --npmTask=npmTaskToExecute

for i in "${@}"
do
    case ${i} in
        --npmTask=*)
            export NPM_TASK="${i#*=}"
            shift
        ;;

        *)
            # unknown option
        ;;
    esac
done

###
### Publish docu
###
echo "Publishing Gitbook Markdown Documentation $NPM_TASK"
pushd docs
npm install
npm run $NPM_TASK
popd

