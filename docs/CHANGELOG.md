# Scenarioo Release Notes

## Version 5.0.0

### Scenarioo Standalone Runner

* [#724 - Scenarioo Standalone Runner](https://github.com/scenarioo/scenarioo/issues/724): From now on you can run scnenarioo without needing to install a Tomcat or similar java application server, just refer to our [Scenarioo Setup Guide](tutorial/Scenarioo-Viewer-Web-Application-Setup.md) 
* [#778 - Secured REST Endpoints using Spring Security](https://github.com/scenarioo/scenarioo/issues/788): The way the api key (or password) for using secured REST endpoints has to be configured has fundamentaly changed by introducing Spring Security. For more details, please refer to the [Migration Guide](Migration-Guide.md) 

### Small Fixes and Improvements

* [#717 - Diff Viewer: Styling and UI issues](https://github.com/scenarioo/scenarioo/issues/717): Various usability improvements.
* [#727 - Build Import: UI issues](https://github.com/scenarioo/scenarioo/issues/727): Information displayed while a build is being imported was improved. 
* [#895 - Share this page Screenshot link broken for Sketch](https://github.com/scenarioo/scenarioo/issues/895): The share this page dialog shows the correct image link again when sharing sketches.
* [#900 - Application Info Popup disappears if query parameters were not provided](https://github.com/scenarioo/scenarioo/issues/900): The application information popup will now also be shown if the application is visited for the first time without query parameters.

### Breaking Changes and Backwards Compatibility

* **Changed Configuration of password for secured REST API calls:** 
The security mechanism for secured REST endpoints (e.g. to upload a build via REST call) has changed. Therefore the password for these rest endpoints needs to be configured differently. For more details, please refer to the [Migration Guide](Migration-Guide.md) on how to adjust this configuration when migrating.

Apart from this small config change there are no other breaking changes. 

You can still deploy Scenarioo as a WAR to your Tomcat, although this is not needed anymore by using the new standalone deployment, as mentioned above. 

### Supported Scenarioo Format

* The used Scenarioo Data format is still version 2.1. You can safely use Scenarioo 5.0 with the same data format as before and do not have to upgrade the used Scenarioo writer library to use the new Scenarioo viewer version. 
* The internal format has not been changed either, so you do not even have to reimport your builds that have been imported with the previous version in the data directory. All build and comparison data will still work with the new version.

### Fixed Issues

All issues fixed in this release: [List of Fixed Issues](https://github.com/scenarioo/scenarioo/milestone/36?closed=1).

### Known Issues

Please visit our [List of Known Issues on GitHub](https://github.com/scenarioo/scenarioo/issues?q=is%3Aopen+is%3Aissue+label%3A%22known+issue%22).

### Feedback and Support

Don't hesitate to open new issues if you find a bug or problem, or have feedback or questions:
https://github.com/scenarioo/scenarioo/issues/new?labels=feedback

## Version 4.0.1

### Bugfixes

* [#751 - Sharing Step Screenshot](https://github.com/scenarioo/scenarioo/issues/751) Sharing screenshot images through direct links with other applications works again, also the share this page dialog shows the correct link again.

## Version 4.0.0 

### Feature "Diff Viewer Plus"

Improvements to the Diff Viewer for Visual Regression Testing:

* [#692 - Create Comparison Manually through the Web Frontend](https://github.com/scenarioo/scenarioo/issues/692): user can create a comparison through a modal dialog.
* [#603 - Improved Comparison View](https://github.com/scenarioo/scenarioo/issues/603) to understand the comparison of two screenshots and the possible comparison view options better. Highlighting of changes is now available in both screenshots.
* [#605 - Comparison REST API to trigger Comparisons from Automated CI Test Pipelines](https://github.com/scenarioo/scenarioo/issues/605) to more easily integrate Diff Viewer comparisons for visual regression testing into your automated test pipeline.
* [#650 - Manage Comparisons View](https://github.com/scenarioo/scenarioo/issues/650): To see all calculated comparisons, their error logs and have the ability to trigger a comparison for recalculation if needed.
* [#673 - Comparison Configurations with Regexp for base branches to compare](https://github.com/scenarioo/scenarioo/issues/673): allow to define comparisons against another build to be calculated on multiple branches selected by regexp (e.g. for all feature branches).
* [#596 - Configure Highlight Color for Changes in DiffViewer Comparison Screenshots](https://github.com/scenarioo/scenarioo/issues/596): new config property `diffImageColorRgbaHex` to customize the highlight color.
* [#604 - Background Calculation of Comparisons](https://github.com/scenarioo/scenarioo/issues/604): to not block builds from being imported while a comparison is computed.
* [#597 - Store diff information directly inside build](https://github.com/scenarioo/scenarioo/issues/597): the comparisons are now stored inside the build directory, such that the comparisons get automatically cleaned up when deleting a build. Breaking change, see [Migration Guide](Migration-Guide.md).
* [#602 - Removed Dependency to GraphicsMagick](https://github.com/scenarioo/scenarioo/issues/602): GraphicsMagick needs not to be installed anymore to use the DiffViewer feature.
* [#618 - New Diff Viewer Internal Diff Screen Format](https://github.com/scenarioo/scenarioo/issues/618) The comparisons need less disk space because only a difference image per screenshot is stored. Needs reimport of builds to see comparisons again, see [Migration Guide](Migration-Guide.md).
* [#577 - Avoid Null Pointer Exceptions](https://github.com/scenarioo/scenarioo/issues/577): do not log null pointer exceptions when there is just no step to compare to.

### Feature "Configuration"

* The data directory can not be configured through the config UI anymore (for security reasons)
* The data directory can now be configured through an environment variable or in the `context.xml` file of the web server (or just by directory mapping on the docker container, similar to before).
* The configuration file of Scenarioo is now stored in the data directory. Make sure you move your old `config.xml` file to the data directory.
* See [Migration Guide](Migration-Guide.md) for more detailed explanations.

### Features "Small Improvements & Fixes"

Further small improvements and bug fixes:

* [#601 - Branch Selection Order](https://github.com/scenarioo/scenarioo/issues/601): new configuration property `branchSelectionListOrder` to configure order of branches in the branch selection list. 
* [#630 - Hide Screen Annotation Button if there are no Annotations](https://github.com/scenarioo/scenarioo/issues/630): bugfix to hide the button if there are no annotations to display.
* [#628 - Improve logging format (log4j.properties) to be less verbose](https://github.com/scenarioo/scenarioo/issues/628).
* [#651 - Generic Request Logging for all REST calls to the backend](https://github.com/scenarioo/scenarioo/issues/651).

### Breaking Changes and Backwards Compatibility

* **Changed configuration of data directory location:** you need to setup your data location and the configuration file 
as explained in the [Migration Guide](Migration-Guide.md). This can not be configured through the frontend anymore.

* **Internal format of comparisons between builds (DiffViewer feature) has changed:** after an update to the new version you will see no calculated comparisons on old builds anymore and need to reimport those builds for which you want to calculate and see comparisons again. See step 3 in [Migration Guide](Migration-Guide.md).

* **Full Text Search needs Elasticsearch Version 5:** we upgraded the full text search feature to use Elasticsearch 5.6.9. If you want to use that feature your have to configure a compatible Elasticsearch server and a cluster name as described in [Full Text Search Setup Guide](features/full-text-search/setup.md)

* **Some undocumented REST endpoints for the frontend have slightly changed:** in case you used these services you have to adjust the usage:
    * `/builds/reimportBuild/{branchName}/{buildName}` is now `/builds/:branchName/:buildName/import`

Apart from that there are no breaking changes.

The Java Writer Library in use is still version 2.1 which is still compatible with Java 6 and upwards. 
Also the internal format version is not increased (still 2.1) so that your existing documentation data 
does not even have to be reimported after the upgrade and will continue to work with the new version (except for the comparisons).

### Fixed Issues

All issues fixed in this release: [List of Fixed Issues](https://github.com/scenarioo/scenarioo/milestone/33?closed=1).

### Known Issues

Please visit our [List of Known Issues on GitHub](https://github.com/scenarioo/scenarioo/issues?q=is%3Aopen+is%3Aissue+label%3A%22known+issue%22).

### Feedback and Support

Don't hesitate to open new issues if you find a bug or problem, or have feedback or questions:
https://github.com/scenarioo/scenarioo/issues/new?labels=feedback

  
## Version 3.0.0 and Earlier 

See https://github.com/scenarioo/scenarioo/releases
  
