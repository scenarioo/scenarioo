# Scenarioo Release Procedure

We use git flow (see [branching strategy](Branching-strategy.md)). Therefore the procedure is:

### Increase internal format version

Set `org.scenarioo.business.aggregator.ServerVersion.DERIVED_FILE_FORMAT_VERSION` to a new version, usually the same as the Scenarioo version that you are going to release. If you are a 100% sure that the internal format did not change since the last release, you can keep the version from the previous release.

### Prepare release notes

* Create the release notes using GitHubs infrastructure: https://github.com/scenarioo/scenarioo/releases. Make sure you safe them as a draft and do not publish them yet.

### Create release branch and make last fixes

* Create a release branch, e.g. execute `git flow release start 1.0.1` while you have checked out the `develop` branch.
* Test it thoroughly.
* Fix the bugs you find and commit them to the release branch.
* Push the release branch and make sure it builds on our [build server](Build-Server).

### Finalize release
* Finalize the release, e.g. using `git flow release finish 1.0.1`. Add the correct tag when git flow asks you for the tag (we only use version numbers for tags, without a leading "v"). Git flow will merge the branch into `master` and `develop`. Therefore you have to push these two branches.
* Push the tag to the master branch using `git push --tags`.
* Delete the release branch. E.g. here: https://github.com/scenarioo/scenarioo/branches.

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
