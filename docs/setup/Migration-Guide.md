# Migration Guide

## Migration from Scenarioo viewer 3.x to 4.x

### Moving the config.xml file to the same location as the Scenarioo data

Starting from version 4.x it is not possible anymore to store the configuration file in another directory than the rest of the Scenarioo data. It is also not possible anymore 
to give it a different name than `config.xml`. The data directory will be determined by Scenarioo at application startup and you will no longer be able to set it in the configuration.

Scenarioo will determine the data directory according to the following rules and order:

* If there is a Tomcat context variable called `scenariooDataDirectory` configured in your Tomcat's context.xml file, Scenarioo will take the directory path from there
* If there is a set environment variable called `SCENARIOO_DATA`, Scenarioo will take the path from there
* Else, Scenarioo will fallback to the `.scenarioo` directory in the user's home folder

You can migrate your Scenarioo installation with the following steps:

* If you did not make use of the context variable feature at all you can either
  * copy the contents of your existing data directory to the `.scenarioo` folder in the home directory of the Tomcat user (where your config.xml file is placed by default)
  * or use one of the rules above to set the path to your existing data directory and copy the config.xml to the same directory
* If you previously have set your configuration directory via context variable
  * copy your config.xml file to the same directory as your Scenarioo data
  * rename the `scenariooConfigurationDirectory` variable in your context.xml to `scenariooDataDirectory`
  * set the value of the `scenariooDataDirectory` variable to the same path where your Scenarioo data is located
* If you previously have set the name of your configuration file via context variable
  * rename it to `config.xml` as it is not possible anymore to name it differently
  * remove the `scenariooConfigurationFilename` variable from the context.xml file as it is not needed anymore

In all cases you can optionally also remove the `testDocumentationDirPath` in your configuration file, but it will eventually be removed automatically as soon as the config file is saved again by the Scenarioo application.
  


