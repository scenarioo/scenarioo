#!/bin/bash
set -e

# Input values
# --docsDistributionFolder=master|develop|version

for i in "${@}"
do
    case ${i} in
        --docsDistributionFolder=*)
            export DOCS_FOLDER="${i#*=}"
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
echo "Publishing Gitbook Markdown Documentation for $DOCS_FOLDER"
pushd docs
npm install
npm run build
echo "Publishing Docs to http://www.scenarioo.org/docs/$DOCS_FOLDER ..."
npm run gh-pages -- --dist _book --repo https://$GIT_USERPASS@github.com/scenarioo/scenarioo.github.io.git --branch master --dest docs/$DOCS_FOLDER --message "Publish docs for $DOCS_FOLDER"
echo "Publishing done to http://www.scenarioo.org/docs/$DOCS_FOLDER"
popd
