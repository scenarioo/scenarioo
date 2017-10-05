# Scenarioo

## Version 4.0.0 

### Feature "Data Directory Configuration"

* The data directory can not be configured through the config UI anymore (for security reasons)
* The data directory needs to be configured via webapp context.xml properties or environment variable (or just by directory mapping on the docker container, similar to before).
* The config.xml is now as well stored inside the same data directory. Make sure you move your old config.xml file to this same scenarioo data directory.
* See [Migration Guide](docs/setup/Migration-Guide.md) for more explanations.

### Feature "Diff Viewer Plus"

Many small improvements for the diff viewer feature:
* [#596 - Configure Highlight Color for Changes in DiffViewer Comparison Screenshots](https://github.com/scenarioo/scenarioo/issues/596): new property `diffImageColorRgbaHex` to customize the highlight color.
* [#618 - New DiffViewer Internal Data Format](https://github.com/scenarioo/scenarioo/issues/618) The comparison builds need now less disk space because only a difference image per screenshot is stored. Also the comparisons are stored now inside the belonging build, such that the diffs get automatically cleaned up when deleting a build. See as well migration guide further below.
* [#603 - Improved Comparison View](https://github.com/scenarioo/scenarioo/issues/603) to understand the comparison of two screenshots and the possible comparison view options better. Also highlighting of changes in both screenshots is now possible.
* [#602 - Removed Dependency to GraphicMagick](https://github.com/scenarioo/scenarioo/issues/602): GraphicMagick needs not to be installed anymore to use the DiffViewer feature.
* [#577 - Avoid Null Pointer Exceptions](https://github.com/scenarioo/scenarioo/issues/577): Do not log null pointer exceptions when there is just no step to compare too.

### Features "Small Improvements"

Further small improvements:

* [#601 - Branch Selection Order](https://github.com/scenarioo/scenarioo/issues/601): new configuration property `branchSelectionListOrder` to configure order of branches in the branch selection list. 

### Breaking Changes and Backwards Compatibility

* Changed configuration of data directory location: After installation you need to setup your data location and the configuration file as explained in the [Migration Guide](docs/setup/Migration-Guide.md). This can not be configured through the frontend anymore.

* Internal format of DiffViewer feature (build comparisons) has been changed and even the place where those are stored. After an update to the new version you will see no calculated comparisons anymore and need to reimport those builds for which you want to calculate and see comparisons.

Apart from that there are no breaking changes.

The Java Writer Library in use is still version 2.1 which is compatible with Java 6. Also the internal format version is not increased (still 2.1) so that your existing documentation data does not even have to be reimported after the upgrade and will continue to work with the new version.

### Fixed Issues

All issues fixed in this release: [List of fixed issues](https://github.com/scenarioo/scenarioo/milestone/33?closed=1).

### Known Issues

Please visit our [list of known issues on GitHub](https://github.com/scenarioo/scenarioo/labels/known-issue).

### Feedback and Support

Don't hesitate to open new issues if you find a bug or problem, or have feedback or questions:
https://github.com/scenarioo/scenarioo/issues/new?labels=feedback

  
## Version 3.0.0 and Earlier 

See https://github.com/scenarioo/scenarioo/releases
  
