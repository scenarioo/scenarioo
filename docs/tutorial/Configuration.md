# Viewer Configuration

Most of the features that are configurable in Scenarioo can be configured directly through the Configuration pages inside the Scenarioo webapplication. Just use the link "Manage" in the upper right corner.

Most settings are on tab "General Settings" in the "Manage" area. 

There are some advanced configuration options for some advanced features that can only be configured through the viewer's `config.xml` file and can not be configured yet through the Scenarioo configuration pages directly. 

This page briefly explains how you can use that `config.xml` file to configure more advanced options.

## How to change `config.xml`

1. Locate the `config.xml` file: it is in your scenarioo documentation data folder that you configured. If you do not find it there: It means either that you use an older version of scenarioo or that you never saved the configuration before. Go to the Scenarioo configuraiton web page and choose to save the settings once --> this will save the configuration to the file.

2. Edit the file - See next section about an example - and save your changes.

3. Restart scenarioo such that the changes are being loaded.

## Branch Selection List Ordering

The order of the branch entries in the top level navigation branch selection dropdown is configurable.

Through an additional config property `branchSelectionListOrder` the following ordering options can be configured:
* `name-ascending`: the branches are sorted by name alphabetically - default value, if value not set the behaviour is the same
* `name-descending`: branches are sorted in descending order according their branch name - useful for projects that have names with version or release date or stuff like that inside
* `last-build-date-descending`: branches are sorted by the last build date

Example to add in `config.xml` inside the `<configuration>`-element:
```xml
<configuration>
    ...
    <branchSelectionListOrder>name-descending</branchSelectionListOrder>
    ...
</configuration>
```

## More Advanced Feature Configurations

Please refer to the documentation of advanced features for more information on how to configure those features in `config.xml`. Like for example the following advanced features:

* [Details & Object Repository for additional object tabs on start page](../features/Details.md)
* [Full Text Search Setup Instructions](../features/full-text-search/setup.md)
* [DiffViewer Setup Instructions](../features/diff-viewer/setup.md)
