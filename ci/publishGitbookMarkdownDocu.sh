#!/bin/bash

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
echo "Publishing Gitbook Markdown Documentation for $VERSION"
pushd docs
npm install
npm run build
npm run gh-pages -- --dist _book --repo https://$GIT_USERPASS@github.com/scenarioo/scenarioo.github.io.git --branch test --dest docs/$DOCS_FOLDER --message "Publish Docs for $DOCS_FOLDER"
popd
