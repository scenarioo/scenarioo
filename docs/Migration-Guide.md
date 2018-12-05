# Scenarioo Viewer Migration Guide

Guide on how to upgrade for major versions of scenarioo.

## 4.x to 5.x

### Migration to Spring Boot

* The Scenarioo viewer has been migrated to a Spring Boot application. You can now run Scenarioo as a standalone WAR file in addition to deploying it to a webserver.
    * Refer to the [Scenarioo setup guide](tutorial/Scenarioo-Viewer-Web-Application-Setup.md) for more information about the different ways to run Scenarioo.
* The REST endpoint for uploading builds is now secured via Spring Security instead of standard servlet security mechanisms. This changes the way that the user/password is configured.
    * User and password for the HTTP authentication are now configured via Spring Boot application properties instead of an entry in the `tomcat-users.xml`.
    * If you were using this feature before, read the section on [how to publish documentation data](tutorial/Publish-Documentation-Data.md#b-http-post-request) to see how this is done differently now.

## 3.x to 4.x

### Breaking Changes:

* Starting with version 4.x the path to your Scenarioo documentation data has to be configured differently and the Scenarioo configuration file (`config.xml`) must be located in the same directory as all your other Scenarioo documentation data.

* For the full text search Scenarioo now uses Elasticsearch Server Version 5.6.9, you will have to upgrade that as well, in case you want to continue to use this feature.

* Also all already calculated Diff Viewer comparison data is not valid for the new version anymore and therefore has to be recalculated after update by simply reimporting those builds for which you still want to see calculated Diff Viewer comparisons.

* Some undocumented REST endpoints for the frontend have slightly changed: in case you used these services you have to adjust the usage: `/builds/reimportBuild/{branchName}/{buildName}` is now `/builds/:branchName/:buildName/import`

### Migration:

Follow these simple steps to migrate:

0. Make sure you know what is the Scenarioo data directory configured on your old Scenarioo installation:

    * Simply go to the `Manage`-Tab in the old version of Scenarioo viewer web app and choose `General Settings` to see `Documentation Data Directory Path` (this setting will not be available in the new UI anymore! It will be just read only in future!).
    * Or: check value of `testDocumentationDirPath` in your `config.xml` file (see next step)

1. Copy the Scenarioo `config.xml` file to that same data directory where all your other Scenarioo documentation data is stored, as explained here:
    * How to find the `config.xml` file:
        * Scenarioo Docker Image: 
           * so far the config file was only stored inside the docker container, in the directory `{user.home}/.scenarioo` - so just copy it from there and in the future the config will survive even when you start a new Scenarioo docker container, which is an important benefit of this change!
        * Scenarioo on Tomcat or other web servers:
           * you will find the `config.xml` file under `{user.home}/.scenarioo` or in the config directory that you configured in your webserver's `context.xml` file in property `scenariooConfigurationDirectory` or `configurationDirectory`.
           * the file might have been even configured to have a different name than `config.xml` in your `context.xml` file using property `scenariooConfigurationFilename` or `configurationFilename` (you will have to rename that file to `config.xml` after you copied it!).
           * you can later remove those settings for the config file location from your `context.xml`, if you configured it there, since the location is now fix in your data directory and can not be configured separately anymore. 
        * If you do not find the config file at all: this might mean that you never saved your configuration, Scenarioo might just be running using its internal default configuration. Go to `Manage`-Tab in the Scenarioo viewer web app and choose `General Settings / Save` this will save the file for you with current settings.
     * When you found the config file then copy it to the Scenarioo data directory
        * Copy the file to the root of your Scenarioo data path (see step 0 if you forgot)
        * In case your config file had a different name, then rename the copied file to `config.xml` (this file can not be named differently anymore!)
        * You can also remove the setting `testDocumentationDirPath` from the copied file's content, since it will be ignored in future versions (see next point on how to configure that directory in new versions).
        
2. If your old installation had an Elasticsearch server configured - which is only the case if you find the config property "elasticSearchEndpoint" in `config.xml` - then you have to setup a newer Elasticsearch server of appropriate version:
    * Install and configure an Elasticsearch server with newer version 5.6.9
    * See [Full Text Search Setup Guide](features/full-text-search/setup.md) for further instructions. 
    * If you want to generate search indexes on the new server for existing Scenarioo documentation builds, you can trigger a reimport for those builds in the Scenarioo Manage Builds tab later (once Scenarioo upgrade is done).
        
3. Configure, update and restart Scenarioo:
    * Using Scenarioo Docker Image:
      * simply stop the old Scenarioo and restart a new one as explained here: [Scenarioo Viewer Docker Image](tutorial/Scenarioo-Viewer-Docker-Image.md)
      * take care to configure your Scenarioo data path on startup properly, as explained in above link, by using the slightly changed parameter to map your documentation data directory to `/scenarioo/data` inside the container (this path is different in new verson).
    * Using WAR Deployment, e.g. on Tomcat:
      * Configure the docu data directory in one of the following ways:
        * if the directory was `.scenarioo` in user's home, which is the default, you do not have to change anything.
        * you can configure the directory now by setting an environment variable `SCENARIOO_DATA`
        * or you can set it in `context.xml` of your webserver as a context variable called `scenariooDataDirectory`
        * for more information on that see [Web Application Setup](tutorial/Scenarioo-Viewer-Web-Application-Setup.md)
      * Download and install newest WAR and restart your server as explained in [Web Application Setup](tutorial/Scenarioo-Viewer-Web-Application-Setup.md)
         
4. Optional but Recommended - only if you used Diff Viewer Comparisons before:
    * Delete all old Diff Viewer comparison data, which is stored in your docu data directory under `scenarioo-application-data\diffViewer`. 
    * From now on this data will be stored in a less disk space consuming format and inside the same directory where the build it belongs to is located.
    * If you want to see comparisons for any of the old builds, you have to reimport these builds under `Manage / Builds`, this will also trigger recalculation of any configured comparisons. See [Diff Viewer Setup Guide](../features/diff-viewer/setup.md) on how to configure such comparisons (which has NOT changed!).

### In case of troubles with the migration:

Let us know immediately here: 
https://github.com/scenarioo/scenarioo/issues/new?labels=feedback
