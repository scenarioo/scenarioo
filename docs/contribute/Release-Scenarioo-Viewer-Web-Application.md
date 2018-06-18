# Scenarioo Release Procedure

We use the git flow branching strategy (see [branching Strategy](Branching-strategy.md)).

Whenever you see `<version>` in this guide we mean a version number in the form `1.2.0` (without `v` prefix!).

## Set Release Versions

In `build.gradle` you have to adjust following version information:

* `documentationVersion`: this is the version that identifies the documentation URL for this release, which is http://scenarioo.org/docs/{documentationVersion} for links into the documentation of that same release version. Please put the major version (e.g. "4.0") of the new release you gona to release (see releasing the docu later).

* `scenariooApiVersion`: which writer library version to use in this release. If the library has been changed, we have to release it first. Use a non snapshot version for the release in any case!

* `scenariooAggregatedDataFormatVersion`: set it to a new version in case the internal aggregated data format somehow changed in a release, which makes it necessary on an update, that all imported builds need to be reimported. 
Usually this should be the same as the Scenarioo version that you are going to release. If you are a 100% sure that the internal format did not change since the last release, you can keep the version from the previous release.

**Search Documentation for occurences of old verison number and replace with new version:**
* Do a global search for the old version!
* update [Scenarioo Viewer Doker Image](../tutorial/Scenarioo-Viewer-Docker-Image.md)
* update any other occurences 
                   
## Prepare Release Notes and Migration Guide

* Go to file `CHANGELOG.md` and prepare the release notes for the next version
* If needed write a migration guide for the new version in [Migration Guide](../tutorial/Migration-Guide.md)
* Let both review by the team and finalize these important release documents.

## Create Release Branch and Stabilize

* Create a release branch:
  * Make sure you have checked out the `develop` branch and are updated to latest changes.
  * `git checkout -b release/<version> develop`
* Push the release branch and make sure it builds on our [build server](Build-Server).
* On the develop branch: change the docu version back to "develop" to be ready for development for next release.
* Inform the team about the release branch and that release stabilisation has to be done on that branch as follows.
* Test the release version thoroughly on the demo.
* Ask some devs to maybe do some release candidate testing in their projects.
* Fix the bugs you find and merge the fixes to the release branch (instead of develop directly!).
* Also proceed with further release preparations as explained in next sections

## Prepare Documentation and Webpage for new Release Version

* See [Documentation and Webpage](Documentation-and-Webpage.md) for further instructions how to configure the documentation for a new release.
 
* Prepare everything as described in that other instructions on the release branch:
   * You should at least check that the docu version for the new release branch is properly published under `http://www.scenarioo.org/docs/{version}` (e.g. `/4.0`) - it should be published automatically by Jenkins pipeline on your release branch (see Build Results Page).
   * Verify also in the published docu for the new version, that the correct new version is displayed and selected in upper left version selection dropdown.
   
* If for the release the web page needs to be upgraded prepare so on a special release branch as well.
 
## Merge and Build Final Release

* **Finalize the release by merging:**
  * Fast forward master to release
    ```
    git checkout master
    git merge --ff-only release/<version>
    git push
    git tag -a <version>
    git push --tags
    ```
  * Merge release branch into develop (if not done yet)
    ```
    git checkout develop
    git merge release/<version>
    git push
    ```
  * Make sure the master branch builds on the build server. As the build uses the git tag as a version number, it's important to distribute a build artifact that was build after the tag was pushed!

* **DO NOT delete the release branch. We will keep the release branch to do improvements on the documentation for the just released version (or if ever needed: for hotfixes as well)**. Because usually short after a release we get feedback about how to improve that version of the docu. Such things have still to be fixed on the release branch and merged both to master and develop, such that those changes are in the published documentation of that version!

### Build Docker Image and Publish to Dockerhub

* Create a new Docker image for this release according to [this manual](Building-the-Docker-Image)
* Do not forget to update the usage instructions for the Docker Image:     
    [Scenarioo Viewer Doker Image](../tutorial/Scenarioo-Viewer-Docker-Image.md) 
    (**!! change the version number in the example!**)

### Publish the Release on Github

* Create a release on github to make the release publicly visible: https://github.com/scenarioo/scenarioo/releases. 
* For description use the release notes of that release version from CHANGELOG.md in the release description on github.
* Make sure you safe it first only as a draft and do not publish them yet.  
* Attach the binary of the release (WAR-file) to the release notes (see area "Attach binaries by dropping them here ...").
* Publish the release when done.

### Publish the Release on Maven Central

1. You will need to specify the following properties in your gradle.properties located in your gradle home directory:

```
signing.keyId=BDCAAE60
signing.password=#private key goes here#
signing.secretKeyRingFile=#secret key file goes here#
ossrhUsername=scenarioo
ossrhPassword=#sonatype password goes here#
```

2. Change the version appropriately in the build.gradle
3. `gradlew clean uploadArchives`
4. Promote build to maven central:
http://central.sonatype.org/pages/releasing-the-deployment.html

### Release Postprocessing

**Change following things on develop branch** to prepare for further work on further releases:
* Check `documentationVersion` in `build.gradle` is changed to `develop` again, such that it points to current latest and greatest developer docu, until we do a new release
* Verify that in `docs/book.json` the develop version of the docu is marked as selected again.

 
