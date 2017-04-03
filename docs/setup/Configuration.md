# Viewer Configuration

Most of the features that are configurable in Scenarioo can be configured directly through the Configuration pages inside the Scenarioo webapplication. Just use the link "Manage" in the upper right corner.

The following sections describe more advanced configuration options that can only be configured through the viewer's `config.xml` file and can not be configured yet through the Scenarioo configuration pages directly. 

All the listed configuration possibilities are optional and you only need to follow these instructions if you want to use these additional features.

## Full Text Search

For enabling full text search inside your scenarioo documentation you have to setup an elastic search engine, as described in [Full Text Search Setup Instructions](../features/full-text-search/setup.md)

## Diff Viewer Comparisons

If you want to enable diff comparisons between two versions of your generated documentation builds (e.g. to see what changed in the last automated build run compared to another build run, including also screenshot comparisons) you have to follow the [DiffViewer Setup Instructions](../features/diff-viewer/setup.md)

## Custom Object Tabs

By adding custom object tabs to your scenarioo home page, you can enable browsing all objects of some type(s) (almost the same way as use cases can be browsed on the entry page). This feature currently has to be enabled by adding some XML fragment to your Scenarioo configuration file.

How to add objects of arbitrary types as details to your scenarioo documentation content is described in [Details & Object Repository](../features/Details.md).

The following XML fragment added to your configuration file, will make all your objects in your scenarioo documentation of the types listed as `<objectTypesToDisplay>` browsable in one searchable object tree inside a new tab called "Calls" on the home page of Scenarioo. This example lists all objects of type "service", "businessOperation" and "uiAction" in one tree, just as you can see it in the Demo of Scenarioo. Also the details properties "description" and "realName" are displayed in the resulting tree table as columns.

```xml
    <customObjectTabs>
        <id>calls</id>
        <tabTitle>Calls</tabTitle>
        <objectTypesToDisplay>service</objectTypesToDisplay>
        <objectTypesToDisplay>businessOperation</objectTypesToDisplay>
        <objectTypesToDisplay>uiAction</objectTypesToDisplay>
        <customObjectDetailColumns>
            <columnTitle>Description</columnTitle>
            <propertyKey>description</propertyKey>
        </customObjectDetailColumns>
        <customObjectDetailColumns>
            <columnTitle>Real Name for Service</columnTitle>
            <propertyKey>realName</propertyKey>
        </customObjectDetailColumns>
    </customObjectTabs>
```
This example is an extract from the demo configuration, for full example configuration file see [config.xml](https://github.com/scenarioo/scenarioo/blob/develop/scenarioo-server/src/main/resources/config-for-demo/config.xml)

## Branch Selection List Ordering

The ordering of the Branch Selection is configurable. 

Through an additional config property `branchSelectionListOrder` the following ordering options can be configured:
* `name-ascending`: the branches are sorted by name alphabetically - default value, if value not set the behaviour is the same
* `name-descending`: branches are sorted in descending order according their branch name - useful for projects that have names with version or release date or stuff like that inside
* `last-build-date-descending`: branches are sorted by the last build date

Example:
```xml
<branchSelectionListOrder>name-ascending</branchSelectionListOrder>
```

## IMPORTANT Remarks

1. Changing the configuration file manually always needs a server restart such that the changes take effect.

2. Defining a new tab in the configuration, needs reimport of your builds to generate the data for the configured tabs. Just go to the "Manage" page and click on the refresh icons behind each build in the tab "Builds" to trigger a reimport.

You can also define more than one such tab, and a tab can also only list objects of one type (just as a list, no tree then).

The default configuration since scenarioo version 2.0 already comes with two such simple object tabs predefined: Labels and Pages, to list all pages and labels in your documentation. If you upgrade from version 1.x you have to manually enable this two tabs by adding the following to your configuration:

```xml
     <customObjectTabs>
        <id>pages</id>
        <tabTitle>Pages</tabTitle>
        <objectTypesToDisplay>page</objectTypesToDisplay>
     </customObjectTabs>
     <customObjectTabs>
        <id>labels</id>
        <tabTitle>Labels</tabTitle>
        <objectTypesToDisplay>label</objectTypesToDisplay>
     </customObjectTabs>
```
In case you are using the default version 2.0 configuration and you do not want to see "Labels" and "Pages" in your documentation, you can remove those tabs by removing this configuration part from your config.xml file and restart the server.

