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

The major scenarioo documentation is in major scenarioo repository here:
https://github.com/scenarioo/scenarioo/tree/develop/docs

###  How to Publish Documentation

* Make sure the version of scenarioo/scenarioo you want to release docu for is checked out (usually `develop`, `master` or a release branch).

* Is it docu for a new release version of scenarioo? If not, continue with next step, otherwise do the following first ... 
  * Add configuration for this new version to configuration in https://github.com/scenarioo/scenarioo/blob/develop/docs/book.json
  * Configure that version to be selected in the book.json
  * In scenarioo.github.io web page repository: Create a new subfolder in folder `docs` for that new version

* Make sure the book.json has the correct version you are going to release selected in the configuration (`"selected": true`)

* Run `npm run build` in `docs` folder.

* Copy the generated content to the scenarioo.github.io repository:
    * Select all content of `docs/_book` to copy (without the .gitignore file)
    * Copy to scenarioo.github.io repo into the correct sub folder of `docs` for the version you want to deploy
    * Make sure the target folder is cleaned first, if there was already an old version of the docu deployed for that same version, before you copy.
    * If this is the docu of the current official released version, please publish both to its concrete version directory as well as to folder `master`.
    
* Commit and push the added / updated documentation versions to master branch on scenarioo.github.io

* When done: make sure that you changes to scenarioo/scenarioo are merged to develop and on develop verify in `book.json` that the Developer version is configured as selected again!
