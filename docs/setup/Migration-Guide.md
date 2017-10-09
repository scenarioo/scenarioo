# Migration Guide

## Scenarioo Viewer 3.x (or lower) to 4.x

**Breaking Changes:**
* Starting with version 4.x the path to your scenarioo documentation data has to be configured differently and the scenarioo configuration file (config.xml) is in the same directory as all your other scenarioo documentation data.
* Also all calculated diff viewer comparison data will be lost, and has to be recalculated after update by reimporting the builds for which you want to see calculated comparisons.

**Migration:**

Follow these simple steps to migrate:

1. Backup / Copy your config.xml file to the same directory where your scenarioo documentation data is stored already:
  * If you are using the scenarioo docker image: the file was stored before only inside the docker container, in directory `{user.home}/.scenarioo` - so just copy it from there and in the future the config will also survive, even when you restart the docker container, which is a hughe benfit of this change!
  * Otherwise: you will find the config.xml file under `{user.home}/.scenarioo` or in the directory that you configured as variable `scenariooConfigurationDirectory` in your webserver's context.xml configuration file (you may want to remove these configuration values from context.xml, as those are not used anymore).
  * In any case: the file has to be named `config.xml` (it's not possible anymore to use a different name, as it was before)
  * If you do not find the config.xml file at all: this might mean that you never saved your configuration, scenarioo might just be running using its internal default configuration. Go to `Manage`-Tab in the scenarioo viewer web app and choose `General Settings / Save` this will save the file for you with current settings.
    
2. Configure, Update and restart Scenarioo as follows:
  * If using Scenarioo Docker Image you can simply stop the old docker image and restart a new one as explained here: [Scenarioo Viewer Docker Image](Scenarioo-Viewer-Docker-Image.md) - just take care to use the slightly changed paramter for mapping your documentation data directory on docker image startup.
  * If using your own Web Server (e.g. tomcat) with the Scenarioo WAR:
    * Before updating the WAR, make sure you know which data directory has been configured before:
      * Go to `Manage`-Tab in the old version of scenarioo viewer web app and choose `General Settings` to see `Documentation Data Directory Path` (this setting will not be available in new UI anymore!).
      * Or: check value of `testDocumentationDirPath` in your config.xml file (see point 1, this property will be ignored in new scenarioo versions)
    * Configure the docu data directory in one of the following ways:
       * if the directory was `.scenarioo` in user's home, which is the default, you do not have to change anything.
       * you can configure the directory now by setting an environment variable `SCENARIOO_DATA`
       * or you can set it in context.xml of your webserver as a context variable called `scenariooDataDirectory`
       * see [Web Application Setup](Scenarioo-Viewer-Web-Application-Setup.md) for more information on that.
    * Finally: Download and install newest WAR and restart your server.
         
3. Optional but Recommended - only if you used Diff Viewer Comparisons before:
  * Delete all old diff viewer comparison data, which is stored in your docu data directory under `scenarioo-application-data\diffViewer`. 
  * From now on this data will be stored in a less disk space consuming format and inside the same directory where the build it belongs to is located.
  * If you want to see comparisons for any of the old builds, you have to reimport these builds under `Manage / Builds`, this will also trigger recalculation of any configured comparisons. See [Diff Viewer Setup Guide](../feature/diff-viewer/setup.md) on how to configure such comparisons (which has NOT changed!).
