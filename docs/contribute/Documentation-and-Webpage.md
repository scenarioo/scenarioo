# Scenarioo Webpage and Documentation


## Web Page

The scnearioo.org webpage is maintained in repository 
https://github.com/scenarioo/scenarioo.github.io

The web page can be changed in that repo by simply do a PR to master.

See README.md in that repo for further detailed information.

Changes for further releases should not yet been made on master but prepared on special release branches, to be merged on the date of release.

## Documentation

We do not use Wikis!

All our repos have a docs folder with a markdown file documentation using gitbook tooling.

The sources for the major scenarioo documentation is in major scenarioo repository here:
https://github.com/scenarioo/scenarioo/tree/develop/docs

This docu is deployed as a nice browsable web page in different versions as follows:

* [Master Docu](http://scenarioo.org/docs/master/): This is the docu of the last official released version.
* [Development Docu](http://scenarioo.org/docs/develop/): This is the docu of the last official released version.
* [Specific Version Docu  - e.g. for Version 3.0](http://scenarioo.org/docs/3.0/): For every major release we keep one archived published version of the docu. Links in our product usually point to that version of the docu that is currently installed. If we fix / improve docu of latest release we have to publish it to the latest archived version and as well into the master documentation.

###  How to Publish Documentation

#### Automatic Publishing

Easiest way is to use the automation on Jenkins which currently automatically publishes the docu as follows:

* `Develop Docu`: Automatically updated/published on every push to develop branch.

#### Manual Publishing 

Since the automation does not yet automatically deploy all our versions of the docu, here is the process to follow to publish one version manually.

* Make sure the version of scenarioo/scenarioo you want to release docu for is checked out (usually `develop`, `master` or a release branch).

* If you publish documentation for a new release version, do the following, otherwise skip to the next step.
  * Add configuration for this new version to configuration in https://github.com/scenarioo/scenarioo/blob/develop/docs/book.json
  * In scenarioo.github.io web page repository: Create a new subfolder in folder `docs` for that new version

* Make sure the book.json has the correct version you are going to release selected in the configuration (`"selected": true`)

* Run `npm install` and `npm run build` in `docs` folder.

* Copy the generated content to the scenarioo.github.io repository:
    * Select all content of `docs/_book` to copy (without the .gitignore file)
    * Copy to scenarioo.github.io repo into the correct sub folder of `docs` for the version you want to deploy
    * Make sure the target folder is cleaned first, if there was already an old version of the docu deployed for that same version, before you copy.
    * If this is the docu of the current official released version, please publish both to its concrete version directory as well as to folder `master`.
    
* Commit and push the added / updated documentation versions to master branch on scenarioo.github.io

* When done: 
    * make sure that you changes to scenarioo/scenarioo are merged to develop 
    * on develop verify in `book.json` that the Developer version is configured as selected again!
