# Scenarioo Release Procedure

We use the git flow branching strategy (see [branching strategy](Branching-strategy.md)).

Whenever you see `<version>` in this guide we mean a version number in the form `1.2.0` (without `v`prefix).

### Set Release Version & Release Branch

In `gradle.build` you have to adjust following version information:

* `documentationVersion`: this is the version that identifies the documentation URL for this release, which is http://scenarioo.org/docs/{documentationVersion} for links into the documentation of that same release version. Please put the major version (e.g. "4.0") of the new release you gona to release (see releasing the docu later).

* `scenariooAggregatedDataFormatVersion`: set it to a new version in case the internal aggregated data format somehow changes, which nakes it necessary on an update, that all imported builds need to be reimported. Usually this should be the same as the Scenarioo version that you are going to release. If you are a 100% sure that the internal format did not change since the last release, you can keep the version from the previous release.

### Prepare release notes

* Create the release notes using GitHubs infrastructure: https://github.com/scenarioo/scenarioo/releases. Make sure you safe them as a draft and do not publish them yet.

### Create release branch and make last fixes

* Create a release branch:
  * Make sure you have checked out the `develop` branch.
  * `git checkout -b release/<version> develop`
* Test it thoroughly.
* Fix the bugs you find and commit them to the release branch.
* Push the release branch and make sure it builds on our [build server](Build-Server).

### Finalize release

* Finalize the release:
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
    git branch -d release/<version>
    git push
    ```

* DO NOT delete the release branch. We will keep the release branch to provide links to the right documentation version
  and we can even reuse the release branch as a branch to do and test hotfixes on it.

### Build master branch

* Make sure the master branch builds on the build server. As the build uses the git tag as a version number,
 it's important to distribute a build artifact that was build after the tag was pushed!

### Create and push Docker image

* Create a new Docker image for this release according to [this manual](Building-the-Docker-Image)
* Do not forget to update the usage instructions for the Docker Image:     
    [Scenarioo Viewer Doker Image](../setup/Scenarioo-Viewer-Docker-Image.md) 
    (**!! change the version number in the example!**)

### Finish release notes and create links
* Attach the binary of the release (WAR-file) to the release notes (see area "Attach binaries by dropping them here ...").
* Publish the release notes.

### Upload to maven central
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


### Publish Versioned Documentation

See [Documentation and Webpage](Documentation-and-Webpage.md).
