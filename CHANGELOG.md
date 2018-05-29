# Scenarioo

This file lists the changes in newer versions of scenarioo.

## Version 4.0.0 

### Feature "Configuration"

* The data directory can not be configured through the config UI anymore (for security reasons)
* The data directory can now be configured through an environment variable or in webserver's context.xml properties (or just by directory mapping on the docker container, similar to before).
* The configuration file of scenarioo is now as well stored in same data directory. Make sure you move your old config.xml file to this same scenarioo data directory.
* See [Migration Guide](docs/setup/Migration-Guide.md) for more detailed explanations.

### Feature "Diff Viewer Plus"

Improvements to the Diff Viewer:

* [#602 - Removed Dependency to GraphicMagick](https://github.com/scenarioo/scenarioo/issues/602): GraphicMagick needs not to be installed anymore to use the DiffViewer feature.
* [#618 - New DiffViewer Internal Diff Screen Format](https://github.com/scenarioo/scenarioo/issues/618) The comparison builds need now less disk space because only a difference image per screenshot is stored. Needs reimport of builds to see comparisons again, see [Migration Guide](docs/setup/Migration-Guide.md).
* [#597 - Store diff information directly inside build](https://github.com/scenarioo/scenarioo/issues/597): Also the comparisons are stored now inside the belonging build directories, such that the diffs get automatically cleaned up when deleting a build. Breaking change, see [Migration Guide](docs/setup/Migration-Guide.md).
* [#603 - Improved Comparison View](https://github.com/scenarioo/scenarioo/issues/603) to understand the comparison of two screenshots and the possible comparison view options better. Also highlighting of changes is now available in both screenshots.
* [#596 - Configure Highlight Color for Changes in DiffViewer Comparison Screenshots](https://github.com/scenarioo/scenarioo/issues/596): new config property `diffImageColorRgbaHex` to customize the highlight color.
* [#577 - Avoid Null Pointer Exceptions](https://github.com/scenarioo/scenarioo/issues/577): Do not log null pointer exceptions when there is just no step to compare too.

### Features "Small Improvements & Fixes"

Further small improvements and bug fixes:

* [#601 - Branch Selection Order](https://github.com/scenarioo/scenarioo/issues/601): new configuration property `branchSelectionListOrder` to configure order of branches in the branch selection list. 
* [#630 - Hide Screen Annotation Button without Annotations](https://github.com/scenarioo/scenarioo/issues/630): bugfix to hide the button again if there are no annotations to display
* [#628 - Improve logging format (log4j.properties) to be less verbose](https://github.com/scenarioo/scenarioo/issues/628)

### Breaking Changes and Backwards Compatibility

* **Changed configuration of data directory location:** You need to setup your data location and the configuration file 
as explained in the [Migration Guide](docs/setup/Migration-Guide.md). This can not be configured through the frontend anymore.

* **Internal format of comparisons between builds (DiffViewer feature) has changed:** After an update to the new version you will see no calculated comparisons on old builds anymore and need to reimport those builds for which you want to calculate and see comparisons again. See Step 3 in [Migration Guide](docs/setup/Migration-Guide.md).

* **Full Text Search needs Elastic Search Version 5:** We upgraded the full text search feature to use ElasticSearch 5. If you want to use that feature your have to configure a compatible ElasticSearch server and a cluster name as described in [Full Text Search Setup Guide](docs/features/full-text-search/setup.md)

* **Some undocumented REST services for the frontend have slightly changed their URLs:** in case you used these services you have to adjust the usage:
    * `/builds/reimportBuild/{branchName}/{buildName}` is now `/builds/:branchName/:buildName/import`

Apart from that there are no breaking changes.

The Java Writer Library in use is still version 2.1 which is compatible with Java 6. 
Also the internal format version is not increased (still 2.1) so that your existing documentation data 
does not even have to be reimported after the upgrade and will continue to work with the new version (except for the comparisons).

### Fixed Issues

All issues fixed in this release: [List of fixed issues](https://github.com/scenarioo/scenarioo/milestone/33?closed=1).

### Known Issues

Please visit our [list of known issues on GitHub](https://github.com/scenarioo/scenarioo/labels/known-issue).

### Feedback and Support

Don't hesitate to open new issues if you find a bug or problem, or have feedback or questions:
https://github.com/scenarioo/scenarioo/issues/new?labels=feedback

  
## Version 3.0.0 and Earlier 

See https://github.com/scenarioo/scenarioo/releases
  
